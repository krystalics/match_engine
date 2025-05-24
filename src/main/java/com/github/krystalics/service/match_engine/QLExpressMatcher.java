package com.github.krystalics.service.match_engine;

import com.github.krystalics.common.Rule;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QLExpressMatcher {
    static Map<String, List<Rule>> ruleCache = new HashMap<>(); // 规则缓存、按照场景缓存规则
    //存在的问题是规则量太大时无法缓存怎么办、一个数据必须与所有规则100万任务匹配时
    //只保存一个bitmap、遍历bitmap id存在时去redis获取对应的规则？

    static ExpressRunner runner = new ExpressRunner(); // QLExpress 执行器
    static ExecutorService executor = Executors.newFixedThreadPool(8);

    public static List<Result> processBatch(List<Map<String, Object>> batch,String domain) throws Exception {
        //todo 根据domain查询对应的ruleids、PreFilter进行规则过滤

        List<Future<List<Result>>> futures = new ArrayList<>();
        for (Map<String, Object> data : batch) {
            futures.add(executor.submit(() -> matchData(data,ruleCache.get(domain))));
        }

        return futures.stream()
                .flatMap(f -> {
                    try {
                        return f.get().stream();
                    } catch (Exception e) {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
    }

    static List<Result> matchData(Map<String, Object> data,List<Rule> rules) throws Exception {
        List<Result> results = new ArrayList<>();
        long dataId = ((Number) data.get("id")).longValue();
        IExpressContext<String, Object> context = new DefaultContext<>();
        data.forEach(context::put);

        for (Rule rule : rules) {
            try {
                Boolean match = (Boolean) runner.execute(rule.expression, context, null, true, false);
                results.add(new Result(dataId, rule.id, match));
            } catch (Exception e) {
                // 记录错误，默认 false
                results.add(new Result(dataId,rule.id, false));
            }
        }

        return results;
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