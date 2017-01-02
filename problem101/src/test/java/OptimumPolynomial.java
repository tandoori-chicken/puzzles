import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by adarsh on 02/01/2017.
 */
public class OptimumPolynomial {


    /**
     * Attempt to solve
     * <p>
     * 2x + 3y - 2z = 1
     * -x + 7y + 6x = -2
     * 4x - 3y - 5z = 1
     */
    @Test
    public void testMatrixSolve() {
        RealMatrix coefficients = new Array2DRowRealMatrix(new double[][]{{2, 3, -2}, {-1, 7, 6}, {4, -3, -5}},
                false);
        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
        RealVector constants = new ArrayRealVector(new double[]{1, -2, 1}, false);
        RealVector solution = solver.solve(constants);
    }

    @Test
    public void testGivenSolution() {
        Assert.assertEquals(58, getFIT(3, new CubingFunction()));

        Assert.assertEquals(1, getFIT(1, new CubingFunction()));

        Assert.assertEquals(15, getFIT(2, new CubingFunction()));
    }


    private long getFIT(int k, CalculatingFunction function) {
/**
 * First we need to solve for OP(k,n)
 * for k=3 and cubing function,
 * a+b+c=1
 * 4a+2b+c=8
 * 9a+3b+c=27
 */
        double[][] doubleMatrix = new double[k][k];
        double[] constants = new double[k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                doubleMatrix[i][j] = Math.pow(i + 1, k - 1 - j);
            }
            constants[i] = function.apply(i + 1);
        }

        RealMatrix coefficients = new Array2DRowRealMatrix(doubleMatrix, false);
        RealVector constantVector = new ArrayRealVector(constants, false);

        RealVector solution = new LUDecomposition(coefficients).getSolver().solve(constantVector); //should give 6,-11,6 for k=3

        return findFIT(solution, k + 1);
    }

    private long findFIT(RealVector solution, int k) {
        int n = solution.getDimension();
        double ans = 0.0;
        for (int i = 0; i < n; i++) {
            ans += Math.round(solution.getEntry(i)) * Math.pow(k, n - 1 - i);
        }
        return (long) ans;
    }

    @Test
    public void testCustomFunction()
    {
        Assert.assertEquals(683,new CustomFunction().apply(2));
        Assert.assertEquals(44287, new CustomFunction().apply(3));
    }

    @Test
    public void testFITForCustomFunction()
    {
        long sum=0;
        for(int k=1;k<=10;k++)
        {
            long res = getFIT(k,new CustomFunction());
//            System.out.println(k+" "+res);
            sum+=res;
        }

        System.out.println(sum); //37076114526
    }

    private interface CalculatingFunction {
        long apply(int n);
    }

    private class CubingFunction implements CalculatingFunction {

        @Override
        public long apply(int n) {
            return n * n * n;
        }
    }

    private class CustomFunction implements CalculatingFunction {

        @Override
        public long apply(int n) {
            double ans=0.0;
            for(int i=0;i<=10;i++)
            {
                double res = Math.pow(n,i);
                if((i&1)==1)
                    res*=-1;
                ans+=res;
            }

            return (long)ans;
        }
    }


}
