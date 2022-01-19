import java.util.Comparator;

public class SortByColor implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        return o1.slot_no - o2.slot_no;
    }
}
