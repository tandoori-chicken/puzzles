package com.geeksforgeeks;

import org.junit.Test;

/**
 * Created by adarsh on 19/12/2016.
 */
public class BracketTest {



    private int[][] trueCount, falseCount, totalCount;
    String input = "T|T&F^T";
    char[] operand, operator;


    @Test
    public void testCombination() {

        char[] inputChar = input.toCharArray();
        operand = new char[(input.length() + 1) / 2];
        operator = new char[input.length() / 2];

        for (int i = 0; i < input.length(); i++) {
            if ((i & 1) == 0) {
                operand[i / 2] = inputChar[i];
            } else {
                operator[i / 2] = inputChar[i];
            }
        }

        trueCount = new int[operand.length][operand.length];
        falseCount = new int[operand.length][operand.length];
        totalCount = new int[operand.length][operand.length];

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
        int trueCount = getTrueCount(0, operand.length - 1);
        System.out.println(trueCount);

    }

    private int getTrueCount(int i, int j) {
        if (trueCount[i][j] != -1) {
            return trueCount[i][j];
        }
        int sum = 0;

        for (int k = i; k < j; k++) {
            if (operator[k] == '|') {
                sum += getTotalCount(i, k) * getTotalCount(k + 1, j) - getFalseCount(i, k) * getFalseCount(k + 1, j);
            }
            if (operator[k] == '&') {
                sum += getTrueCount(i, k) * getTrueCount(k + 1, j);
            }
            if (operator[k] == '^') {
                sum += getTrueCount(i, k) * getFalseCount(k + 1, j) + getFalseCount(i, k) * getTrueCount(k + 1, j);
            }
        }
        return sum;
    }

    private int getFalseCount(int i, int j) {
        if (falseCount[i][j] != -1) {
            return falseCount[i][j];
        }
        int sum = 0;

        for (int k = i; k < j; k++) {
            if (operator[k] == '|') {
                sum += getFalseCount(i, k) * getFalseCount(k + 1, j);

            }
            if (operator[k] == '&') {
                sum += getTotalCount(i, k) * getTotalCount(k + 1, j) - getTrueCount(i, k) * getTrueCount(k + 1, j);
            }
            if (operator[k] == '^') {
                sum += getTrueCount(i, k) * getTrueCount(k + 1, j) + getFalseCount(i, k) * getFalseCount(k + 1, j);
            }
        }
        return sum;
    }

    private int getTotalCount(int i, int j) {
        if (totalCount[i][j] != -1) {
            return totalCount[i][j];
        }

        return getTrueCount(i, j) + getFalseCount(i, j);
    }


    @Test
    public void testEmptyBracket() {
        printEmptyBracket(3, 0, "");
    }

    private void printEmptyBracket(int openStock, int closeStock, String s) {
        if (openStock == 0 && closeStock == 0) {
            System.out.println(s);
            return;
        }
        if (openStock > 0) {
            printEmptyBracket(openStock - 1, closeStock + 1, s + "<");
        }
        if (closeStock > 0) {
            printEmptyBracket(openStock, closeStock - 1, s + ">");
        }
    }
}
