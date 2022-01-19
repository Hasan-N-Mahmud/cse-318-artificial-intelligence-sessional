import java.io.*;
import java.sql.SQLOutput;
import java.time.Duration;
import java.time.Instant;
import java.util.*;



public class Ai_dis {
    public static void main(String[] args) throws IOException {
        Instant start=Instant.now();



        int[][] int_mat=new int[4][4];
        int[][] goal_mat=new int[4][4];
        int i,j,k=1,x = 0,y=0,xplored_node_count=0,n;


        ArrayList<Node>list=new ArrayList<>();
        PriorityQueue<Node> pq=new PriorityQueue<>(5,new The_Comparator());

        File file = new File("src/input.txt");
        Scanner sc = new Scanner(file);
        String[] arg;

        j=0;
        n=Integer.parseInt(sc.nextLine());

        String st=sc.nextLine();
        arg=st.split("\\s+");
        //System.out.println(st);
        k=0;
        for(i=0;i<4;i++)
        {
            for(j=0;j<4;j++)
            { goal_mat[i][j]=Integer.parseInt(arg[k]);
                k++;}
        }

        //Main LOOP
        for(int c=0;c<(n-1);c++) {
            st = sc.nextLine();
            arg = st.split("\\s+");
            //System.out.println(st);
            k = 0;
            for (i = 0; i < 4; i++) {
                for (j = 0; j < 4; j++) {
                    int_mat[i][j] = Integer.parseInt(arg[k]);
                    if (int_mat[i][j] == 0) {
                        x = i;
                        y = j;
                    }
                    k++;
                }
            }


            //System.out.println(st);

            int[] row = {1, -1, 0, 0};
            int[] col = {0, 0, 1, -1};
            Node n1 = new Node(null, int_mat, goal_mat, x, y, x, y, 0);
            pq.clear();
            list.clear();

            Node init = n1;
            //n1.printMatrix();
            //n1.printFinalMatrix();
            //System.out.println(n1.inversionCount());
            if (!n1.isSolvable()) {
                System.out.println("Not solvable..");
                Instant finish = Instant.now();
                long timeElapsed = Duration.between(start, finish).toMillis();
                System.out.println("Elapsed time (in milli Seconds): " + timeElapsed);
                return;
            } else
                System.out.println("Solvable..");

            pq.add(n1);
            list.add(n1);

            xplored_node_count++;
            //n1.printMatrix();
            Node current, New;
            k = 0;
            //int prev_x,prev_y;
            while (!pq.isEmpty()) {
                current = pq.poll();
                // System.out.println("Current cost:"+current.h_cost);
                // current.printMatrix();


                if (current.h_cost == 0) {
                    System.out.println("Solution:");
                    System.out.println("Given matrix:");
                    init.printMatrix();
                    System.out.println("Solution path:");
                    current.printPath();
                    System.out.println("The steps needed is " + current.act_cost);
                    System.out.println("Explored node count: " + xplored_node_count);
                    Instant finish = Instant.now();
                    long timeElapsed = Duration.between(start, finish).toMillis();
                    System.out.println("Elapsed time (in milli Seconds): " + timeElapsed);
                    break;
                }

                for (i = 0; i < 4; i++) {
                    if (((current.x + row[i] < 4) && (current.x + row[i] > -1)) && ((current.y + col[i] < 4) && (current.y + col[i] > -1))) {
                        if ((current.prev_x == -1 * row[i]) && (current.prev_y == -1 * col[i])) {
                            continue;

                        }


                        New = new Node(current, current.mat, goal_mat, current.x, current.y, current.x + row[i], current.y + col[i], current.act_cost + 1);
                        New.prev_x = row[i];
                        New.prev_y = col[i];

                        if (!(list.contains(New))) {
                            pq.add(New);
                            list.add(New);
                        }


                        xplored_node_count++;
                    }
                }


            }
        }

    }

}




