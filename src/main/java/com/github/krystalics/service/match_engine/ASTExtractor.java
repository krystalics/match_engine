package com.github.krystalics.service.match_engine;


import com.ql.util.express.ExpressRunner;
import com.ql.util.express.InstructionSet;

import java.util.ArrayList;
import java.util.List;

public class ASTExtractor {
    static ExpressRunner runner = new ExpressRunner();

    public static List<String> extractSubExpressions(String expression) throws Exception {
        List<String> subExpressions = new ArrayList<>();
        InstructionSet instructionSet = runner.parseInstructionSet(expression);
        traverseInstructions(instructionSet, subExpressions);
        return subExpressions;
    }

    private static void traverseInstructions(InstructionSet instructionSet, List<String> subExpressions) {
        // 简化为递归遍历，实际需解析操作符和操作数
        for (int i = 0; i < instructionSet.getInstructionLength(); i++) {
            String expr = instructionSet.getInstruction(i).toString();
            if (isValidSubExpression(expr)) { // 过滤无关指令
                subExpressions.add(expr);
            }
        }
    }

    private static boolean isValidSubExpression(String expr) {
        // 示例：只保留布尔表达式，如 "age > 30"
        return expr.contains(">") || expr.contains("<") || expr.contains("==") || expr.contains("equals");
    }
}