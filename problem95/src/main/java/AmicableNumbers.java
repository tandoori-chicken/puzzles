import java.util.HashSet;
import java.util.Set;

/**
 * Created by adarsh on 30/12/2016.
 */
public class AmicableNumbers {

    //problem at https://projecteuler.net/problem=95

//Answer 14316 discovered from the print statements
    public static void main(String[] args) {
        boolean chainDiscoveredFlag;
        Set<Integer> chain;
        int lim = 1000000;
        Set<Integer> knownAmicables = new HashSet<>();
        int maxChainLength = Integer.MIN_VALUE;
        Set<Integer> result = null;
        int start = -1;
        int hits;
        for (int i = 6; i <= lim; i++) {
            hits=0;
            chainDiscoveredFlag = false;
//            System.out.println("Working for "+i);
            chain = new HashSet<>();
            chain.add(i);
            int next = getNextElement(i);
            while (next != -1 && next <= lim && !knownAmicables.contains(next)&&hits++<100) {
                boolean unique = chain.add(next);
                if (!unique && next == i) {
                    chainDiscoveredFlag = true;
                    break;
                }
                if (!unique && next > i)
                    break;
                next = getNextElement(next);
            }
            if(hits>=99)
                System.out.println("Bug at "+i);
            if (!chainDiscoveredFlag)
                continue;
            System.out.println("For " + i + " chain size " + chain.size());
            knownAmicables.addAll(chain);
            if (chain.size() > maxChainLength) {
                maxChainLength = chain.size();
                result = chain;
                start = i;
                System.out.println(maxChainLength);
//                chain.stream().sorted().forEach(System.out::println);
            }

        }
        System.out.println(start);
    }


    //Used to calculate next element in chain. returns -1 if input is prime
    private static int getNextElement(int start) {
        if (isPrime(start))
            return -1;

        int sum = 0;
        for (int i = 1; i < start; i++) {
            if (start % i == 0) {
                sum += i;
            }
        }
        return sum;
    }

    private static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        long sqrtN = (long) Math.sqrt(n) + 1;
        for (long i = 6L; i <= sqrtN; i += 6) {
            if (n % (i - 1) == 0 || n % (i + 1) == 0) return false;
        }
        return true;
    }

}
