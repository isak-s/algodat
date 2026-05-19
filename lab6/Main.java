import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
/*
Implementing a network-flow algorithm using the ford-fulkerson method using DFS
()
The structure of the railway system is a set of edges connecting stations.
Each edge has a capacity
The total flow from begin node to end node has to be at least C
We will be given a list of edges to remove in a specific order. Remove if possible (flow not below c)

answer: How many routes on the list of routes to remove can we remove?
    What is the maximum flow through the graph from start to end with the edges removed

Node 0 is considered start (minsk)
Node nNodes - 1 is considered end (Lund)

2. DFS from start to end searching for a path where we can increase flow on every edge.
    The edge with the least capacity for increase -> take that delta and add to every edge in the path.
    Find delta.
    Store visited nodes so that we don't do a loop. store idx
    Store visited edges Apply delta to every edge in the path.
*/
import java.util.Stack;

public class Main {

    public static ResidualGraph creatResidualGraph(Graph g) {
        int n = g.adj.length;
        ResidualGraph rg = new ResidualGraph(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; i++) {
                if (g.adj[i][j] == null) {rg.adj[i][j] = 0;}
                rg.adj[i][j] = g.adj[i][j].capacity; // forwards edge
                // later these will be set to cap - flow and flow for backwards
            }
        }
        return rg;
    }

    // using dfs
    public static GTuple<ArrayList<Integer>, Integer> find_path(ResidualGraph rg) {
        boolean[] visited = new boolean[rg.adj.length];
        ArrayList<Integer> pathTaken = new ArrayList<>();

        int next = 0;
        int curr = Integer.MAX_VALUE;  // placeholder
        int bottleneck = Integer.MAX_VALUE;

        while (next != rg.adj.length -1) {

            if (next == curr) {
                return null;
            }

            int[] currAdj = rg.adj[next];
            curr = next;
            for (next = 0; next < currAdj.length; next++) {
                if (currAdj[next] == 0 || visited[next]) {continue;}
                visited[next] = true;

                bottleneck = Math.min(rg.adj[curr][next], bottleneck);
                pathTaken.add(next);
                curr = next;
                break;
            }
        }


        return new GTuple<ArrayList<Integer>,Integer>(pathTaken, bottleneck);
    }

    public static int fordFulkerson(Graph g) {
        ResidualGraph rg = creatResidualGraph(g);
        var p = find_path(rg);
        var pathTaken = p.a;
        var bottleneck = p.b;
        int totalFlow = 0;

        while (pathTaken != null) {
            int prev = 0; // first is always 0
            for (int i = 0; i < pathTaken.size(); i++) {
                // update original graph
                // upper triangle : we did a backwards traversal
                if (prev > i) {
                    g.adj[prev][i].flow -= bottleneck;
                } else {
                    g.adj[prev][i].flow += bottleneck;
                }
                // update residual graph
                // if flow and capacity are equal, this will be 0. 0 Represents no edge
                rg.adj[prev][i] = g.adj[prev][i].capacity - g.adj[prev][i].flow;
                rg.adj[i][prev] = g.adj[prev][i].flow;

                prev = pathTaken.get(i);
            }
            totalFlow += bottleneck;
        }

        return totalFlow;
    }

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        // first line consists of four integers N M C P
        int nNodes = scan.nextInt();
        int mEdges = scan.nextInt();
        int cCapacity = scan.nextInt();
        int pRoutesToRemove = scan.nextInt();

        Graph g = new Graph(nNodes);

        for (int i = 0; i < mEdges; i++) {
            // these edges are undirected
            int nodeV = scan.nextInt();
            int nodeU = scan.nextInt();
            int c = scan.nextInt();

            g.adj[nodeU][nodeV] = new Tuple(c, 0);
        }

        int lowestPossibleCapacity = 0;
        int nbrEdgesRemoved = 0;

        for (int i = 0; i < pRoutesToRemove; i++) {
            int edgeIdx = scan.nextInt();

            // removeEdge(nodes, edgeIdx);

            int newCap = fordFulkerson();
            Boolean canRemove = newCap > cCapacity;

            if (!canRemove) {
                break;
            }

            lowestPossibleCapacity = newCap;
            nbrEdgesRemoved++;

        }
        System.out.println(nbrEdgesRemoved + " " + lowestPossibleCapacity);
        scan.close();
    }

    static class GTuple<A, B> {
        public A a;
        public B b;

        public GTuple(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }

    static class Tuple {
        public int capacity, flow;
        public Tuple(int c, int f) {
            this.capacity = c;
            this.flow = f;
        }
    }
    static class Graph {
        public Tuple[][] adj;
        public Graph(int n) {
            this.adj = new Tuple[n][n];
        }
    }
    // lower triangle is positive edges, upper triangle is negative edges.
    static class ResidualGraph {
        public int[][] adj; // the capacity on each
        public ResidualGraph(int n) {
            this.adj = new int[n][n];
        }
    }

}