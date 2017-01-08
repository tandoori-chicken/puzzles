import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by adarsh on 08/01/2017.
 */
//problem at https://projecteuler.net/problem=106
public class MetaTestingTest {

    @Test
    public void testMinSets() {
        Set<Integer> set = IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toSet());
        List<Set<Integer>> subsets = getNonEmptySubsets(set);


        MetaSubsetDTO dto = new MetaSubsetDTO();

        processCombinations(subsets, dto);

        System.out.println(dto.needToCompareCount); //Answer 21384
    }

    private void processCombinations(List<Set<Integer>> subsets, MetaSubsetDTO dto) {
        for (int i = 0; i < subsets.size(); i++) {
            for (int j = i + 1; j < subsets.size(); j++) {

                processCombination(subsets.get(i), subsets.get(j), dto);

            }
        }
    }

    private void processCombination(Set<Integer> b, Set<Integer> c, MetaSubsetDTO dto) {
        Set<Integer> cloneB = new HashSet<>(b);
        cloneB.retainAll(c);
        if (!cloneB.isEmpty())//if both sets have intersection
        {
            dto.intersectingSetsCount++;
            return;
        }

        if (b.size() == c.size()) {
            if (b.size() == 1) {
                dto.sameSizeSingletonSetsCount++;
            } else {

                dto.sameSizeNonSingletonSetsCount++;
                processNeedToCheck2(b, c, dto);
            }
        }

        dto.totalSetCount++;
    }

    static int ctr = 0;

    private void processNeedToCheck2(Set<Integer> b, Set<Integer> c, MetaSubsetDTO dto) {
        List<Integer> bList = new ArrayList<>(b);
        List<Integer> cList = new ArrayList<>(c);
        // we are populating the sets from sortedIntStream and comparing such that
        // index of 'c' is greater than index of 'b' in the list of subsets.
        // This gives us a few advantages during comparison
        // bList's first index will always be less than cList's first index
        int bLast = bList.get(b.size()-1);
        int cFirst = cList.get(0);

        if(bLast<cFirst)
            return; //b and c do not intersect, so sum(B) less than sum(C). no need to check;

        boolean lessThanFlag=true;

        for(int i=1;i<bList.size();i++)
        {
            if(bList.get(i)>cList.get(i))
                lessThanFlag=false;
        }
        if(!lessThanFlag) //if lessThanFlag is true this means every element in b has a corresponding element in c that is greater than it.
        {
            dto.needToCompareCount++;
        }

    }


    private List<Set<Integer>> getNonEmptySubsets(Set<Integer> set) {
        Deque<Integer> deque = new LinkedList<>(set);

        List<Deque<Integer>> midResults = getSubsets(deque);

        return midResults.stream().map((Function<Deque<Integer>, HashSet<Integer>>) HashSet::new).collect(Collectors.toList());
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


    private int getSum(Collection<Integer> collection) {
        return collection.stream().reduce((a, b) -> a + b).get();
    }

    static class MetaSubsetDTO {
        int sameSizeSingletonSetsCount;
        int totalSetCount;
        int sameSizeNonSingletonSetsCount;
        int intersectingSetsCount;
        int needToCompareCount;
    }

}
