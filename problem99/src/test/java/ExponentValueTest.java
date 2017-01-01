import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by adarsh on 01/01/2017.
 */
public class ExponentValueTest {

    @Test
    public void testValue() throws IOException {
        double maxValue = Double.MIN_VALUE;
        File testFile = new File(getClass().getClassLoader().getResource("p099_base_exp.txt").getFile());
        List<String> lines = FileUtils.readLines(testFile);
        int index = 0, maxIndex = -1;
        for (String line : lines) {
            double value = getValue(line);
            if (value > maxValue) {
                maxValue = value;
                maxIndex = index;
            }
            index++;
        }
        System.out.println(maxValue + " " + (maxIndex+1));
    }

    private double getValue(String line) {
        String[] split = line.split(",");
        long exp = Long.parseLong(split[1]);
        double logBase = Math.log(Long.parseLong(split[0]));
        return exp * logBase;
    }
}
