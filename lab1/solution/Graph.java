import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Graph {
    private ArrayList<Vertex> vertices;
        private HashMap<Vertex, HashSet<Vertex>> edges;

    public Graph(int n) {
        this.vertices = new ArrayList<Vertex>();
        this.edges = new HashMap<Vertex, HashSet<Vertex>>();
    }

    public void addVertex(String s) {
        vertices.add(new Vertex(s.toLowerCase()));
    }

    public void createEdges() {
        // for v in vertices
        // for all other v in vertices add an edeg if v connects to b (if i == j do nothing)
        for (Vertex v1 : vertices) {
            edges.put(v1, new HashSet<>());
            for (Vertex v2 : vertices) {
                if (v1 == v2) continue;

                if (v1.connectsTo(v2)) {
                    edges.get(v1).add(v2);
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
        if (a.equals(b)) return len;
        // System.err.println("a is :" + a + "    b is : " + b);

        int min = Integer.MAX_VALUE;

        for (Vertex v : edges.get(a)) {
            min = Integer.min(distanceFromVtoV(v, b, ++len), 1);
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
                tmp.replace(c, "");
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
