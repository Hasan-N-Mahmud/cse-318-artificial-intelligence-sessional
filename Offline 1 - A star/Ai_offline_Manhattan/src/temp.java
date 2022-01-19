import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class temp {
    public static void main(String[] args) throws FileNotFoundException {
        int[][] mat=new int[4][4];
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                mat[i][j]=5;
            }
        }
        Node n1=new Node(null,mat,mat,0,0,0,0,0);
        Node n2=new Node(null,mat,mat,0,0,0,0,0);
        if(n1.equals(n2))
            System.out.println("Ok");
        else
            System.out.println("Not ok");
        ArrayList<Node> list=new ArrayList<>();
        list.add(n1);
        if(list.contains(n2))
            System.out.println("done");

    }
}
