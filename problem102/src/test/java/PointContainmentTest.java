import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by adarsh on 02/01/2017.
 */
public class PointContainmentTest {

    @Test
    public void testInput() {
        /**
         * A(-340,495), B(-153,-910), C(835,-947)

         X(-175,41), Y(-421,-714), Z(574,-645)

         */
        Point A = new Point(-340, 495);
        Point B = new Point(-153, -910);
        Point C = new Point(835, -947);

        //From http://stackoverflow.com/questions/13300904/determine-whether-point-lies-inside-triangle

        //If point P is inside triangle formed by P1,P2,P3, then there exists a solution for
        // P = alpha*P1+beta*P2+gamma*P3 such that alpha+beta+gamma=1 and all alpha, beta and gamma >0

        //Next we solve for alpha,beta, gamma and verify inputs

        SplitDTO dto = getSplitForCoordinates(A, B, C);

        Assert.assertTrue(dto.check());

        Point X = new Point(-175, 41);
        Point Y = new Point(-421, -714);
        Point Z = new Point(574, -645);

        dto = getSplitForCoordinates(X,Y,Z);
        Assert.assertFalse(dto.check());
    }

    private SplitDTO getSplitForCoordinates(Point a, Point b, Point c) {
        // We are checking for origin, which makes this very easy
        RealMatrix coefficients = new Array2DRowRealMatrix(new double[][]{{a.x, b.x, c.x}, {a.y, b.y, c.y}, {1, 1, 1}},
                false);
        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
        RealVector constants = new ArrayRealVector(new double[]{0, 0, 1}, false);
        RealVector solution = solver.solve(constants);
        return new SplitDTO(solution.getEntry(0), solution.getEntry(1), solution.getEntry(2));

    }

    @Test
    public void processFileData() throws IOException {
     File inputFile = new File(getClass().getClassLoader().getResource("p102_triangles.txt").getFile());
        Assert.assertNotNull(inputFile);
        int ctr=0;
        List<String> lines = FileUtils.readLines(inputFile);
        for(String line : lines)
        {
            String[] coordinateStrings = line.split(",");
         if(getSplitForCoordinates(
                 new Point(Integer.parseInt(coordinateStrings[0]),Integer.parseInt(coordinateStrings[1])),
                 new Point(Integer.parseInt(coordinateStrings[2]),Integer.parseInt(coordinateStrings[3])),
                 new Point(Integer.parseInt(coordinateStrings[4]),Integer.parseInt(coordinateStrings[5]))
         ).check())
         {
             ctr++;
         }
        }
        System.out.println(ctr); //228
    }

    private static class Point {
        final int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class SplitDTO {
        final double alpha, beta, gamma;

        public SplitDTO(double alpha, double beta, double gamma) {
            this.alpha = alpha;
            this.beta = beta;
            this.gamma = gamma;
        }

        public boolean check() {
            return alpha > 0 && beta > 0 && gamma > 0;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("SplitDTO{");
            sb.append("alpha=").append(alpha);
            sb.append(", beta=").append(beta);
            sb.append(", gamma=").append(gamma);
            sb.append('}');
            return sb.toString();
        }
    }
}
