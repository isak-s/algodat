import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
/*
Implementing a network-flow algorithm
The structure of the railway system is a set of edges connecting stations.
Each edge has a capacity
The total flow from begin node to end node has to be at least C
We will be given a list of edges to remove in a specific order. Remove if possible (flow not below c)

answer: How many routes on the list of routes to remove can we remove?
    What is the maximum flow through the graph from start to end with the edges removed

Node 0 is considered start (minsk)
*/
public class Main {
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        HashMap<Integer, Edge> edges = new HashMap<>();
        // first line consists of four integers N M C P
        int nNodes = scan.nextInt();
        int mEdges = scan.nextInt();
        int cCapacity = scan.nextInt();
        int pRoutesToRemove = scan.nextInt();


        for (int i = 0; i < mEdges; i++) {
            // these edges are undirected
            int nodeV = scan.nextInt();
            int nodeU = scan.nextInt();
            int c = scan.nextInt();
            Edge e = new Edge(nodeU, nodeV, c, i);
            // construt the graph here
            edges.put(i, e);
        }

        for (int i = 0; i < pRoutesToRemove; i++) {
            int edgeIdx = scan.nextInt();
            Boolean canRemove = false;
            if (canRemove) {
                edges.remove(edgeIdx);
            }

        }
        scan.close();
    }

    static class Edge {
        public int u;
        public int v;
        public int c;
        public int idx;

        public Edge(int u, int v, int c, int idx) {
            this.u = u;
            this.v = v;
            this.c = c;
            this.idx = idx;
        }
    }
}