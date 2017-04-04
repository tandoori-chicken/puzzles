import org.junit.Test;

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
        System.out.println(eggDrop(2, 100));
        System.out.println(eggDrop(3,100));
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

    @Test
    public void testLockerClosingProblem() {
        /**
         * There are 100 closed lockers in a hallway.
         * A man begins by opening all 100 lockers.
         * Next, he closes every second locker.
         * Then, on his third pass, he toggles every third locker (closes it if it is open or opens it if it is closed).
         * This process continues for 100 passes, such that on each pass i, the man toggles every i th locker.
         * After his 100th pass in the hallway, in which he toggles only locker #100, how many lockers are open?
         */

        boolean[] lockers = new boolean[101];
        for (int i = 1; i <= 100; i++) {
            for (int j = i; j <= 100; j++) {
                if (j % i == 0)
                    lockers[j] = !lockers[j];
            }
        }

        for (int i = 1; i <= 100; i++) {
            if (lockers[i])
                System.out.println(i);
        }
    }
}
