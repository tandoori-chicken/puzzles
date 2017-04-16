import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by adarsh on 15/04/2017.
 */
public class Node {
    private Node parent;
    private Character key;
    private Node[] children;
    private boolean isRoot = false;

    public Node(Node parent, Character key) {
        this.parent = parent;
        this.key = key;
//        if (key != null || this.parent == null) {
        this.children = new Node[41];
//        }
    }

    public void insert(String word, int index) {
        if (index < word.length()) {
            char c = word.charAt(index);
            Node child = childExists(c) ? getChild(c) : addChild(c);
            child.insert(word, index + 1);
        }
    }

    private Node getChild(char c) {
        return children[Dictionary.indexOf(c)];
    }

    private boolean childExists(char c) {
        return children[Dictionary.indexOf(c)] != null;
    }

    public boolean isLeaf() {
        return parent != null && key == null;
    }


    public void setAsRoot() {
        this.isRoot = true;
    }

    private Node addChild(char c) {

        int childIndex = Dictionary.indexOf(c);
        Node child = new Node(this, c);
        this.children[childIndex] = child;
        return child;
    }

    public int size() {
        int offset = isRoot ? 0 : 1;
        return offset + Arrays.stream(children).filter(n -> n != null).map(Node::size).collect(Collectors.summingInt(i -> i));
    }

    public boolean wordExists(String word, int index) {
        if(index<word.length()) {
            char c = word.charAt(index);
            if(!childExists(c))
                return false;
            return getChild(c).wordExists(word,index+1);
        }
        return true;
    }
}
