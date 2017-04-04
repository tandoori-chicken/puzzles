import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarsh on 04/04/2017.
 */
public class MineSweeper {
    Node[][] field;
    int gridSize;

    public MineSweeper() {
        this.gridSize = 8;
        field = new Node[gridSize][gridSize];
        buildField();
    }

    private void buildField() {
        field[1][3] = new Node(true);
        field[0][4] = new Node(true);
        field[5][6] = new Node(true);
        field[5][5] = new Node(true);
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (field[i][j] == null) {
                    field[i][j] = new Node(countBombNeighbours(i, j));
                }
            }
        }

    }

    private int countBombNeighbours(int i, int j) {
        return (int) getNeighbours(i, j).stream().filter(n -> n.hasBomb).count();

    }

    private List<Node> getNeighbours(int i, int j) {
        List<Node> neighbours = new ArrayList<>();
        for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
                if (k == 0 && l == 0)
                    continue;
                if ((i + k) < 0 || (i + k) >= gridSize || (j + l) < 0 || (j + l) >= gridSize)
                    continue;
                if (field[i + k][j + l] == null)
                    continue;
                neighbours.add(field[i + k][j + l]);
            }
        }
        return neighbours;
    }


    public void printProper()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Node node=field[i][j];
                if(node.exposed)
                {
                    sb.append(node.value);
                }
                else
                {
                    if(node.flag)
                    {
                        sb.append('F');
                    }
                    else
                    {
                        sb.append('X');
                    }
                }
                sb.append("\t");
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }
    public void printTest() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (field[i][j].hasBomb) {
                    sb.append('B');
                } else {
                    sb.append(field[i][j].value);
                }
                sb.append("\t");
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    class Node {
        int value;
        boolean hasBomb;
        boolean exposed;
        boolean flag;

        public Node(boolean hasBomb) {
            this.hasBomb = hasBomb;
        }

        public Node(int value) {
            this.value = value;
        }

        public Node() {
        }
    }
}
