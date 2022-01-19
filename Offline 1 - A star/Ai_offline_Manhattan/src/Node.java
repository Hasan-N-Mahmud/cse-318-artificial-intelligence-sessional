import java.util.Arrays;
import java.util.Objects;

import static java.lang.Math.abs;

public class Node {

    Node parent;
    int [][] mat=new int[4][4];
    int [][] final_mat=new int[4][4];
    int x,y,h_cost,act_cost,k=1;
    int temp;
    int prev_x,prev_y;


    public Node(Node parent, int[][] matrix,int[][] goal, int oldx, int oldy,int newX,int newY, int act_cost) {
        this.parent = parent;
        //System.out.println(matrix[0][1]);
        this.x = newX;
        this.y=newY;


        this.act_cost = act_cost;
        k=1;
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++){
                this.mat[i][j]=matrix[i][j];
                //this.final_mat[i][j]=k;
            }
        }
        //final_mat[3][3]=0;
        final_mat=goal;
        temp=this.mat[oldx][oldy];
        this.mat[oldx][oldy]=this.mat[newX][newY];
        this.mat[newX][newY]=temp;

        //this.printMatrix();
        this.h_cost = this.calculateCost();

    }

    void printPath()
    {

        if (parent == null){
            //System.out.println("Intially given matrix is the answer..");
            return;}
        parent.printPath();
        this.printMatrix();

        System.out.println("");
    }
    void printMatrix()
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
                System.out.print(this.mat[i][j]+" ");

            System.out.println("");
        }
    }

    void printFinalMatrix()
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
                System.out.print(this.final_mat[i][j]+" ");

            System.out.println("");
        }
    }

    int calculateCost()
    {
        int num=0;
        int count = 0;
        int [] pos_x = new int [16];
        int [] pos_y = new int[16];
        for(int l=0;l<4;l++)
        {
            for(int m=0;m<4;m++)
            {

                num=final_mat[l][m];
                pos_x[num]=l;
                pos_y[num]=m;

            }

        }
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                num=this.mat[i][j];
                if(num == 0)
                    continue;

                count = count + Abs(pos_x[num]-i) +Abs(pos_y[num]-j);
                //System.out.println("THis :"+num+" "+row+" "+col+" "+i+" "+j+"Count:"+count );
            }}
        return count;
    }
    int Abs(int x)
    {
        if(x<0)
            return -x;
        else
            return x;
    }
    boolean isSolvable()
    {
        int k=this.inversionCount();
        // System.out.println(k);
        //System.out.println(this.x);
        if(this.x % 2 == 1)
        {
            if(k % 2 == 0)
                return true;
        }
        else
        {
            if(k % 2 == 1)
                return true;
        }

        return false;
    }
    int inversionCount()
    {
        int[] inversion=new int[15];
        int k=0,count=0;
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                if(this.mat[i][j] == 0)
                    continue;
                inversion[k] = this.mat[i][j];
                k++;
            }
        }


        for (int i = 0; i < 14; i++)
            for (int j = i + 1; j < 15; j++)
                if (inversion[i] > inversion[j])
                    count++;

        return count;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node n = (Node) o;
        if (n == this) {
            return true;
        }


        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!(this.mat[i][j] == n.mat[i][j])) {
                    //System.out.println(this.mat[i][j] + " " + n.mat[i][j]);
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(parent, x, y, h_cost, act_cost, k, temp, prev_x, prev_y);
        result = 31 * result + Arrays.hashCode(mat);
        result = 31 * result + Arrays.hashCode(final_mat);
        return result;
    }
}
