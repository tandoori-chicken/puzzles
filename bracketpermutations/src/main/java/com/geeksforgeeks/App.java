package com.geeksforgeeks;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Problem statement in http://www.practice.geeksforgeeks.org/problem-page.php?pid=1240
 * Solution using Dynamic Programming
 */
public class App 
{
    public static void main( String[] args )
    {

        Scanner scanner = new Scanner(System.in);
//        System.out.println("Input :");
        int testCaseCount = scanner.nextInt();
        List<String> testInputs = new ArrayList<String>(testCaseCount);

        for(int i=1;i<=testCaseCount;i++)
        {
            int lengthOfInput = scanner.nextInt();
            testInputs.add(scanner.next());
        }
//        System.out.println("Output :");
        testInputs.stream().map(App::getTrueCount).forEach(System.out::println);
    }

    private static int getTrueCount(String input)
    {
        char[] inputChar = input.toCharArray();
        char[]operand = new char[(input.length() + 1) / 2];
        char[]operator = new char[input.length() / 2];

        for (int i = 0; i < input.length(); i++) {
            if ((i & 1) == 0) {
                operand[i / 2] = inputChar[i];
            } else {
                operator[i / 2] = inputChar[i];
            }
        }

        int[][]trueCount = new int[operand.length][operand.length];
        int[][]falseCount = new int[operand.length][operand.length];
        int[][]totalCount = new int[operand.length][operand.length];

        for (int i = 0; i < operand.length; i++) {
            for (int j = 0; j < operand.length; j++) {
                if (i != j) {
                    trueCount[i][j] = -1;
                    falseCount[i][j] = -1;
                    totalCount[i][j] = -1;
                } else {
                    trueCount[i][i] = operand[i] == 'T' ? 1 : 0;
                    falseCount[i][i] = operand[i] == 'F' ? 1 : 0;
                    totalCount[i][i] = 1;
                }

            }
        }
        return getTrueCount(0, operand.length - 1,operator,trueCount,falseCount,totalCount);
    }

    private static int getTrueCount(int i, int j, char[] operator, int[][] trueCount, int[][] falseCount, int[][] totalCount) {
        if (trueCount[i][j] != -1) {
            return trueCount[i][j];
        }
        int sum = 0;

        for (int k = i; k < j; k++) {
            if (operator[k] == '|') {
                sum += getTotalCount(i, k,operator,trueCount,falseCount,totalCount) * getTotalCount(k + 1, j,operator,trueCount,falseCount,totalCount) - getFalseCount(i, k,operator,trueCount,falseCount,totalCount) * getFalseCount(k + 1, j,operator,trueCount,falseCount,totalCount);
            }
            if (operator[k] == '&') {
                sum += getTrueCount(i, k,operator,trueCount,falseCount,totalCount) * getTrueCount(k + 1, j,operator,trueCount,falseCount,totalCount);
            }
            if (operator[k] == '^') {
                sum += getTrueCount(i, k,operator,trueCount,falseCount,totalCount) * getFalseCount(k + 1, j,operator,trueCount,falseCount,totalCount) + getFalseCount(i, k,operator,trueCount,falseCount,totalCount) * getTrueCount(k + 1, j,operator,trueCount,falseCount,totalCount);
            }
        }
        return sum;
    }

    private static int getFalseCount(int i, int j, char[] operator, int[][] trueCount, int[][] falseCount, int[][] totalCount) {
        if (falseCount[i][j] != -1) {
            return falseCount[i][j];
        }
        int sum = 0;

        for (int k = i; k < j; k++) {
            if (operator[k] == '|') {
                sum += getFalseCount(i, k,operator,trueCount,falseCount,totalCount) * getFalseCount(k + 1, j,operator,trueCount,falseCount,totalCount);

            }
            if (operator[k] == '&') {
                sum += getTotalCount(i, k,operator,trueCount,falseCount,totalCount) * getTotalCount(k + 1, j,operator,trueCount,falseCount,totalCount) - getTrueCount(i, k,operator,trueCount,falseCount,totalCount) * getTrueCount(k + 1, j,operator,trueCount,falseCount,totalCount);
            }
            if (operator[k] == '^') {
                sum += getTrueCount(i, k,operator,trueCount,falseCount,totalCount) * getTrueCount(k + 1, j,operator,trueCount,falseCount,totalCount) + getFalseCount(i, k,operator,trueCount,falseCount,totalCount) * getFalseCount(k + 1, j,operator,trueCount,falseCount,totalCount);
            }
        }
        return sum;
    }

    private static int getTotalCount(int i, int j, char[] operator, int[][] trueCount, int[][] falseCount, int[][] totalCount) {
        if (totalCount[i][j] != -1) {
            return totalCount[i][j];
        }

        return getTrueCount(i, j,operator,trueCount,falseCount,totalCount) + getFalseCount(i, j,operator,trueCount,falseCount,totalCount);
    }

}
