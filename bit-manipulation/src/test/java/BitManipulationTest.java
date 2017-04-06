import org.junit.Assert;
import org.junit.Test;

/**
 * Created by adarsh on 31/03/2017.
 */
public class BitManipulationTest {

    @Test
    public void testFlipBitLongest1s() {
        /**
         * Given a number, we can flip exactly 1 bit from 0 to 1. Which flipped bit gives us longest series of 1s.
         * For example, take 1775 = 11011101111. Flipping one bit gives longest length of 8
         */

        int a = 1775;
        int previousLength = 0, currentLength = 0;
        int maxLength = 1;
        while (a != 0) {
            if ((a & 1) == 1) //current bit is 1
            {
                currentLength++;
            } else //current bit is 0
            {
                if ((a & 2) == 0) //next bit is 0
                {
                    previousLength = 0;
                } else {
                    previousLength = currentLength;
                }
                currentLength = 0;
            }
            maxLength = Math.max(maxLength, previousLength + currentLength + 1);
            a >>>= 1;
        }

        Assert.assertEquals(8, maxLength);

    }

    @Test
    public void testNeighboursWithSame1Count() {
        /**
         * Given a positive number, find next smallest and next largest number with same number of 1s
         */


        for (int input = 1; input < 65536; input++) {
            Assert.assertEquals("Smallest failed for " + input, getNextSmallestBruteForce(input), getNextSmallest(input));
            Assert.assertEquals("Largest failed for " + input, getNextLargestBruteForce(input), getNextLargest(input));
        }


    }

    private int getNextSmallestBruteForce(int a) {
        int ctr = a;
        while (ctr-- > 0) {
            if (Integer.bitCount(ctr) == Integer.bitCount(a))
                return ctr;
        }

        return -1;
    }

    private int getNextLargestBruteForce(int a) {
        int ctr = a;
        while (ctr++ > 0) {
            if (Integer.bitCount(ctr) == Integer.bitCount(a))
                return ctr;
        }

        return -1;
    }

    private int getNextSmallest(int a) {
        /**
         * step1 find the first 1 from right that comes after 0 and unset it
         */
        int temp = a, index = 0;
        boolean got0 = false;
        while (temp != 0) {
            if ((temp & 1) == 0)
                got0 = true;
            if ((temp & 1) == 1 && got0)
                break;
            index++;
            temp >>>= 1;
        }

        if (temp == 0)
            return -1;

        /**
         * step2 separate 1s to right of flipped 1 and append 1 to it.
         */
        int lesserBits = a & ((1 << index) - 1);
        lesserBits = 1 + (lesserBits << 1);

        /**
         * step3 separate significant bits after flipped 1
         */
        int significantBits = a & (-1 << (index + 1));

        /**
         * step4 add significant bits to left shifted lesser bits until the sum becomes more than input
         */
        int offset = 0;
        while ((significantBits + (lesserBits << offset)) < a)
            offset++;
        return significantBits + (lesserBits << (offset - 1));

    }

    private int getNextLargest(int a) {
        /**
         * step1 find the first 0 from right that comes after 1 and set it
         */
        int temp = a, index = 0;
        boolean got1 = false;
        while (temp != 0) {
            if ((temp & 1) == 1)
                got1 = true;
            if ((temp & 1) == 0 && got1)
                break;
            index++;
            temp >>>= 1;
        }


        /**
         * step2 separate 1s to right of flipped 1 and right shift it until its not even; then once to remove 1
         */
        int lesserBits = a & ((1 << index) - 1);
        while ((lesserBits & 1) == 0) {
            lesserBits >>>= 1;
        }
        lesserBits >>>= 1;


        /**
         * step3 separate significant bits after set 0
         */
        int significantBits = (a & (-1 << (index + 1))) + (1 << index);

        /**
         * step4 add significant bits to lesser bits
         */

        return significantBits + lesserBits;

    }

    @Test
    public void testSwapAdjacentBits()
    {
        /**
         * Given an integer, swap adjacent bits
         * 11 01 10 00 -> 11 10 01 00
         */

        Integer evenMask = 0xaaaaaaaa;
        Integer oddMask = 0x55555555;

        int input = Integer.parseInt("11011000",2);
        int expectedOutput = Integer.parseInt("11100100",2);

        int evenBits = input&evenMask;
        int oddBits = input&oddMask;
        int output = (oddBits<<1)|(evenBits>>1);
        Assert.assertEquals(expectedOutput,output);
    }


}
