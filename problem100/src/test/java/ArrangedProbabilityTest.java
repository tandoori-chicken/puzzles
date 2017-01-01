import org.junit.Assert;
import org.junit.Test;

/**
 * Created by adarsh on 01/01/2017.
 */

//problem at https://projecteuler.net/problem=100
public class ArrangedProbabilityTest {


    private static int lim = 20;
    private static long[] a = new long[lim];
    private static long[] b = new long[lim];
    private static long[] h = new long[lim];

    static {
        a[0] = 3;
        a[1] = 10;
        b[0] = 2;
        b[1] = 7;
        h[0] = 3;
        h[1] = 5;
    }


    @Test
    public void testGenerateSolutionBySeries() {
        Assert.assertEquals(99, getA(4));
        Assert.assertEquals(41, getB(3));
        Assert.assertEquals(29, getH(3));

        long lim = 1000000000000l;
//        lim = 1000000000;
        long y = Long.MIN_VALUE;
        int index = 2;
        for (; y < lim; index++) {
            long a = getA(index) * getH(index);
            long b = getB(index) * getH(index);
            long x = a + b;
            y = a + 2 * b;
        }
        printOutput(index);

    }

    private void printOutput(int index) {
        long a = getA(index-1) * getH(index-1);
        long b = getB(index-1) * getH(index-1);
        long x = a + b;
        long y = a + 2 * b;
        System.out.println(x + " " + y);
    }

// had to derive series by hand. phew.
    private long getH(int index) {
        if (h[index] != 0)
            return h[index];
        long temp = getA(index - 1) + getB(index - 1);
        h[index] = temp;
        return temp;
    }

    private long getA(int index) {
        if (a[index] != 0)
            return a[index];
        long temp = getH(index) + ((index & 1) == 1 ? getH(index) : 0);
        a[index] = temp;
        return temp;
    }


    private long getB(int index) {
        if (b[index] != 0)
            return b[index];
        long temp = ((index & 1) == 0 ? getB(index - 1) + getH(index - 1) : getB(index - 1) + getH(index));
        b[index] = temp;
        return temp;
    }

}
