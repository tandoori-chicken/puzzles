/**
 * Created by adarsh on 15/04/2017.
 */
public class Dictionary {
    private Node root;

    public Dictionary() {
        root = new Node(null,null);
        root.setAsRoot();
    }

    public void insertWord(String word)
    {
        if(word.length()==0)
            return;
        root.insert(word, 0);
    }

    public static int indexOf(Character c)
    {
        if(c.equals('\"'))
            return 0;
        if(c.equals('&'))
            return 1;
        if(c.equals('\''))
            return 2;
        if(c.equals('/'))
            return 3;
        if(Character.isDigit(c))
        {
            return 4+Character.getNumericValue(c);
        }
        return ((int)c)+14-97;
    }

    public int size()
    {
        return root.size();
    }

    public boolean wordExists(String word)
    {
        if(word.length()==0)
            return false;
        return root.wordExists(word,0);
    }
}
