import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/*
Implementing a network-flow algorithm using the ford-fulkerson method using BFS (Edmonds-Karp)

The structure of the railway system is a set of edges connecting stations.
Each edge has a capacity.
The total flow from begin node to end node has to be at least C.
We will be given a list of edges to remove in a specific order. Remove if possible (flow not below C).

Answer: How many routes on the list of routes to remove can we remove?
        What is the maximum flow through the graph from start to end with the edges removed.

Node 0 is considered start (Minsk)
Node nNodes - 1 is considered end (Lund)


When removing an edge, the full flow of the graph does not need to be recomputed
We can instead look at how the flow is affected by the removal.
Reuse the flow computed initially, and fix up the flow.
*/

public class Main {

    public static ResidualGraph creatResidualGraph(Graph g) {
        int n = g.adj.length;
        ResidualGraph rg = new ResidualGraph(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // capacity - flow = residual capacity
                rg.adj[i][j] = g.adj[i][j].capacity - g.adj[i][j].flow;
            }
        }
        return rg;
    }

    public static List<Integer> find_path_bfs(ResidualGraph rg) {
        int n = rg.adj.length;
        int sink = n - 1;

        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        visited[0] = true;
        Queue<Integer> q = new LinkedList<>();
        q.add(0);

        while (!q.isEmpty()) {
            int curr = q.poll();

            if (curr == sink) {
                // reconstruct path from sink back to source
                ArrayList<Integer> path = new ArrayList<>();
                int node = sink;
                while (node != 0) {
                    path.add(node);
                    node = parent[node];
                }
                path.add(0);          // prepend source
                return path.reversed();
            }

            for (int i = 0; i < n; i++) {
                if (rg.adj[curr][i] > 0 && !visited[i]) {
                    visited[i] = true;
                    parent[i] = curr;
                    q.add(i);
                }
            }
        }

        return null; // no path to sink
    }

    public static int bottleneck(ResidualGraph rg, List<Integer> path) {
        int min = Integer.MAX_VALUE;
        // FIX: start from index 0 and look at edge path[i] -> path[i+1]
        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to   = path.get(i + 1);
            min = Math.min(min, rg.adj[from][to]);
        }
        return min;
    }

    public static int fordFulkerson(Graph g) {
        // FIX: reset all flows before computing max flow
        for (int i = 0; i < g.adj.length; i++)
            for (int j = 0; j < g.adj.length; j++)
                g.adj[i][j].flow = 0;

        int totalFlow = 0;
        ResidualGraph rg = creatResidualGraph(g);
        List<Integer> pathTaken = find_path_bfs(rg);

        while (pathTaken != null) {
            int bottleneck = bottleneck(rg, pathTaken);

            // FIX: use actual node IDs from path, not loop index i
            int prev = pathTaken.get(0); // source = 0
            for (int i = 1; i < pathTaken.size(); i++) {
                int curr = pathTaken.get(i);

                // update original graph flow
                g.adj[prev][curr].flow += bottleneck;
                g.adj[curr][prev].flow -= bottleneck;

                // update residual graph: capacity - flow for both directions
                rg.adj[prev][curr] = g.adj[prev][curr].capacity - g.adj[prev][curr].flow;
                rg.adj[curr][prev] = g.adj[curr][prev].capacity - g.adj[curr][prev].flow;

                prev = curr;
            }

            totalFlow += bottleneck;
            pathTaken = find_path_bfs(rg);
        }

        return totalFlow;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // first line: N M C P
        int nNodes          = scan.nextInt();
        int mEdges          = scan.nextInt();
        int cCapacity       = scan.nextInt();
        int pRoutesToRemove = scan.nextInt();

        Graph g = new Graph(nNodes);

        int[] fromE = new int[mEdges];
        int[] toE   = new int[mEdges];

        for (int i = 0; i < mEdges; i++) {
            // edges are undirected — store both directions
            int nodeU = scan.nextInt();
            int nodeV = scan.nextInt();
            int c     = scan.nextInt();

            g.adj[nodeU][nodeV] = new Tuple(c, 0);
            g.adj[nodeV][nodeU] = new Tuple(c, 0);

            fromE[i] = nodeU;
            toE[i]   = nodeV;
        }

        int lowestPossibleCapacity = fordFulkerson(g); // initial max flow before any removal
        int nbrEdgesRemoved = 0;

        for (int i = 0; i < pRoutesToRemove; i++) {
            int edgeIdx = scan.nextInt();
            int u = fromE[edgeIdx];
            int v = toE[edgeIdx];

            // tentatively remove the edge
            int savedCap = g.adj[u][v].capacity;
            g.adj[u][v].capacity = 0;
            g.adj[v][u].capacity = 0; // FIX: remove both directions

            int newCap = fordFulkerson(g);

            if (newCap < cCapacity) {
                // FIX: restore the edge if removing it violates the capacity constraint
                g.adj[u][v].capacity = savedCap;
                g.adj[v][u].capacity = savedCap;
                break;
            }

            lowestPossibleCapacity = newCap;
            nbrEdgesRemoved++;
        }

        System.out.println(nbrEdgesRemoved + " " + lowestPossibleCapacity);
        scan.close();
    }

    static class Tuple {
        public int capacity, flow;
        public Tuple(int c, int f) {
            this.capacity = c;
            this.flow     = f;
        }
    }

    static class Graph {
        public Tuple[][] adj;
        public Graph(int n) {
            this.adj = new Tuple[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    this.adj[i][j] = new Tuple(0, 0);
        }
    }

    static class ResidualGraph {
        public int[][] adj;
        public ResidualGraph(int n) {
            this.adj = new int[n][n];
        }
    }
}
