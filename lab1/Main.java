import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // The first row of the input consists two integers N , Q
        // N is the number of words
        // Q is the number of querie
        int N = scan.nextInt();
        int Q = scan.nextInt();

        // create graph
        Graph g = new Graph(N);
        // then follows N lines containing one 5 letter word each
        // add vertices
        for (int i = 0; i < N; i++) {
            g.addVertex(scan.next());
        }
        // add edges
        g.createEdges();

        // after that Q lines with 2 (space seperated) words each
        // for each of these lines we answer the query:
        // if there exists a path from the first to the second word, output the length
        // of the path
        // otherwise print impossible
        for (int i = 0; i < Q; i++) {
            String w1 = scan.next();
            String w2 = scan.next();
            int dist = g.distanceFromVtoV(w1, w2);
            System.out.println(dist == Integer.MAX_VALUE ? "Impossible" : dist);
        }
        scan.close();
    }

    public static class Graph {
        private ArrayList<Vertex> vertices;
        private HashMap<Vertex, HashSet<Vertex>> edges;
        private int nEdges;

        private int n;
        public Graph(int n) {
            this.n=n;
            this.vertices = new ArrayList<Vertex>(n);
            this.edges = new HashMap<Vertex, HashSet<Vertex>>();
        }

        public void addVertex(String s) {
            vertices.add(new Vertex(s.toLowerCase()));
        }

        public void createEdges() {
            // for v in vertices
            // for all other v in vertices add an edeg if v connects to b (if i == j do
            // nothing)
            for (Vertex v1 : vertices) {
                edges.put(v1, new HashSet<>());
                for (Vertex v2 : vertices) {
                    if (v1 == v2)
                        continue;

                    if (v1.connectsTo(v2)) {
                        edges.get(v1).add(v2);
                        nEdges++;
                    }
                }
            }
        }

        public int distanceFromVtoV(String a, String b) {

            // perform breadh first search
            // from a, get all connected vertices

            return distanceFromVtoV(new Vertex(a), new Vertex(b), 0);
        }

        private int distanceFromVtoV(Vertex a, Vertex b, int len) {
            if (a.equals(b))
                return len;

            int min = Integer.MAX_VALUE;
            
            if (len>n) {
                return min;
            }
            int i= 0;
            for (Vertex v : edges.get(a)) {
                System.err.println(i);
                i++;
                min = Integer.min(distanceFromVtoV(v, b, len+1), min);
            }

            return min;
        }

        private class Vertex {
            public String s;

            public Vertex(String s) {
                this.s = s;
            }

            public Boolean connectsTo(Vertex v2) {
                // take the last 4 letters in this vertex
                // does b contain each of them?
                String tmp = v2.s;
                for (int i = 1; i < 5; i++) {
                    String c = this.s.substring(i, i + 1);
                    if (!tmp.contains((c))) {
                        return false;
                    }
                    tmp=tmp.replaceFirst(c, "");
                }
                return true;
            }

            @Override
            public boolean equals(Object obj) {
                Vertex v = (Vertex) obj;
                return v.s.equals(this.s);
            }

            @Override
            public int hashCode() {
                return this.s.hashCode();
            }
        }
    }
}
