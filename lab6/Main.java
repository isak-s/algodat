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

public class Main {

    public int fordFulkerson(HashMap<Integer, Edge> edges, int start, int end) {
        return 0;
    }

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        // first line consists of four integers N M C P
        int nNodes = scan.nextInt();
        int mEdges = scan.nextInt();
        int cCapacity = scan.nextInt();
        int pRoutesToRemove = scan.nextInt();

        Node[] nodes = new Node[nNodes];
        for (int i = 0; i < nNodes; i++) {
            nodes[i] = new Node();
        }

        for (int i = 0; i < mEdges; i++) {
            // these edges are undirected
            int nodeV = scan.nextInt();
            int nodeU = scan.nextInt();
            int c = scan.nextInt();
            Edge e = new Edge(nodeU, nodeV, c, i);

            nodes[nodeU].addEdge(e);
        }

        int lowestPossibleCapacity = 0;
        int nbrEdgesRemoved = 0;

        for (int i = 0; i < pRoutesToRemove; i++) {
            int edgeIdx = scan.nextInt();

            removeEdge(nodes, edgeIdx);

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

    public static void removeEdge(Node[] nodes, int edgeIdxToRemove) {
        for (Node node : nodes) {
            for (Edge e : node.edges) {
                if (e.idx == edgeIdxToRemove) {
                    node.removeEdge(e);
                    return;
                }
            }
        }
    }

    static class Node {
        public HashSet<Edge> edges;

        public Node() {
        }

        public void addEdge(Edge e) {
            edges.add(e);
        }

        public void removeEdge(Edge e) {
            edges.remove(e);
        }
    }

    static class Edge {
        public int u;
        public int v;
        public int c;
        public int flow;
        public int idx;

        public Edge(int u, int v, int c, int idx) {
            this.u = u;
            this.v = v;
            this.c = c;
            this.flow = 0;
        }
    }
}