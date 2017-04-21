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

        Dictionary dictionary = buildDictionary();
        List<String> words = FileUtils.readLines(new File(Dictionary.class.getClassLoader().getResource("words.txt").getFile()));
        words.stream().filter(StringUtils::isNotEmpty).forEach(word -> Assert.assertTrue(dictionary.wordExists(word)));

        Assert.assertFalse(words.contains("tre")); //testing if substrings are not treated as real strings
        Assert.assertFalse(dictionary.wordExists("tre"));
    }

    private Dictionary buildDictionary() throws IOException {
        Dictionary dictionary = Dictionary.getInstance("words.txt");
        return dictionary;
    }

    @Test
    public void testBestApproximateSentence() throws IOException {
        String sentence = "jesslookedjustliketimherbrother";
Dictionary dictionary = buildDictionary();
        BuildSentenceDTO result = buildSentence(dictionary, sentence);
        Assert.assertFalse(StringUtils.isBlank(result.result));
        Assert.assertNotEquals(sentence, result.result);
        Assert.assertEquals(0, result.numInvalid);
//        System.out.println(result.numInvalid);
        System.out.println(result.result);
    }

    private BuildSentenceDTO buildSentence(Dictionary dictionary, String sentence) {
        BuildSentenceDTO[] memory = new BuildSentenceDTO[sentence.length()];
        return buildSentence(dictionary, sentence, 0, memory);
    }

    private BuildSentenceDTO buildSentence(Dictionary dictionary, String sentence, int start, BuildSentenceDTO[] memory) {
        if (start >= sentence.length()) {
            return new BuildSentenceDTO(0, "");
        }
        if (memory[start] != null)
            return memory[start];
        String prefix = "";
        int bestNumInvalid = Integer.MAX_VALUE;
        String bestString = sentence;
        int index = start;
        while (index < sentence.length()) {
            prefix += sentence.charAt(index);
            BuildSentenceDTO subResult = buildSentence(dictionary, sentence, index + 1, memory);
            int prefixInvalid = dictionary.wordExists(prefix) ? 0 : prefix.length();
            if (prefixInvalid + subResult.numInvalid < bestNumInvalid) {
                bestNumInvalid = prefixInvalid + subResult.numInvalid;
                bestString = prefix + " " + subResult.result;
                if(bestNumInvalid==0)
                    return new BuildSentenceDTO(bestNumInvalid,bestString);
            }
            index++;
        }

        return new BuildSentenceDTO(bestNumInvalid, bestString);

    }

    private class BuildSentenceDTO {
        int numInvalid;
        String result;

        public BuildSentenceDTO(int numInvalid, String result) {
            this.numInvalid = numInvalid;
            this.result = result;
        }
    }
}
