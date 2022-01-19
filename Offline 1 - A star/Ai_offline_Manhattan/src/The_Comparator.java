import java.util.Comparator;

class The_Comparator implements Comparator<Node> {


    @Override
    public int compare(Node n1, Node n2) {
        if((n1.h_cost + n1.act_cost) < (n2.h_cost+n2.act_cost))
            return -1;
        else
            return 1;

    }
}