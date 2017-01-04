import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by adarsh on 04/01/2017.
 */

//problem at https://projecteuler.net/problem=105
public class OptimalSubsetTest {

    @Test
    public void testSubsets() throws IOException {
        File testFile = new File(getClass().getClassLoader().getResource("p105_sets.txt").getFile());
        Assert.assertNotNull(testFile);

        List<String> lines = FileUtils.readLines(testFile);
        List<Set<Integer>> sets = new ArrayList<>(lines.size());

//        int i=lines.size();
        for(String line : lines)
        {
//            System.out.println(i--);
            Set<Integer> set = Arrays.stream(line.split(",")).map(Integer::parseInt).collect(Collectors.toSet());
            sets.add(set);
        }
int sum=0;
        for(Set<Integer> set : sets)
        {
//            System.out.println(i--);
            if(!checkValidity(set))
                continue;
          sum+=getSum(set);
        }
        System.out.println(sum);
    }



    //below from problem 103
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
