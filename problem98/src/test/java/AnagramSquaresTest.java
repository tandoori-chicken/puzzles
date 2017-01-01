import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by adarsh on 30/12/2016.
 */

//problem at https://projecteuler.net/problem=98 answer 18769
public class AnagramSquaresTest {

    private Map<Long, List<String>> anagramMapString = new HashMap<>();
    private Map<Long, List<Long>> anagramMapSquare = new HashMap<>();

    @Test
    public void testFileParse() throws Exception {
        File wordFile = new File(getClass().getClassLoader().getResource("p098_words.txt").toURI());
        Assert.assertNotNull(wordFile);
        List<String> words = parseWordsFromFile(wordFile);
        Assert.assertEquals(1786, words.size());
        Assert.assertEquals("A", words.get(0));
        Assert.assertEquals("YOUTH", words.get(1785));
    }

    private List<String> parseWordsFromFile(File file) throws IOException {
        return Arrays.stream(FileUtils.readFileToString(file).split(",")).map(s -> s.replaceAll("^\"|\"$", "")).collect(Collectors.toList());
    }

    private List<String> parseWordsFromFile() throws Exception {
        File wordFile = new File(getClass().getClassLoader().getResource("p098_words.txt").toURI());
        return parseWordsFromFile(wordFile);
    }

    @Test
    public void collectAnagramsString() throws Exception {
        Assert.assertTrue(testAnagram("CARE", "RACE"));
        Assert.assertFalse(testAnagram("CCRE", "CRRE"));

        Assert.assertEquals(getUniqueIdentifierForString("ABC"), getUniqueIdentifierForString("CBA"));
        int maxLength = Integer.MIN_VALUE;
        for (String word : parseWordsFromFile()) {
            long uniqueIdentifier = getUniqueIdentifierForString(word);
            if (anagramMapString.containsKey(uniqueIdentifier)) {
                List<String> anagramSet = anagramMapString.get(uniqueIdentifier);
                anagramSet.add(word);
                if (word.length() > maxLength) {
                    maxLength = word.length(); //maxLength is 9 for REDUCTION INTRODUCE
                }
            } else {
                anagramMapString.put(uniqueIdentifier, new ArrayList<>(Collections.singleton(word)));
            }
        }

    }

    @Test
    public void collectAnagramsSquares() throws Exception {
        for (long i = 4; i <= Math.sqrt(999999999) + 1; i++) {
            long square = i * i;
            long uniqueIdentifier = getUniqueIdentifierForNumber(square);
            if (anagramMapSquare.containsKey(uniqueIdentifier)) {
                List<Long> anagramSet = anagramMapSquare.get(uniqueIdentifier);
                anagramSet.add(square);
//                System.out.println("Found match");
//                anagramSet.stream().forEach(System.out::println);
            } else {
                anagramMapSquare.put(uniqueIdentifier, new ArrayList<>(Collections.singleton(square)));
            }
        }



    }

    @Test
    public void testMatch()
    {
        Map<Long, String> result = isMatch(Arrays.asList(1296l, 9216l, 2916l), Arrays.asList("CARE", "RACE"));
        Assert.assertNotNull(result);
        result = isMatch(Arrays.asList(1764l,4761l),Arrays.asList("CARE","RACE"));
        Assert.assertNull(result);
    }

    @Test
    public void testMatchStringSquare() throws Exception {
        for (long i = 4; i <= Math.sqrt(999999999) + 1; i++) {
            long square = i * i;
            long uniqueIdentifier = getUniqueIdentifierForNumber(square);
            if (anagramMapSquare.containsKey(uniqueIdentifier)) {
                List<Long> anagramSet = anagramMapSquare.get(uniqueIdentifier);
                anagramSet.add(square);
//                System.out.println("Found match");
//                anagramSet.stream().forEach(System.out::println);
            } else {
                anagramMapSquare.put(uniqueIdentifier, new ArrayList<>(Collections.singleton(square)));
            }
        }

        for (String word : parseWordsFromFile()) {
            long uniqueIdentifier = getUniqueIdentifierForString(word);
            if (anagramMapString.containsKey(uniqueIdentifier)) {
                List<String> anagramSet = anagramMapString.get(uniqueIdentifier);
                anagramSet.add(word);
            } else {
                anagramMapString.put(uniqueIdentifier, new ArrayList<>(Collections.singleton(word)));
            }
        }

        anagramMapString = trim(anagramMapString);

        anagramMapSquare = trim(anagramMapSquare);

        Set<List<String>> anagramStringSets = new HashSet<>();
        anagramMapString.keySet().stream().map(anagramMapString::get).forEach(anagramStringSets::add);

        Set<List<Long>> anagramSquareSets = new HashSet<>();
        anagramMapSquare.keySet().stream().map(anagramMapSquare::get).forEach(anagramSquareSets::add);

        for(List<String> strings : anagramStringSets)
        {
            for(List<Long> squares : anagramSquareSets)
            {
                Map<Long,String> result = isMatch(squares,strings);
                if(result!=null)
                {
                    System.out.println(result);
                }
            }
        }

    }

    private <T> Map<Long, List<T>> trim(Map<Long, List<T>> anagramMapString) {
        Map<Long,List<T>> small = new HashMap<>();
        anagramMapString.keySet().stream().filter(key -> anagramMapString.get(key).size() > 1).forEach(key -> {
            small.put(key, anagramMapString.get(key));
        });

        return small;
    }

    private long getUniqueIdentifierForNumber(long num) {
        String s = num + "";
        long product = 1;
        for (char c : s.toCharArray()) {
            product *= getPrimeForNumericChar(c);
        }

        return product;
    }

    /**
     * Returns if pairing can be done between the list of squares and list of strings. Returns Map of 1296->CARE and 9216->RACE
     * Returns null if pairing is not possible
     */
    private Map<Long, String> isMatch(List<Long> squares, List<String> strings) {
        for (int i = 0; i < squares.size(); i++) {
            for (int j = 0; j < strings.size(); j++) {
                if ((squares.get(i) + "").length() != strings.get(j).length())
                    return null;
                for (int k = 0; k < squares.size(); k++) {
                    if (i == k)
                        continue;
                    String potentialString = getStringForConfiguration(squares.get(i), strings.get(j), squares.get(k));
                    if (strings.contains(potentialString)) {
                        Map<Long, String> map = new HashMap<>(2);
                        map.put(squares.get(i), strings.get(j));
                        map.put(squares.get(k), potentialString);
                        return map;
                    }
                }
            }
        }
        return null;
    }

    private String getStringForConfiguration(long l1, String s, long l2) {
        Map<Character, Character> map = new HashMap<>(s.length());
        String longString = l1 + "";
        for (int i = 0; i < s.length(); i++) {
            map.put(longString.charAt(i), s.charAt(i));
        }
        StringBuilder sb = new StringBuilder();
        longString = l2 + "";
        for (char c : longString.toCharArray()) {
            sb.append(map.get(c));
        }
        return sb.toString();
    }


    private int getPrimeForAlphabetChar(char c) {
        return primes[c - 65];
    }

    private int getPrimeForNumericChar(char c) {
        return primes[c - 48];
    }

    private long getUniqueIdentifierForString(String s) {

        long product = 1;
        for (char c : s.toCharArray()) {
            product *= getPrimeForAlphabetChar(c);
        }

        return product;
    }

    private static final int[] primes = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101};


    @Deprecated
    private boolean testAnagram(String a, String b) {
        char[] aArr = a.toCharArray(), bArr = b.toCharArray();
        if (aArr.length != bArr.length)
            return false;
        if (a.equals(b))
            return false;
        int[] counts = new int[26]; // An array to hold the number of occurrences of each character
        for (int i = 0; i < aArr.length; i++) {
            counts[aArr[i] - 65]++;  // Increment the count of the character at i
            counts[bArr[i] - 65]--;  // Decrement the count of the character at i
        }
        // If the strings are anagrams, the counts array will be full of zeros
        for (int i = 0; i < 26; i++)
            if (counts[i] != 0)
                return false;
        return true;
    }
}
