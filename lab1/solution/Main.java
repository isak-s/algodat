import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // The first row of the input consists two integers N , Q
        // N is the number of words
        // Q is the number of querie
        int N = scan.nextInt();
        int Q = scan.nextInt();

        // create graph
        Graph g = new Graph(10);
        // then follows N lines containing one 5 letter word each
        // add vertices
        for (int i = 0; i < N; i++) {
            g.addVertex(scan.next());
        }
        // add edges
        g.createEdges();

        // after that Q lines with 2 (space seperated) words each
        // for each of these lines we answer the query:
        // if there exists a path from the first to the second word, output the length of the path
        // otherwise print impossible
        for (int i = 0; i < Q; i++) {
            String w1 = scan.next();
            String w2 = scan.next();
            int dist = g.distanceFromVtoV(w1, w2);
            System.out.println(dist == Integer.MAX_VALUE ? "impossible" : dist);
        }
        scan.close();
    }
}
