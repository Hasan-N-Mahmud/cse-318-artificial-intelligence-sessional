import java.util.Objects;

public class Node {
    int id;
    int degree;

    int slot_no;
    public Node() {
        this.degree=0;

        this.slot_no=-1;
    }

    public Node(int id) {
        this.id = id;
        this.degree=0;
        this.slot_no=-1;
    }

    public void increaseDegree()
    {
        this.degree++;
    }

    public int getDegree() {
        return degree;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
