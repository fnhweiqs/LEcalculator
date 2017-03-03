import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* 用于逻辑判断，针对文本中不同的name对应的boolean型值的组合生成最后的判断值，这个值决定，此文本是否中该规则的标
* modify by weiqs 2017-03-03
* */

public class LECalculator {

    private static final Pattern p = Pattern.compile("[\\da-zA-Z]+|[\\(\\)!]");
    private static final Map<String, Integer> operatorMap = new HashMap<>();
    private static final String error = "illegal logical expression : " ;

    static {
        operatorMap.put("(", 0);
        operatorMap.put(")", 0);
        operatorMap.put("!", 1);
        operatorMap.put("AND", 2);
        operatorMap.put("OR", 2);
    }

    public static boolean calLogicExp(String rule, Map<String, Boolean> map) throws IlegalLEException {
        List<String> exp = splits(rule);
        Stack<String> operator = new Stack<String>();
        Stack<Boolean> operand = new Stack<Boolean>();
        int i = 0;
        while (i < exp.size() || !operator.empty()) {
            if (i < exp.size()) {
                String s = exp.get(i++);
                if (operatorMap.containsKey(s)) {
                    if (")".equals(s)) {
                        while (!operator.empty() && !"(".equals(operator.peek())) {
                            cal(operator, operand);
                        }
                        if (operator.empty() || !"(".equals(operator.pop())) {
                            throw new IlegalLEException( error + rule);
                        }
                    } else {
                        operator.push(s);
                    }
                } else {
                    operand.push(map.getOrDefault(s, false));
                }

            } else {
                cal(operator, operand);
            }
        }
        boolean res ;
        if (operand.size() == 1) {
            res = operand.pop() ;
        } else {
            throw new IlegalLEException(error + rule + " operand number is error!");
        }
        return res ;
    }

    /**
     * 运算优先级判断。
     * @param operator
     * @param operand
     * @throws IlegalLEException
     */
    private static void cal(Stack<String> operator, Stack<Boolean> operand) throws IlegalLEException {
        String current_operator = operator.pop();
        if ("!".equals(current_operator) || operator.empty() || !"!".equals(operator.peek())) {
            boolean res = cal(current_operator, operand);
            operand.push(res);
        } else {
            String prior_operator = operator.pop();
            boolean temp_operand = operand.pop();
            boolean res = cal(prior_operator, operand);
            operand.push(res);
            operand.push(temp_operand);
            operator.push(current_operator);

        }
    }

    /**
     * 运算符计算结果。
     * @param current_operator
     * @param operand
     * @return boolean
     * @throws IlegalLEException
     */

    private static boolean cal(String current_operator, Stack<Boolean> operand) throws IlegalLEException {
        int operandNum = operatorMap.get(current_operator);
        if (operandNum > operand.size()) {
            throw new IlegalLEException(error + current_operator);
        }
        boolean operand1, operand2;
        switch (current_operator) {
            case "!":
                return !operand.pop();
            case "AND":
                operand1 = operand.pop();
                operand2 = operand.pop();
                return operand1 && operand2;
            case "OR":
                operand1 = operand.pop();
                operand2 = operand.pop();
                return operand1 || operand2;
            default:
                return true;
        }
    }

    private static List<String> splits(String rule) {
        List<String> result = new ArrayList<String>();
        Matcher m = p.matcher(rule);
        while (m.find()) {
            result.add(m.group());
        }
        return result;
    }

    static class IlegalLEException extends Exception {
        public IlegalLEException(String s) {
            super(s);
        }
    }

    public static void main(String[] args) {

    }
}
