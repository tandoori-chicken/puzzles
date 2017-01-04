import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by adarsh on 03/01/2017.
 */

//problem at https://projecteuler.net/problem=103
public class SubsetGenerationTest {

    @Test
    public void testSubsetGeneration() {
        List<Integer> parentSet = IntStream.rangeClosed(20, 50).boxed().collect(Collectors.toList());

        int n = 7;

        List<Set<Integer>> subsets = getSubsetsOfSize(parentSet, n);

//        System.out.println(subsets.size());

        int minSum = Integer.MAX_VALUE;
        Set<Integer> chosenSet = null;

        List<Set<Integer>> solutions = subsets.parallelStream().filter(this::checkValidity).collect(Collectors.toList());

        for(Set<Integer> solution : solutions)
        {
            int sum = getSum(solution);
                        if (sum < minSum) {
                minSum = sum;
                chosenSet = solution;
            }
        }

        System.out.println(chosenSet + " " + minSum); //answer is 20,31,38,39,40,42,45 sum 255


    }

    private List<Set<Integer>> getSubsetsOfSize(List<Integer> parentSet, int n) {

        boolean used[] = new boolean[parentSet.size()];
        return getSubsetsOfSize(parentSet, n, 0, 0, used);
    }

    private List<Set<Integer>> getSubsetsOfSize(List<Integer> parentSet, int targetLength, int startIndex, int curLength, boolean[] used) {
        if (curLength == targetLength) {
            Set<Integer> set = new HashSet<>(targetLength);
            IntStream.range(0, parentSet.size()).boxed().filter(i -> used[i]).map(parentSet::get).forEach(set::add);
            return new ArrayList<>(Collections.singleton(set));
        }

        if (startIndex == parentSet.size())
            return null;

        List<Set<Integer>> result = new ArrayList<>();
        used[startIndex] = true;
        List<Set<Integer>> trueResult = getSubsetsOfSize(parentSet, targetLength, startIndex + 1, curLength + 1, used);
        if (trueResult != null) {
            result.addAll(trueResult);
        }

        used[startIndex] = false;
        List<Set<Integer>> falseResult = getSubsetsOfSize(parentSet, targetLength, startIndex + 1, curLength, used);
        if (falseResult != null) {
            result.addAll(falseResult);
        }

        if (trueResult == null && falseResult == null)
            return null;

        return result;

    }

    private boolean checkValidity(Set<Integer> set) {

        List<Set<Integer>> subsets = getNonEmptySubsets(set);
        for (int i = 0; i < subsets.size(); i++) {
            for (int j = i + 1; j < subsets.size(); j++) {

                boolean isValid = checkValidity(subsets.get(i), subsets.get(j));
                if (!isValid)
                    return false;
            }
        }
        return true;

    }

    private List<Set<Integer>> getNonEmptySubsets(Set<Integer> set) {
        Deque<Integer> deque = new LinkedList<>(set);

        List<Deque<Integer>> midResults = getSubsets(deque);

        return midResults.stream().map(d -> new HashSet<>(d)).collect(Collectors.toList());
    }

    private List<Deque<Integer>> getSubsets(Deque<Integer> set) {
        if (set.size() == 0)
            return new ArrayList<>();
        Deque<Integer> set1 = new LinkedList<>(set);
        int last = set1.removeLast();

        List<Deque<Integer>> result = new ArrayList<>();

        List<Deque<Integer>> subsets = getSubsets(set1);
        for (Deque<Integer> subset : subsets) {
            result.addAll(new ArrayList<>(Collections.singleton(subset)));

            Deque<Integer> clone = new LinkedList<>(subset);
            clone.add(last);
            result.addAll(new ArrayList<>(Collections.singleton(clone)));
        }
        result.add(new LinkedList<>(Collections.singleton(last)));

        return result;

    }

    private boolean checkValidity(Collection<Integer> b, Collection<Integer> c) {
        Set<Integer> cloneB = new HashSet<>(b);
        cloneB.retainAll(c);
        if (!cloneB.isEmpty())//if both sets have intersection
        {
            return true;
        }
        int sumB = getSum(b);
        int sumC = getSum(c);
        if (sumB == sumC)
            return false;
        if (b.size() == c.size())
            return true;
        if ((b.size() < c.size() && sumB > sumC) || (b.size() > c.size() && sumB < sumC))
            return false;

        return true;
    }

    private int getSum(Collection<Integer> collection) {
        return collection.stream().reduce((a, b) -> a + b).get();
    }


}
