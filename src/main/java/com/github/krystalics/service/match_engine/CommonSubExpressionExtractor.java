package com.github.krystalics.service.match_engine;

import java.util.*;
import java.sql.*;

public class CommonSubExpressionExtractor {
    public static Map<String, Integer> extractCommonSubExpressions(Map<Long, String> ruleCache) throws Exception {
        Map<String, Integer> subExprCounts = new HashMap<>();
        
        // 遍历规则，提取子表达式
        for (Map.Entry<Long, String> entry : ruleCache.entrySet()) {
            List<String> subExprs = ASTExtractor.extractSubExpressions(entry.getValue());
            for (String subExpr : subExprs) {
                String normalized = normalizeSubExpression(subExpr);
                subExprCounts.merge(normalized, 1, Integer::sum);
            }
        }

        // 筛选高频子表达式（阈值：出现 > 100 次）
        Map<String, Integer> commonSubExprs = new HashMap<>();
        subExprCounts.forEach((expr, count) -> {
            if (count > 100) {
                commonSubExprs.put(expr, count);
            }
        });
        return commonSubExprs;
    }

    private static String normalizeSubExpression(String expr) {
        // 示例：归一化 "age > 30" 和 "30 < age"
        if (expr.contains("<")) {
            String[] parts = expr.split("<");
            if (parts.length == 2) {
                return parts[1].trim() + " > " + parts[0].trim();
            }
        }
        return expr.trim();
    }

    public static void main(String[] args) throws Exception {
        // 模拟 MySQL 规则
        Map<Long, String> ruleCache = new HashMap<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db", "user", "pass")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT rule_id, expression FROM rules");
            while (rs.next()) {
                ruleCache.put(rs.getLong("rule_id"), rs.getString("expression"));
            }
        }
        
        Map<String, Integer> commonSubExprs = extractCommonSubExpressions(ruleCache);
        System.out.println("Common Sub-Expressions: " + commonSubExprs);
    }
}