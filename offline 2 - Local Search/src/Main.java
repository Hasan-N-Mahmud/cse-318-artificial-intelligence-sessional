import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    //int[][] courses=new int[1000][1];

    public static void main(String[] args) throws IOException {

        File output = new File("output.txt");
        FileWriter fw = new FileWriter(output);
        int timeslot = 0;
        if (!output.exists()) {
            output.createNewFile();
        }

        String p=System.getProperty("user.dir");
        p=p+"\\input";
        File f=new File(p);
        File[] input_crs=f.listFiles((dir, name) -> name.toLowerCase().endsWith(".crs"));
        File[] input_std=f.listFiles((dir, name) -> name.toLowerCase().endsWith(".stu"));


        int i, j, k;
        int kempe_loop=1200;

        for (int x = 0; x < input_crs.length; x++) {
            String[] name = input_crs[x].getName().split("\\.");
            fw.write(name[0]+" File:\n");
            fw.flush();

            Scanner sc = new Scanner(input_crs[x]);
            Path path = Paths.get(input_crs[x].getPath());
            int lineCount = (int) Files.lines(path).count();  //linecount = number of courses
           // System.out.println(lineCount);
            int[] courses = new int[lineCount];
            String st;
            String[] arr;
            i = 0;
            while (sc.hasNextLine()) {
                st = sc.nextLine();
                if (st == "")
                    break;
                arr = st.split(" ");
                courses[i] = Integer.parseInt(arr[1]);
                // System.out.println(courses[i]);
                i++;
            }

            sc = new Scanner(input_std[x]);
            path = Paths.get(input_std[x].getPath());
            int lineCount2 = (int) Files.lines(path).count();   // linecount2 == number of students
            //System.out.println("std:"+lineCount2);
            i = 0;
            int[][] students = new int[lineCount2][lineCount + 1];
            while (sc.hasNextLine()) {
                st = sc.nextLine();
                arr = st.split(" ");
                students[i][0] = arr.length;
                // System.out.println("id:"+(i+1)+"  "+students[i][0]);
                for (j = 0; j < arr.length; j++) {
                    if (arr[j] == "")
                        break;
                    // System.out.println(Integer.parseInt(arr[j]));
                    students[i][j + 1] = Integer.parseInt(arr[j]);
                }
                i++;
            }

            fw.write("Total courses: "+lineCount+" \n");
            fw.write("Total students: "+lineCount2+"\n");
            fw.flush();
//-------------------------------Heurisitics---------------------------------
            Graph g = new Graph(lineCount);

            // Creating Graph
            for (i = 0; i < lineCount2; i++) {
                // System.out.println("\nNode: "+(i+1));
                for (j = 1; j <= students[i][0]; j++) {
                    //System.out.println(" ");
                    for (k = j + 1; k <= students[i][0]; k++) {
                        //System.out.print(students[i][j]+" "+students[i][k]+"\t");
                        g.create_edge(students[i][j], students[i][k]);

                    }
                }
            }




            //         Graph Created



//-------------------------------------DSatur Algorithm--------------------------------------------------

            int length =g.num_of_node;
            int count=0;
            boolean[] Colored=new boolean[length];
            int[] id_color = new int[length];
            int[] color_count = new int[length];

            int maxDegree=0;
            int vertexWithMaxdegree=0;
            for(i=0;i<length;i++)
            {
                color_count[i] = -1;
                id_color[i] = -1;
                Colored[i]=false;
                //System.out.println(i+" "+g.nodes.get(i).degree);
                if(g.nodes.get(i).degree > maxDegree) {
                    vertexWithMaxdegree = i;
                    maxDegree = g.nodes.get(i).degree;
                }
            }
            Node n = g.nodes.get(vertexWithMaxdegree);
            i = vertexWithMaxdegree;
            //System.out.println(vertexWithMaxdegree);
            int in;
            n.slot_no = 0;

            id_color[i] = n.slot_no;
            color_count[n.slot_no] = 1;
            Colored[i]=true;
            count++;
            //System.out.println("Node : "+(i+1)+"  Color : "+(id_color[i]+1));
            while (count < length) {


                    n = Main.getHighestSaturation(g, id_color);
                    i = n.id - 1;
                    boolean[] available_color = new boolean[length];

                    for (j = 0; j < length; j++)
                        available_color[j] = true;

                    for (j = 0; j < length; j++) {

                        if (Colored[j] && g.mat[j][i] == 1 ) {
                            available_color[id_color[j]] = false;
                        }
                    }

                    for (j = 0; j < length; j++) {
                        if (available_color[j]) {
                            n.slot_no = j;
                            break;

                        }
                    }

                id_color[i] = n.slot_no;
                color_count[n.slot_no] = 1;
                Colored[i]=true;
                count++;
                }

                int total_used_color = 0;

                for (i = 0; i < length; i++) {
                    //System.out.println(color_count[i]);
                    if (color_count[i] == 1)
                        total_used_color++;
                }

                //System.out.println("File "+name[0] + ":" + total_used_color);

                fw.write("Degree of Saturation: "+total_used_color+"\n" );
                fw.flush();

                //  ---------------------         Penalty Calculation       ---------------------
           double penalty;
           penalty = consecutive_penalty(g,lineCount2,students);
            fw.write("Penalty (Consecutive pair) : "+ penalty + "\n");
            fw.flush();

            //-----------------------------All pairs-----------------------------------------------

            penalty =Main.all_pair_penalty(g,lineCount2,students);
            fw.write("Penalty (All possible pairs) : "+ penalty + "\n");
            fw.flush();

            //----------------------------------------Kempe-chain Interchange----------------------------------


            for(i=0;i<kempe_loop;i++)
            g = Main.Kempe_chain(g,id_color,students,lineCount2);

            //  ---------------------         Penalty Calculation(After Kempe-chain)       ---------------------
           fw.write("\nDegree of saturation (after Kempe-chain) \n");
            fw.flush();


            //System.out.println(lineCount2);
            penalty = Main.consecutive_penalty(g,lineCount2,students);

            fw.write("Penalty (Consecutive pair) : "+ penalty + "\n");
            fw.flush();

            //-----------------------------All pairs-----------------------------------------------

            penalty = Main.all_pair_penalty(g,lineCount2,students);
            fw.write("Penalty (All possible pairs) : "+ penalty + "\n");
            fw.flush();







            //-----------------------------------------------Largest Degree------------------------------------------
                count=0;
            for(i=0;i<length;i++)
            {
                color_count[i] = -1;
                id_color[i] = -1;
                Colored[i] = false;
            }
                while(count < length) {
                    maxDegree = 0;
                    vertexWithMaxdegree = 0;
                    for (i = 0; i < length; i++) {
                        if (g.nodes.get(i).degree > maxDegree && !Colored[i]) {

                            maxDegree = g.nodes.get(i).degree;
                            vertexWithMaxdegree = i;
                        }
                    }
                    n = g.nodes.get(vertexWithMaxdegree);
                    boolean[] available_color=new boolean[length];

                    for (j = 0; j < length; j++)
                        available_color[j] = true;

                    for (j = 0; j < length; j++) {

                        if (Colored[j] && g.mat[j][vertexWithMaxdegree] == 1 ) {
                            available_color[id_color[j]] = false;
                        }
                    }

                    for (j = 0; j < length; j++) {


                        if (available_color[j] == true) {

                            n.slot_no = j;
                            break;

                        }
                    }

                    id_color[vertexWithMaxdegree] = n.slot_no;
                    color_count[n.slot_no] = 1;
                    Colored[vertexWithMaxdegree]=true;
                    count++;
                }

             total_used_color = 0;

            for (i = 0; i < length; i++) {
                //System.out.println(color_count[i]);
                if (color_count[i] == 1)
                    total_used_color++;
            }
           // System.out.println("File "+name[0] + "  Largest Degree :" + total_used_color);

            fw.write("\nLargest Degree: "+total_used_color+"\n" );
            fw.flush();

            //  ---------------------         Penalty Calculation       ---------------------

            penalty = Main.consecutive_penalty(g,lineCount2,students);
            fw.write("Penalty (Consecutive pair) : "+ penalty + "\n");
            fw.flush();

            //-----------------------------All pairs-----------------------------------------------

            penalty = Main.all_pair_penalty(g,lineCount2,students);
            fw.write("Penalty (All possible pairs) : "+ penalty + "\n");
            fw.flush();


            //-----------------------------------------Kempe-Chain Interchange--------------------------------------------------------



            for(i=0;i<kempe_loop;i++)
            Main.Kempe_chain(g,id_color,students,lineCount2);

            fw.write("\nlargest Degree (After Kempe):\n");
            fw.flush();
            penalty = Main.consecutive_penalty(g,lineCount2,students);

            fw.write("Penalty (consecutive pair): "+ penalty +" \n");
            fw.flush();

            penalty = Main.all_pair_penalty(g,lineCount2,students);

            fw.write("Penalty (all possible pair): "+ penalty +" \n");
            fw.flush();

                fw.write("---------------------------------------------------------------------------------------\n\n\n");
                fw.flush();

            }
        }


    public static Node getHighestSaturation (Graph g,int[] id_color){
        int maxSaturation = 0;
        int vertexWithMaxSaturation = 0;
        int length = g.num_of_node;

        for (int i = 0; i < length; i++) {
            if (id_color[i] == -1) {
                Set<Integer> colors = new TreeSet<>();
                for (int j = 0; j < length; j++) {
                    if (g.mat[i][j] == 1 && id_color[j] != -1) {
                        colors.add(id_color[j]);
                    }
                }
                int tempSaturation = colors.size();
                if (tempSaturation > maxSaturation) {
                    maxSaturation = tempSaturation;
                    vertexWithMaxSaturation = i;
                } else if (tempSaturation == maxSaturation && g.nodes.get(i).degree > g.nodes.get(vertexWithMaxSaturation).degree) {
                    vertexWithMaxSaturation = i;
                }
            }
        }
        return g.nodes.get(vertexWithMaxSaturation);
    }

    public static Graph Kempe_chain(Graph g,int[] id_color,int[][] students,int line)
    {
        double prev_pen,pen;
        prev_pen = Main.all_pair_penalty(g,line,students);
        int color_i,color_j = -1,length=g.num_of_node,i,j,k;
        int random_vertex = (int) (Math.random() * (length));

       //System.out.println(random_vertex);
        color_i=id_color[random_vertex];
        int[] chain=new int[length];
        boolean[] chain_check=new boolean[length];
        for(i=0;i<length;i++)
            chain_check[i] = true;
        j=0;
        chain[j]=random_vertex;
        chain_check[random_vertex]=false;
        j++;
        for(i=0;i<length;i++)
        {
            if(g.mat[i][random_vertex] == 1)
            {
                color_j=id_color[i];
                chain[j]=i;
                chain_check[i]=false;
                j++;
                break;
            }
        }
        Stack<Integer> s=new Stack<>();
        k=random_vertex;
        s.push(i);

        while(!s.empty())
        {
            for(i=0;i<length;i++)
            {
                if(g.mat[i][k] == 1 && (id_color[i] == color_i || id_color[i] == color_j ) && chain_check[i])
                {
                    s.push(i);
                    chain[j]=i;
                    chain_check[i]=false;
                    j++;
                }
            }
            k=s.pop();
        }
//-----------------------swap------------------------------
        for (i=0;i < j;i++)
        {

            if(id_color[chain[i]] == color_i)
            {
                id_color[chain[i]] = color_j;
                g.nodes.get(chain[i]).slot_no =color_j;
            }
            else
            {
                id_color[chain[i]] = color_i;
                g.nodes.get(chain[i]).slot_no =color_i;
            }
        }

        //----------------Reverse--------------
        pen = Main.all_pair_penalty(g,line,students);

       //System.out.println("prev-pen: "+prev_pen+"  Pen: "+pen);
        if(pen > prev_pen)
        {
            for (i=0;i < j;i++)
            {

                if(id_color[chain[i]] == color_i)
                {
                    id_color[chain[i]] = color_j;
                    g.nodes.get(chain[i]).slot_no =color_j;
                }
                else
                {
                    id_color[chain[i]] = color_i;
                    g.nodes.get(chain[i]).slot_no =color_i;
                }
            }
        }
        return g;

    }

    public static double consecutive_penalty(Graph g,int lineCount2,int[][] students)
    {
        double penalty=0,gap;
        int i,j,k;
        //System.out.println(lineCount2);

        for(i=0;i<lineCount2;i++)
        {
            Node[] array =new Node[students[i][0]];
            for(j=1;j<=students[i][0];j++)
            {
                if(students[i][j]<1)
                    break;
                array[j-1]=g.nodes.get(students[i][j]-1);
            }
            Arrays.sort(array,new SortByColor());
            for(j=0;j<(students[i][0]-1);j++)
            {
                gap=Math.abs(array[j].slot_no - array[j+1].slot_no);
                if(gap < 6)
                    penalty += Math.pow(2,(5-gap));
            }
            array=null;

        }
        penalty /= lineCount2;
        return penalty;
    }

    public static double all_pair_penalty(Graph g,int lineCount2,int[][] students)
    {
        double penalty=0,gap;
        int i,j,k;
        for(i=0;i<lineCount2;i++)
        {
            Node[] array =new Node[students[i][0]];
            for(j=1;j<=students[i][0];j++)
            {
                if(students[i][j]<1)
                    break;
                array[j-1]=g.nodes.get(students[i][j]-1);
            }
            Arrays.sort(array,new SortByColor());
            for(j=0;j< array.length;j++)
            {
                for(k=j+1;k < array.length ;k++ ){

                    gap=Math.abs(array[j].slot_no - array[k].slot_no);
                    if(gap < 6)
                        penalty += Math.pow(2,(5-gap));
                }
            }
        }
        penalty /= lineCount2;
        return penalty;
    }

}
