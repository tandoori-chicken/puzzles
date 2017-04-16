import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by adarsh on 15/04/2017.
 */
public class StringPuzzleTests {
    @Test
    public void testBuildDictionary() throws IOException {
        List<String> words = FileUtils.readLines(new File(getClass().getClassLoader().getResource("words.txt").getFile()));


        Dictionary dictionary = new Dictionary();
        words.forEach(dictionary::insertWord);
        words.stream().filter(StringUtils::isNotEmpty).forEach(word->Assert.assertTrue(dictionary.wordExists(word)));
    }
}
