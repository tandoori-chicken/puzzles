import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by adarsh on 04/01/2017.
 */
// problem at https://projecteuler.net/problem=104
public class FibonacciDigitTest {

    private long[] lastArray = new long[2000000];

    @Test
    public void testInput() {
        long valid2000Last = 516817125l;
        lastArray[0] = 0;
        lastArray[1] = 1;
        Set<Integer> specialLastIndex = new HashSet<>();
        for (int i = 2; i <= 1000000; i++) {
            long sum = lastArray[i - 1] + lastArray[i - 2];
            sum = sum % ((long) Math.pow(10, 9));
            lastArray[i] = sum;
            if (checkLastValidity(sum)) {
                specialLastIndex.add(i);
            }
        }
        Assert.assertEquals(valid2000Last, lastArray[2000]);

        System.out.println("SpecialLastIndex size : " + specialLastIndex.size());
        for(int index : specialLastIndex)
        {
            if(isFirstPanDigital(index))
            {
                System.out.println(lastArray[index]+" "+index); //Answer 352786941 329468
            }
        }

    }


    //http://math.stackexchange.com/questions/355552/computing-first-digits-of-fibonacci-numbers
    private boolean isFirstPanDigital(int index) {
        double log = index * Math.log10(phi1.doubleValue()) - Math.log10(sqrt5.doubleValue());
        log %= 1;
        log += 8;
        double exp = Math.pow(10, log);
        String valueString = (exp + "").replace(".", "").substring(0, 9);
        return !valueString.contains("0")&&Arrays.stream(valueString.split("")).distinct().count() == 9;
    }

    private boolean checkLastValidity(long num) {
        String beforeSplit = num + "";
        return !beforeSplit.contains("0") && Arrays.stream(beforeSplit.split("")).distinct().count() == 9;
    }

    private static BigDecimal phi1, sqrt5;

    static {
        double sqrt5D = Math.sqrt(5);
        phi1 = new BigDecimal((1 + sqrt5D) / 2);
        sqrt5 = new BigDecimal(sqrt5D);
    }

}
