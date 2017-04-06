import org.junit.Assert;
import org.junit.Test;

/**
 * Created by adarsh on 04/04/2017.
 */
public class OOPTest {

    /**
     * Given a R x C grid with obstacles and a robot that can only move right and down,
     * calculate shortest path from top left to bottom right
     */
    @Test
    public void testRobotInGrid() {
        int rows = 5, columns = 5;

        boolean[][] grid = new boolean[rows][columns];
        grid[0][1] = true;
        grid[2][1] = true;
        grid[0][3] = true;
        grid[4][0] = true;
        grid[4][2] = true;
        grid[3][3] = true;
//        printGrid(grid);

        int[][] solution = new int[rows][columns]; //keeps track of min number of ways to reach a given node
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[0].length; j++) {
                solution[i][j] = -1;
            }
        }
        solution[0][0] = 0;

//        printSolution(solution);


        int numWays = solve(grid, rows - 1, columns - 1, solution);
        System.out.println(numWays);

//        printSolution(solution);
    }

    private int solve(boolean[][] grid, int r, int c, int[][] solution) {
        if (r < 0 || c < 0)
            return Integer.MAX_VALUE;
        if (solution[r][c] != -1)
            return solution[r][c];
        if (grid[r][c]) {
            solution[r][c] = Integer.MAX_VALUE;
            return solution[r][c];
        }
        int solutionFromTop = solve(grid, r - 1, c, solution);
        int solutionFromLeft = solve(grid, r, c - 1, solution);
        if (solutionFromLeft == Integer.MAX_VALUE && solutionFromTop == Integer.MAX_VALUE) {
            solution[r][c] = Integer.MAX_VALUE;
            return solution[r][c];
        }
        solution[r][c] = Math.min(solutionFromLeft, solutionFromTop) + 1;
        return solution[r][c];
    }


    private void printGrid(boolean[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                sb.append(grid[i][j] ? 'T' : 'F');
                sb.append("\t");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    private void printSolution(int[][] solution) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[0].length; j++) {
                sb.append((solution[i][j] == Integer.MAX_VALUE) ? "H" : solution[i][j]);
                sb.append("\t");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    /**
     * A child can climb 1, 2 or 3 steps at a time. Calculate number of ways of reaching nth step
     */
    @Test
    public void testNumSteps() {
        /**
         * Solution f(n) = f(n-1)+f(n-2)+f(n-3)
         * f(0)=0
         * f(1)=1
         * f(2)=1+1=2 [2,1->1]
         * f(3)=4 [3,1->2,2->1,1->1->1]
         * f(4)=7 [1->3,  2->2,1->1->2,  3->1,1->2->1,2->1->1,1->1->1->1]
         */

        Assert.assertEquals(7, countNumWays(4));
//        System.out.println(countNumWays(5));
    }

    /**
     * Solution f(n) = f(n-1)+f(n-2)+f(n-3)
     * f(0)=0
     * f(1)=1
     * f(2)=1+1=2 [2,1->1]
     * f(3)=4 [3,1->2,2->1,1->1->1]
     * f(4)=7 [1->3,  2->2,1->1->2,  3->1,1->2->1,2->1->1,1->1->1->1]
     */
    private int countNumWays(int n) {
        int a = 1, b = 2, c = 4;
        int d;
        while (n-- > 3) {
            d = a + b + c;
            a = b;
            b = c;
            c = d;
        }
        return c;
    }


}
