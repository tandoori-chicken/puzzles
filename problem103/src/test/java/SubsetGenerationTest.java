import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by adarsh on 03/01/2017.
 */
public class SubsetGenerationTest {

    @Test
    public void testSubsetGeneration() {
        Deque<Integer> parentSet = new LinkedList<>(Arrays.asList(1, 2, 4));

        List<Deque<Integer>> subsets = getSubsets(parentSet);

        Assert.assertEquals(7, subsets.size());

        Assert.assertFalse(checkValidity(parentSet));

        Assert.assertTrue(checkValidity(new LinkedList<>(Arrays.asList(2, 3, 4))));


    }

    private boolean checkValidity(Deque<Integer> set) {
        List<Deque<Integer>> subsets = getSubsets(set);

        for (int i = 0; i < subsets.size(); i++) {
            for (int j = i + 1; j < subsets.size(); j++) {

                boolean isValid = checkValidity(subsets.get(i), subsets.get(j));
                if (!isValid)
                    return false;
            }
        }
        return true;

    }

    private boolean checkValidity(Deque<Integer> b, Deque<Integer> c) {
        Deque<Integer> cloneB = new LinkedList<>(b);
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

    private int getSum(Deque<Integer> deque) {
        return deque.stream().reduce((a, b) -> a + b).get();
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

}
