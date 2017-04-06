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
        Assert.assertEquals(14,eggDrop(2,100));
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


}
