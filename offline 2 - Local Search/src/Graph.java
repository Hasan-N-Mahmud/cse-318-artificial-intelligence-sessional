import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Graph {

    ArrayList<Node> nodes=new ArrayList<>();
    int num_of_node;
    int[][] mat;
    public Graph(int num_of_node) {
        mat=new int[num_of_node][num_of_node];
        this.num_of_node = num_of_node;
        for(int i=0;i<num_of_node;i++)
            nodes.add(new Node(i+1));
        for(int i=0;i<num_of_node;i++)
        {
            for(int j=0;j<num_of_node;j++)
                mat[i][j]=0;
        }
    }

    public int getNum_of_node() {
        return num_of_node;
    }
    boolean create_edge(int node1,int node2)
    {

        if((mat[node1-1][node2-1] == 1)||(mat[node2-1][node1-1] == 1))
            return false;
        mat[node1-1][node2-1]=1;
        mat[node2-1][node1-1]=1;
        nodes.get(node1-1).increaseDegree();
        nodes.get(node2-1).increaseDegree();
            return true;
    }

    public void print()
    {
        for(int i=0;i<num_of_node;i++)
        {
            System.out.println("node "+ nodes.get(i).id+":" +nodes.get(i).getDegree());
        }
    }

}
