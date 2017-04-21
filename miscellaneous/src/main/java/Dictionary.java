import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by adarsh on 15/04/2017.
 */
public class Dictionary {
    private Node root;

    private static Dictionary INSTANCE;

    private static Set<String> forbiddenWords = new HashSet<>(Arrays.asList("bcdefghjklmnopqrstuvwxyz".split("")));

    public static Dictionary getInstance(String source) throws IOException {
        if (INSTANCE == null) {
            forbiddenWords.addAll(Arrays.asList("ed","li","mh","er","ot","ket","oke","ked","br","brot")); //removing invalid word from wordlist
            LineIterator lineIterator = FileUtils.lineIterator(new File(Dictionary.class.getClassLoader().getResource(source).getFile()));
            INSTANCE = new Dictionary();
            while (lineIterator.hasNext()) {
                String word = lineIterator.next();
                if (!forbiddenWords.contains(word))
                    INSTANCE.insertWord(word);
            }
        }
        return INSTANCE;
    }

    private Dictionary() {
        root = new Node(null, null);
        root.setAsRoot();
    }

    public void insertWord(String word) {
        if (word.length() == 0)
            return;
        root.insert(word, 0);
    }

    public static int indexOf(Character c) {
        if (c.equals('\"'))
            return 0;
        if (c.equals('&'))
            return 1;
        if (c.equals('\''))
            return 2;
        if (c.equals('/'))
            return 3;
        if (Character.isDigit(c)) {
            return 4 + Character.getNumericValue(c);
        }
        return ((int) c) + 14 - 97;
    }

    public int size() {
        return root.size();
    }

    public boolean wordExists(String word) {
        if (word.length() == 0)
            return false;
        return root.wordExists(word, 0);
    }
}
