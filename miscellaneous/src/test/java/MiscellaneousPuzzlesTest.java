import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

/**
 * Created by adarsh on 31/03/2017.
 */
public class MiscellaneousPuzzlesTest {

    @Test
    public void testEggDropProblem() {
        /**
         * 100 floors, 2 eggs. Find minimum number of tries for worst case
         */


        for (int n = 0; n < 5; n++) {
            for (int k = 0; k < 200; k++) {
                cache[n][k] = -1;
                cache[n][0] = 0;
                cache[n][1] = 1;
                cache[1][k] = k;
            }
        }
//        System.out.println(eggDrop(2, 100));
//        System.out.println(eggDrop(3, 100));
        Assert.assertEquals(14, eggDrop(2, 100));
    }

    private static int[][] cache = new int[5][200];

    /* Function to get minimum number of trials needed in worst
  case with n eggs and k floors */
    private int eggDrop(int n, int k) {
        if (cache[n][k] != -1) {
            return cache[n][k];
        }
        // If there are no floors, then no trials needed. OR if there is
        // one floor, one trial needed.
        if (k == 1 || k == 0)
            return k;

        // We need k trials for one egg and k floors
        if (n == 1)
            return k;

        int min = Integer.MAX_VALUE, x, res;

        // Consider all droppings from 1st floor to kth floor and
        // return the minimum of these values plus 1.
        for (x = 1; x <= k; x++) {
            res = Math.max(eggDrop(n - 1, x - 1), eggDrop(n, k - x));
            if (res < min)
                min = res;
        }
        cache[n][k] = min + 1;
        return min + 1;
    }

    /**
     * There are 100 closed lockers in a hallway.
     * A man begins by opening all 100 lockers.
     * Next, he closes every second locker.
     * Then, on his third pass, he toggles every third locker (closes it if it is open or opens it if it is closed).
     * This process continues for 100 passes, such that on each pass i, the man toggles every i th locker.
     * After his 100th pass in the hallway, in which he toggles only locker #100, how many lockers are open?
     */

    @Test
    public void testLockerClosingProblem() {


        boolean[] lockers = new boolean[101];
        for (int i = 1; i <= 100; i++) {
            for (int j = i; j <= 100; j++) {
                if (j % i == 0)
                    lockers[j] = !lockers[j];
            }
        }

        for (int i = 1; i <= 100; i++) {
            if (lockers[i]) {
//                System.out.println(i); //Only perfect Squares stay open because they have odd number of factors :)
            }
        }
    }


    //Tries to generate another configuration by moving topmost disk from tower-i to tower-j if legal
    private List<Stack<Integer>> getNextConfiguration(List<Stack<Integer>> towers, int i, int j) {

        Stack<Integer> sourceStack = towers.get(i);
        Stack<Integer> targetStack = towers.get(j);


        if (sourceStack.isEmpty())
            return null;
        if (targetStack.isEmpty() || targetStack.peek() > sourceStack.peek()) {
            towers = clone(towers);
            sourceStack = towers.get(i);
            targetStack = towers.get(j);
            targetStack.push(sourceStack.pop());
            return towers;
        }
        return null;

    }

    private List<Stack<Integer>> clone(List<Stack<Integer>> towers) {
        List<Stack<Integer>> clone = new ArrayList<>(towers);
        clone.set(0, clone(clone.get(0)));
        clone.set(1, clone(clone.get(1)));
        clone.set(2, clone(clone.get(2)));
        return clone;
    }

    private Stack<Integer> clone(Stack<Integer> stack) {
        Stack<Integer> clone = new Stack<>();
        stack.stream().forEach(clone::push);
        return clone;
    }

    private class HanoiDTO {
        String targetConfiguration;
        boolean solutionFound;
        Set<String> sourceProcessedConfigurations = new HashSet<>();
        Set<String> targetProcessedConfigurations = new HashSet<>();
        int numTowers;
        int ctr;
        Set<String> processedConfigurations = new HashSet<>();

        public HanoiDTO(int numTowers) {
            this.numTowers = numTowers;
        }

        public HanoiDTO(String targetConfiguration, int numTowers) {
            this.targetConfiguration = targetConfiguration;
            this.numTowers = numTowers;
        }
    }


    /**
     * In the classic problem of the Towers of Hanoi, you have 3 towers
     * and N disks of different sizes which can slide onto any tower.
     * The puzzle starts with disks sorted in ascending order of size from top to bottom
     * (ie., each disk sits on top of an even larger one).
     * You have the following constraints:
     * (1) Only one disk can be moved at a time.
     * (2) A disk is slid off the top of one tower onto another tower.
     * (3) A disk cannot be placed on top of a smaller disk.
     * Write a program to move the disks from the first tower to the last using stacks.
     */
    @Test
    public void testHanoiTowers() {


        int numDisk = 3;
        int numTowers = 3;

        for (numDisk = 1; numDisk <= 11; numDisk++) {
            List<Stack<Integer>> sourceTowers = new ArrayList<>();
            List<Stack<Integer>> targetTowers = new ArrayList<>();

            IntStream.range(0, numTowers).boxed().forEach(i -> {
                sourceTowers.add(new Stack<>());
                targetTowers.add(new Stack<>());
            });

            int buff = numDisk;
            while (buff > 0) {
                sourceTowers.get(0).push(buff);
                targetTowers.get(numTowers - 1).push(buff--);
            }

            HanoiDTO dto = new HanoiDTO(numTowers);
            getSolution(sourceTowers, targetTowers, dto);
            assertTrue(dto.solutionFound);
//            System.out.println(numDisk + "\t" + dto.ctr); //ctr keeps track of number of executions. used to estimate O(n)
        }


    }

    private void getSolution2(List<Stack<Integer>> towers, HanoiDTO dto) {
        dto.ctr++;
        dto.processedConfigurations.add(stringify2(towers));
        List<List<Stack<Integer>>> nextConfigurations = getNextConfigurations(towers, dto);

        for (List<Stack<Integer>> nextConfiguration : nextConfigurations) {
            if (stringify2(nextConfiguration).equals(dto.targetConfiguration)) {
                dto.solutionFound = true;
                return;
            }
            getSolution2(nextConfiguration, dto);
            if (dto.solutionFound)
                return;
        }

    }

    private void getSolution(List<Stack<Integer>> sourceTowers, List<Stack<Integer>> targetTowers, HanoiDTO dto) {
//        System.out.println("Source : "+stringify2(sourceTowers));
//        System.out.println("Target : "+stringify2(targetTowers));
        dto.ctr++;
        dto.sourceProcessedConfigurations.add(stringify2(sourceTowers));
        dto.targetProcessedConfigurations.add(stringify2(targetTowers));

        List<List<Stack<Integer>>> nextForSource = getNextConfigurations(sourceTowers, dto, true);
        if (dto.solutionFound)
            return;
        List<List<Stack<Integer>>> nextForTarget = getNextConfigurations(targetTowers, dto, false);
        if (dto.solutionFound)
            return;

        for (List<Stack<Integer>> sourceNext : nextForSource) {
            for (List<Stack<Integer>> targetNext : nextForTarget) {
                if (stringify2(sourceNext).equals(stringify2(targetNext))) {
                    dto.solutionFound = true;
                    return;
                }
                getSolution(sourceNext, targetNext, dto);
                if (dto.solutionFound)
                    return;
            }
        }


    }


    private List<List<Stack<Integer>>> getNextConfigurations(List<Stack<Integer>> towers, HanoiDTO dto) {
        List<List<Stack<Integer>>> listOfTowers = new ArrayList<>();

        for (int i = 0; i < dto.numTowers; i++) {
            for (int j = dto.numTowers - 1; j >= 0; j--) {

                if (i == j)
                    continue;

                List<Stack<Integer>> nextConfiguration = getNextConfiguration(towers, i, j);
                if (nextConfiguration != null && !dto.processedConfigurations.contains(stringify2(nextConfiguration)))
                    listOfTowers.add(nextConfiguration);

            }
        }
        return listOfTowers;
    }

    private List<List<Stack<Integer>>> getNextConfigurations(List<Stack<Integer>> towers, HanoiDTO dto, boolean isSource) {
        List<List<Stack<Integer>>> listOfTowers = new ArrayList<>();

        if (isSource) {
            for (int i = 0; i < dto.numTowers; i++) {
                for (int j = dto.numTowers - 1; j >= 0; j--) {

                    if (i == j)
                        continue;
                    List<Stack<Integer>> nextConfiguration = getNextConfiguration(towers, i, j);
                    if (nextConfiguration != null && !dto.sourceProcessedConfigurations.contains(stringify2(nextConfiguration)))
                        listOfTowers.add(nextConfiguration);
                    if (nextConfiguration != null && dto.targetProcessedConfigurations.contains(stringify2(nextConfiguration))) {
                        dto.solutionFound = true;
                        return listOfTowers;
                    }

                }
            }
        } else {

            for (int i = dto.numTowers - 1; i >= 0; i--) {
                for (int j = 0; j < dto.numTowers; j++) {
                    if (i == j)
                        continue;
                    List<Stack<Integer>> nextConfiguration = getNextConfiguration(towers, i, j);

                    if (nextConfiguration != null && !dto.targetProcessedConfigurations.contains(stringify2(nextConfiguration)))
                        listOfTowers.add(nextConfiguration);
                    if (nextConfiguration != null && dto.sourceProcessedConfigurations.contains(stringify2(nextConfiguration))) {
                        dto.solutionFound = true;
                        return listOfTowers;
                    }

                }
            }
        }
        return listOfTowers;
    }

    private String stringify2(List<Stack<Integer>> towers) {
        StringBuilder sb = new StringBuilder();
        int towerIndex = 0;
        for (Stack<Integer> tower : towers) {
            if (tower.isEmpty()) {
                sb.append("_");
            } else {
                tower.forEach(sb::append);
            }
            if (towerIndex++ != 2) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    @Test
    public void testSubsetGeneration() {
        List<Integer> set = IntStream.rangeClosed(0, 2).boxed().collect(Collectors.toList());
        int subsetSize = (int) Math.pow(2, set.size());
        List<List<Integer>> subsets = new ArrayList<>(subsetSize);

        for (int i = 0; i < subsetSize; i++) {
            List<Integer> configuration = getSetConfiguration(set, i);
            subsets.add(configuration);
//            System.out.println(i+"\t"+stringify(configuration));
        }

        Assert.assertTrue(subsets.get(0).isEmpty());
        Assert.assertTrue(subsets.get(subsetSize - 1).size() == set.size());
        Assert.assertTrue(subsets.get(4).size() == 1 && subsets.get(5).contains(set.get(2)));

    }

    private String stringify(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(sb::append);
        return sb.toString();
    }

    private List<Integer> getSetConfiguration(List<Integer> set, int i) {
        List<Integer> solution = new ArrayList<>();
        int index = 0;
        while (i > 0) {
            if ((i & 1) == 1) {
                solution.add(set.get(index));
            }
            index++;
            i >>= 1;
        }
        return solution;
    }

    @Test
    public void testGeneratePermutationString() {
        /**
         * Generate All permutations of a string with unique characters;
         */

        String input = "1234";
        Set<String> permutations = getPermutations(input);

        Assert.assertEquals(getFactorial(input.length()), permutations.size());

    }

    private Set<String> getPermutations(String input) {
        if (input.length() == 1)
            return Collections.singleton(input);
        Set<String> permutations = new HashSet<>(getFactorial(input.length()));
        char prefix = input.charAt(0);
        String trimmedString = input.substring(1);
        Set<String> subPermutations = getPermutations(trimmedString);
        for (String string : subPermutations) {
            for (int i = 0; i <= string.length(); i++) {
                StringBuilder sb = new StringBuilder(input.length());
                sb.append(string.substring(0, i));
                sb.append(prefix);
                sb.append(string.substring(i, string.length()));
                permutations.add(sb.toString());
            }
        }
        return permutations;
    }

    private int getFactorial(int i) {
        if (i <= 1)
            return 1;
        return i * getFactorial(i - 1);
    }

    @Test
    public void testGetKthPermutationOfString() {
        String input = "1234";
        String kthPermutation = getKthPermutation(input, 11);
        Assert.assertEquals("2431", kthPermutation);
    }

    private String getKthPermutation(String input, int k) {
        StringBuilder sb = new StringBuilder();
        getKthPermutation(input, k, sb);
        return sb.toString();
    }

    private void getKthPermutation(String input, int k, StringBuilder sb) {

        if (input.length() == 1) {
            sb.append(input);
            return;
        }
        int factorial = getFactorial(input.length() - 1);

        char prefix = input.charAt(k / factorial);
        sb.append(prefix);
        getKthPermutation(StringUtils.remove(input, prefix), k % factorial, sb);

    }


    @Test
    public void testGeneratePermutationsWithDuplicateChars() {
        /**
         * Generate All permutations of a string with non-unique characters;
         */

        String input = "1223334";
        Set<String> permutations = getPermutationsDuplicate(input);

        Assert.assertEquals(getFactorial(input.length()) / (getFactorial(2) * getFactorial(3)), permutations.size());
    }

    private Set<String> getPermutationsDuplicate(String input) {
        if (input.length() == 1 || getDistinctCharCount(input) == 1)
            return Collections.singleton(input);
        Set<String> permutations = new HashSet<>(getFactorial(input.length()));
        char prefix = input.charAt(0);
        String trimmedString = input.substring(1);
        Set<String> subPermutations = getPermutationsDuplicate(trimmedString);
        for (String string : subPermutations) {
            for (int i = 0; i <= string.length(); i++) {
                StringBuilder sb = new StringBuilder(input.length());
                sb.append(string.substring(0, i));
                sb.append(prefix);
                sb.append(string.substring(i, string.length()));
                permutations.add(sb.toString());
            }
        }
        return permutations;
    }

    private int getDistinctCharCount(String input) {
        int i = 0;
        for (char c : input.toLowerCase().toCharArray()) {
            int offset = Character.getNumericValue(c) - Character.getNumericValue('a');
            i |= (1 << offset);
        }
        return Integer.bitCount(i);
    }

    @Test
    public void testKthPermutationStringWithDuplicates() {
        String input = "1223";

        Assert.assertEquals(12, getNumPermutations(input));
        Assert.assertEquals(420, getNumPermutations("1223334"));

        Assert.assertEquals("3212", getKthPermutationWithDuplicates(input, 10));
        Assert.assertEquals("2213", getKthPermutationWithDuplicates(input, 5));
        Assert.assertEquals("1223", getKthPermutationWithDuplicates(input, 0));
        Assert.assertEquals("1232", getKthPermutationWithDuplicates(input, 1));

    }

    private List<Integer> getCumulativePermutationList(String input) {
        Map<Character, Integer> countMap = getCountMap(input);
        Set<Character> processedCharacters = new HashSet<>();
        List<Integer> result = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (processedCharacters.contains(c))
                continue;
            processedCharacters.add(c);
            result.add(sum);
            updateMap(countMap, c, -1);
            sum += getNumPermutationsForCountMap(countMap);
            updateMap(countMap, c, 1);
        }
        return result;
    }

    private int getNumPermutations(String input) {
        Map<Character, Integer> countMap = getCountMap(input);
        return getNumPermutationsForCountMap(countMap);
    }

    private int getNumPermutationsForCountMap(Map<Character, Integer> countMap) {
        int sum = countMap.keySet().stream().map(countMap::get).reduce((a, b) -> a + b).get();
        int result = getFactorial(sum);
        for (char c : countMap.keySet()) {
            result /= getFactorial(countMap.get(c));
        }
        return result;
    }

    private Map<Character, Integer> getCountMap(String input) {
        Map<Character, Integer> countMap = new HashMap<>();
        for (char c : input.toLowerCase().toCharArray()) {
            updateMap(countMap, c, 1);
        }
        return countMap;
    }

    private void updateMap(Map<Character, Integer> countMap, char c, int delta) {
        int val = countMap.getOrDefault(c, 0);
        val += delta;
        countMap.put(c, val);

    }

    private String getKthPermutationWithDuplicates(String input, int k) {
        StringBuilder sb = new StringBuilder();
        getKthPermutationWithDuplicates(input, k, sb);
        return sb.toString();
    }

    private void getKthPermutationWithDuplicates(String input, int k, StringBuilder sb) {

        if (input.length() == 1) {
            sb.append(input);
            return;
        }
        List<Integer> cumulativePermutationList = getCumulativePermutationList(input);
        int index = 0;
        for (; index < cumulativePermutationList.size() && cumulativePermutationList.get(index) <= k; index++) {
        }
        index--;
        char prefix = getUniqueKthChar(input, index);
        sb.append(prefix);
        getKthPermutationWithDuplicates(input.replaceFirst(prefix + "", ""), k - cumulativePermutationList.get(index), sb);

    }

    private char getUniqueKthChar(String input, int index) {
        List<Character> list = new ArrayList<>();
        for (char c : input.toLowerCase().toCharArray()) {
            if (!list.contains(c))
                list.add(c);
        }
        return list.get(index);
    }

    /**
     * Print permutations possible with N brackets
     */
    @Test
    public void testBracketPermutations() {

        int n = 3;
        printBracketPermutation(n);

    }

    private void printBracketPermutation(int n) {
        printBracketPermutation(n, 0, "");
    }

    /**
     * @param openStock  how many brackets should I open
     * @param closeStock how many brackets should I close
     * @param s          result tracker
     */
    private void printBracketPermutation(int openStock, int closeStock, String s) {
        if (openStock == 0 && closeStock == 0)
            System.out.println(s);
        if (openStock > 0)
            printBracketPermutation(openStock - 1, closeStock + 1, s + "(");
        if (closeStock > 0)
            printBracketPermutation(openStock, closeStock - 1, s + ")");
    }

    @Test
    public void testPaintFill() {
        int m = 10, n = 10;
        PaintNode[][] canvas = new PaintNode[m][n];

        /**
         * Create grid of 0s and create 2 non-adjacent regions of 1s
         */
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                canvas[i][j] = new PaintNode(0);
                if (j < 3 && i < 4)
                    canvas[i][j].paintIndex = 1;
                if (j > 7 && i > 6)
                    canvas[i][j].paintIndex = 1;
            }
        }

        Assert.assertEquals(1, canvas[0][0].paintIndex);
        Assert.assertEquals(1, canvas[m - 1][n - 1].paintIndex);
        Assert.assertEquals(0, canvas[m / 2][n / 2].paintIndex);

//        printCanvas(canvas);

        fillRegion(canvas, 2, 2, 4); //Fill one region alone

//        printCanvas(canvas);

        Assert.assertEquals(4, canvas[0][0].paintIndex);
        Assert.assertEquals(1, canvas[m - 1][n - 1].paintIndex);
        Assert.assertEquals(0, canvas[m / 2][n / 2].paintIndex);

    }

    private void fillRegion(PaintNode[][] canvas, int i, int j, int newIndex) {
        fillRegion(canvas, i, j, canvas[i][j].paintIndex, newIndex);

    }

    private void fillRegion(PaintNode[][] canvas, int i, int j, int oldIndex, int newIndex) {
        if (i < 0 || i >= canvas.length || j < 0 || j >= canvas[i].length || canvas[i][j].visited || canvas[i][j].paintIndex != oldIndex)
            return;

        canvas[i][j].visited = true;
        canvas[i][j].paintIndex = newIndex;

        fillRegion(canvas, i - 1, j - 1, oldIndex, newIndex);
        fillRegion(canvas, i - 1, j, oldIndex, newIndex);
        fillRegion(canvas, i - 1, j + 1, oldIndex, newIndex);

        fillRegion(canvas, i, j - 1, oldIndex, newIndex);
        fillRegion(canvas, i, j + 1, oldIndex, newIndex);

        fillRegion(canvas, i + 1, j - 1, oldIndex, newIndex);
        fillRegion(canvas, i + 1, j, oldIndex, newIndex);
        fillRegion(canvas, i + 1, j + 1, oldIndex, newIndex);

    }

    private void printCanvas(PaintNode[][] canvas) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                sb.append(canvas[i][j].paintIndex);
                sb.append("\t");
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    private class PaintNode {
        int paintIndex;
        boolean visited = false;

        public PaintNode(int paintIndex) {
            this.paintIndex = paintIndex;
        }
    }

    @Test
    public void testNumberOfWaysToMakeChange() {
        /**
         * Given an infinite number of quarters (25 cents), dimes (10 cents), nickels (5 cents), and
         pennies (1 cent), write code to calculate the number of ways of representing n cents.
         */

        /**
         * If n=98, it's number of ways of reaching 98 without 25 cents
         * + number of ways of reaching 73 without 25 cents
         * + number of ways of reaching 48 without 25 cents
         * + number of ways of reaching 23 without 25 cents
         */

        int n = 98;

        int numWays = getNumWays(n);
        System.out.println(numWays);

        Assert.assertEquals(2, getNumWays(5));
        Assert.assertEquals(4, getNumWays(10));


    }

    private int getNumWays(int n) {
        int[] changeArray = new int[]{1, 5, 10, 25};
        return getNumWays(n, changeArray, changeArray.length - 1);
    }

    private int getNumWays(int n, int[] changeArray, int maxIndex) {
        if (maxIndex == 0)
            return 1;
        if (n < changeArray[maxIndex])
            return getNumWays(n, changeArray, maxIndex - 1);
        int numWays = 0;
        for (int i = 0; i <= n / changeArray[maxIndex]; i++) {
            numWays += getNumWays(n - (i * changeArray[maxIndex]), changeArray, maxIndex - 1);
        }
        return numWays;
    }

    @Test
    public void testSudokuSolver() {
        int[][] sudoku = getTestInput();
        boolean solved = solveSudoku(sudoku);
        Assert.assertTrue(solved);

//        printSudoku(sudoku);
        Assert.assertNull(getNextUnassignedLocation(sudoku));
    }

    private void printSudoku(int[][] sudoku) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(sudoku[i][j]);
                sb.append("\t");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    private boolean solveSudoku(int[][] sudoku) {
        RowColumnDTO dto = getNextUnassignedLocation(sudoku);
        if (dto == null)
            return true;

        for (int i = 1; i <= 9; i++) {
            if (!isValidLocation(i, dto, sudoku))
                continue;
            sudoku[dto.row][dto.col] = i;
            boolean subSolution = solveSudoku(sudoku);
            if (subSolution)
                return true;
            sudoku[dto.row][dto.col] = 0;
        }
        return false;
    }

    /**
     * Checks if the value 'i' is valid at the location specified by the DTO
     */
    private boolean isValidLocation(int i, RowColumnDTO dto, int[][] sudoku) {
        return isRowValid(i, dto, sudoku) && isColumnValid(i, dto, sudoku) && isBoxValid(i, dto, sudoku);
    }

    private boolean isRowValid(int val, RowColumnDTO dto, int[][] sudoku) {
        for (int col = 0; col < 9; col++) {
            if (sudoku[dto.row][col] == val)
                return false;
        }
        return true;
    }

    private boolean isColumnValid(int val, RowColumnDTO dto, int[][] sudoku) {
        for (int row = 0; row < 9; row++) {
            if (sudoku[row][dto.col] == val)
                return false;
        }
        return true;
    }

    private boolean isBoxValid(int val, RowColumnDTO dto, int[][] sudoku) {
        int rowOffset = (dto.row / 3) * 3;
        int colOffset = (dto.col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudoku[i + rowOffset][j + colOffset] == val)
                    return false;
            }
        }
        return true;
    }


    /**
     * Gets location of first '0' entry
     */
    private RowColumnDTO getNextUnassignedLocation(int[][] sudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j] == 0)
                    return new RowColumnDTO(i, j);
            }
        }
        return null;
    }

    private class RowColumnDTO {
        int row, col;

        public RowColumnDTO(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(this.row);
            sb.append(",");
            sb.append(this.col);
            sb.append(")");
            return sb.toString();
        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//
//            if (o == null || getClass() != o.getClass()) return false;
//
//            RowColumnDTO that = (RowColumnDTO) o;
//
//            return new EqualsBuilder()
//                    .append(row, that.row)
//                    .append(col, that.col)
//                    .isEquals();
//        }
//
//        @Override
//        public int hashCode() {
//            return new HashCodeBuilder(17, 37)
//                    .append(row)
//                    .append(col)
//                    .toHashCode();
//        }
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


    /**
     * Write an algorithm to print all ways of arranging eight queens on an 8x8 chess board so that none of them share the same row, column, or diagonal.
     * In this case, "diagonal" means all diagonals, not just the two that bisect the board.
     */
    @Test
    public void testQueenPlacementInChess() {
        char[][] board = getChessBoard();

        board[2][1] = 'Q';
        Set<RowColumnDTO> queenPositions = new HashSet<>(Arrays.asList(
                new RowColumnDTO(2, 1)
        ));

        Assert.assertFalse(isValidQueenPlacement(board, 0, 3, queenPositions));
        Assert.assertFalse(isValidQueenPlacement(board, 3, 0, queenPositions));
        Assert.assertFalse(isValidQueenPlacement(board, 1, 0, queenPositions));
        Assert.assertFalse(isValidQueenPlacement(board, 7, 6, queenPositions));
        Assert.assertFalse(isValidQueenPlacement(board, 2, 7, queenPositions));
        Assert.assertFalse(isValidQueenPlacement(board, 6, 1, queenPositions));
        Assert.assertTrue(isValidQueenPlacement(board, 4, 4, queenPositions));


        board = getChessBoard();
        printQueenConfigurations(board, 0);
        Assert.assertEquals(92,ctr);
    }

    static int ctr=0;
    private void printQueenConfigurations(char[][] board, int minIndex) {
        Set<RowColumnDTO> queenPositions = getQueenPositions(board);
        if (queenPositions.size() == 8) {
//            printBoard(board);
            ctr++;
            return;
        }
        if(minIndex==8)
            return;
//        if (minIndex == 6 && queenPositions.size() < 7)
//            return;

        for (int i = 0; i < 8; i++) {
            char prev = board[i][minIndex];
//            if (isValidQueenPlacement(board, i, minIndex, queenPositions)) {
//                board[i][minIndex] = 'Q';
//                printQueenConfigurations(board, minIndex + 1);
//                board[i][minIndex] = prev;
//            }
            if (isValidQueenPlacement(board, minIndex, i, queenPositions)) {
                prev = board[minIndex][i];
                board[minIndex][i] = 'Q';
                printQueenConfigurations(board, minIndex + 1);
                board[minIndex][i] = prev;
            }
        }
//        printQueenConfigurations(board, minIndex + 1);
    }

    private Set<RowColumnDTO> getQueenPositions(char[][] board) {
        Set<RowColumnDTO> queenPositions = new HashSet<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'Q')
                    queenPositions.add(new RowColumnDTO(i, j));
            }
        }
        return queenPositions;
    }

    private boolean isValidQueenPlacement(char[][] board, RowColumnDTO dto, Set<RowColumnDTO> queenPositions) {
        for (RowColumnDTO queenPosition : queenPositions) {
            if (!isValidPosition(dto, queenPosition))
                return false;
        }
        return true;
    }

    /**
     * returns true if potential position doesn't conflict with given single queen's position
     */
    private boolean isValidPosition(RowColumnDTO potentialPosition, RowColumnDTO queenPosition) {
        if (potentialPosition.row == queenPosition.row || potentialPosition.col == queenPosition.col) //check row, col
            return false;
        if ((potentialPosition.row + potentialPosition.col) == (queenPosition.row + queenPosition.col)) //check diagonal 1
            return false;
        if ((potentialPosition.row - potentialPosition.col) == (queenPosition.row - queenPosition.col)) //check diagonal 2
            return false;
//        System.out.println("Returning True for "+potentialPosition+" and "+queenPosition);
//        RowColumnDTO testDTO = new RowColumnDTO(5,1);
//        RowColumnDTO testDTO2 = new RowColumnDTO(6,4);
//        if(testDTO.equals(queenPosition)&&potentialPosition.equals(testDTO2))
//            System.out.println(potentialPosition);
        return true;
    }

    private boolean isValidQueenPlacement(char[][] board, int row, int col, Set<RowColumnDTO> queenPositions) {
        return isValidQueenPlacement(board, new RowColumnDTO(row, col), queenPositions);
    }


    private void printBoard(char[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                sb.append(board[i][j] == 'Q' ? ""+i+j : '-');
                sb.append("\t");
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    private char[][] getChessBoard() {
        char[][] board = new char[8][8];
        boolean flag = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = flag ? 'W' : 'B';
                flag ^= true;
            }
            flag ^= true;
        }
        return board;
    }

}
