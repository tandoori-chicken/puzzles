import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by adarsh on 15/04/2017.
 */
public class ModeratePuzzleTest {
    @Test
    public void testCompareNumbers() {
        int a = -4;
        System.out.println(Integer.toBinaryString(a));
        a >>>= 30;
        System.out.println(Integer.toBinaryString(a));
    }

    /**
     * Given an arithmetic equation consisting of positive integers, +, -, * and / (no paren-
     * theses). compute the result.
     * EXAMPLE
     * Input: 2*3+5/6*3+15 Output: 23.5
     */

    @Test
    public void testEvaluateExpression() {
        String input = "2*3+5/6*3+9+6";
        Double targetOutput = 23.5;
        Double output = evaluateExpression(input);

        org.junit.Assert.assertEquals(targetOutput,output);
    }

    private Double evaluateExpression(String input) {
        Stack<Double> valueStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        for (int i = 0; i < input.length(); ) {
            if (Character.isDigit(input.charAt(i))) {
                Double d =Double.valueOf(input.charAt(i++) + "");
//                System.out.println("Pushing "+d+" into value stack");
                valueStack.push(d);
            } else {
                if (operatorStack.isEmpty() || precedenceMap.get(input.charAt(i)) >= precedenceMap.get(operatorStack.peek())) {
                    char c = input.charAt(i++);
//                    System.out.println("Pushing "+c+" into operator stack");
                    operatorStack.push(c);
                } else {
                    char operator = operatorStack.pop();
                    double secondOperator = valueStack.pop();
                    double firstOperator = valueStack.pop();
                    double result = calculate(firstOperator, operator, secondOperator);
//                    System.out.println("Pushing "+result+" into value stack");
                    valueStack.push(result);
                    char c = input.charAt(i++);
//                    System.out.println("Pushing "+c+" into operator stack");
                    operatorStack.push(c);
                }
            }
        }

        while(!operatorStack.isEmpty())
        {
            char operator = operatorStack.pop();
            double secondOperator = valueStack.pop();
            double firstOperator = valueStack.pop();
            double result = calculate(firstOperator, operator, secondOperator);
            System.out.println("Pushing "+result+" into value stack");
            valueStack.push(result);
        }

        return valueStack.pop();
    }

    private double calculate(double firstOperator, char operator, double secondOperator) {
        System.out.println("Calculating "+firstOperator+operator+secondOperator);
        switch (operator) {
            case '+':
                return firstOperator + secondOperator;
            case '-':
                return firstOperator - secondOperator;
            case '*':
                return firstOperator * secondOperator;
            case '/':
                return firstOperator / secondOperator;
            default:
                throw new IllegalArgumentException("Operator [" + operator + "] cannot be processed");
        }
    }

    private static Map<Character, Integer> precedenceMap = new HashMap<>(4);

    static {
        precedenceMap.put('/', 4);
        precedenceMap.put('*', 3);
        precedenceMap.put('+', 2);
        precedenceMap.put('-', 1);
    }

}
