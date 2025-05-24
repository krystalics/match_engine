package com.github.krystalics.service.match_engine;

import com.alibaba.fastjson.JSON;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import org.apache.kafka.clients.consumer.*;

import java.sql.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Match {
    static Map<Long, String> ruleCache = new HashMap<>();
    static Map<String, Integer> commonSubExprs = new HashMap<>();
    static ExpressRunner runner = new ExpressRunner();
    static ExecutorService executor = Executors.newFixedThreadPool(8);

    public static void main(String[] args) throws Exception {
        // 加载 MySQL 规则
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hello", "root", "root123")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT rule_id, expression FROM rules");
            while (rs.next()) {
                ruleCache.put(rs.getLong("rule_id"), rs.getString("expression"));
            }
        }

        // 提取公共子表达式
        commonSubExprs = CommonSubExpressionExtractor.extractCommonSubExpressions(ruleCache);

        // Kafka 消费
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "matcher");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("data_topic"));

        List<Map<String, Object>> batch = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                batch.add(parseJson(record.value()));
                if (batch.size() >= 1000) {
                    processBatch(batch);
                    batch.clear();
                }
            }
        }
    }

    static Map<String, Object> parseJson(String json) {
        Map<String, Object> data = JSON.parseObject(json);
        data.put("city", ((String) data.get("city")).toLowerCase());
        return data;
    }

    static void processBatch(List<Map<String, Object>> batch) throws Exception {
        List<Future<List<Result>>> futures = new ArrayList<>();
        for (Map<String, Object> data : batch) {
            futures.add(executor.submit(() -> matchData(data)));
        }
        List<Result> results = futures.stream()
                .flatMap(f -> {
                    try {
                        return f.get().stream();
                    } catch (Exception e) {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());

        // 写入 MySQL
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db", "user", "pass")) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO match_results (data_id, rule_id, match) VALUES (?, ?, ?)");
            for (Result result : results) {
                stmt.setLong(1, result.dataId);
                stmt.setLong(2, result.ruleId);
                stmt.setBoolean(3, result.match);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    static List<Result> matchData(Map<String, Object> data) throws Exception {
        List<Result> results = new ArrayList<>();
        long dataId = ((Number) data.get("id")).longValue();
        IExpressContext<String, Object> context = new DefaultContext<>();
        data.forEach(context::put);

        // 预计算公共子表达式
        Map<String, Boolean> subExprResults = new HashMap<>();
        for (String subExpr : commonSubExprs.keySet()) {
            try {
                Boolean result = (Boolean) runner.execute(subExpr, context, null, true, false);
                subExprResults.put(subExpr, result);
            } catch (Exception e) {
                subExprResults.put(subExpr, false);
            }
        }

        // 计算完整规则，替换公共子表达式
        for (Map.Entry<Long, String> entry : ruleCache.entrySet()) {
            String expr = entry.getValue();
            try {
                // 替换公共子表达式（简化为查找）
                String optimizedExpr = optimizeExpression(expr, subExprResults);
                Boolean match = (Boolean) runner.execute(optimizedExpr, context, null, true, false);
                results.add(new Result(dataId, entry.getKey(), match));
            } catch (Exception e) {
                results.add(new Result(dataId, entry.getKey(), false));
            }
        }
        return results;
    }

    static String optimizeExpression(String expr, Map<String, Boolean> subExprResults) {
        // 示例：替换 "age > 30" 为 "true" 或 "false"
        for (Map.Entry<String, Boolean> entry : subExprResults.entrySet()) {
            if (expr.contains(entry.getKey())) {
                expr = expr.replace(entry.getKey(), entry.getValue().toString());
            }
        }
        return expr;
    }

    static class Result {
        long dataId, ruleId;
        boolean match;

        Result(long dataId, long ruleId, boolean match) {
            this.dataId = dataId;
            this.ruleId = ruleId;
            this.match = match;
        }
    }
}