import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarsh on 30/12/2016.
 */

//problem at https://projecteuler.net/problem=96
public class SudokuSolver {

    private static final int UNASSIGNED = 0;

    @Test
    public void testSingleSudokuSolver() {
        int[][] input = getTestInput();

        Assert.assertTrue(isSafeRow(input, 0, 4));
        Assert.assertTrue(isSafeRow(input, 0, 8));
        Assert.assertFalse(isSafeRow(input, 0, 3));

        Assert.assertTrue(isSafeCol(input, 6, 1));
        Assert.assertFalse(isSafeCol(input, 6, 6));

        Assert.assertTrue(isSafeBox(input, new RowColumnDTO(0, 6), 2));
        Assert.assertFalse(isSafeBox(input, new RowColumnDTO(0, 6), 6));

        Assert.assertTrue(isSafe(input, new RowColumnDTO(3, 4), 5));
        Assert.assertFalse(isSafe(input, new RowColumnDTO(3, 4), 1));

        Assert.assertTrue(solveSudoku(input));

        Assert.assertEquals(4, input[0][0]);
        Assert.assertEquals(6, input[4][4]);
        Assert.assertEquals(2, input[8][8]);
        Assert.assertEquals(3, input[0][2]);

    }

    @Test
    public void testMultipleSudokuSolver() throws URISyntaxException, IOException {
        File sudokuFile = new File(getClass().getClassLoader().getResource("p096_sudoku.txt").toURI());
        List<String> lines = FileUtils.readLines(sudokuFile);
        List<int[][]> sudokuList = parseLines(lines);
        int sum=0;

        for(int[][] sudokuGrid : sudokuList)
        {
            boolean solved = solveSudoku(sudokuGrid);
            if(!solved)
                throw new IllegalArgumentException("Unsolvable sudoku");

            sum+=sudokuGrid[0][0]*100+sudokuGrid[0][1]*10+sudokuGrid[0][2];
        }

        System.out.println(sum);
    }

    private List<int[][]> parseLines(List<String> lines) {
        List<int[][]> bigList = new ArrayList<>(50);
        for (int sudokuIndex = 0; sudokuIndex < 50; sudokuIndex++) {
            int[][] sudokuGrid = convertToGrid(lines, sudokuIndex);
            bigList.add(sudokuGrid);
        }
        return bigList;
    }

    private int[][] convertToGrid(List<String> lines, int index) {
        //sudoku is between index*10+1 and index*10+9
        int[][] sudokuGrid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuGrid[i][j] = convertCharToInt(lines.get(index*10 + 1 + i).charAt(j));
            }
        }
        return sudokuGrid;
    }

    private int convertCharToInt(char c) {
        return Character.getNumericValue(c);
    }

    private void print(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                System.out.print(grid[row][col] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Solves with backtracking. returns false if unsolvable
     */
    private boolean solveSudoku(int[][] grid) {

        RowColumnDTO dto = new RowColumnDTO(null, null);

        if (!findUnassignedLocation(grid, dto)) {
            return true;
        }

        for (int num = 1; num <= 9; num++) {
            if (isSafe(grid, dto, num)) {
                grid[dto.row][dto.col] = num;
                if (solveSudoku(grid))
                    return true;
                grid[dto.row][dto.col] = UNASSIGNED;
            }
        }
        return false;
    }

    private boolean isSafe(int[][] grid, RowColumnDTO dto, int num) {
        int row = 3 * (dto.row / 3);
        int col = 3 * (dto.col / 3);
        return isSafeRow(grid, dto.row, num) && isSafeCol(grid, dto.col, num) && isSafeBox(grid, new RowColumnDTO(row, col), num);
    }

    private boolean isSafeBox(int[][] grid, RowColumnDTO dto, int num) {
//        System.out.println("Checking box for dto " + dto);
        //assume dto contains start row and start col
        for (int row = dto.row; row < dto.row + 3; row++) {
            for (int col = dto.col; col < dto.col + 3; col++) {
                if (grid[row][col] == num)
                    return false;
            }
        }
        return true;
    }

    private boolean isSafeRow(int[][] grid, int row, int num) {
        for (int col = 0; col < 9; col++) {
            if (grid[row][col] == num)
                return false;
        }
        return true;
    }

    private boolean isSafeCol(int[][] grid, int col, int num) {
        for (int row = 0; row < 9; row++) {
            if (grid[row][col] == num)
                return false;
        }
        return true;
    }

    private boolean findUnassignedLocation(int[][] grid, RowColumnDTO dto) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == UNASSIGNED) {
                    dto.row = row;
                    dto.col = col;
                    return true;
                }
            }
        }
        return false;
    }

    private int[][] getTestInput() {

        return new int[][]{
                {0, 0, 3, 0, 2, 0, 6, 0, 0},
                {9, 0, 0, 3, 0, 5, 0, 0, 1},
                {0, 0, 1, 8, 0, 6, 4, 0, 0},
                {0, 0, 8, 1, 0, 2, 9, 0, 0},
                {7, 0, 0, 0, 0, 0, 0, 0, 8},
                {0, 0, 6, 7, 0, 8, 2, 0, 0},
                {0, 0, 2, 6, 0, 9, 5, 0, 0},
                {8, 0, 0, 2, 0, 3, 0, 0, 9},
                {0, 0, 5, 0, 1, 0, 3, 0, 0}
        };
    }
}
