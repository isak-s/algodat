import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

    public static void main (String[] args){
        Scanner scan = new Scanner(System.in);


        int nVertices = scan.nextInt();
        int mEdges = scan.nextInt();

        HashSet<Integer> visited = new HashSet<>();

        int[] dist = new int[nVertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Vertex[] vertices = new Vertex[nVertices];

        for (int i=0;i<nVertices;i++){
            vertices[i]= new Vertex(i, Integer.MAX_VALUE);
        }
        PriorityQueue<Vertex> unvisitied = new PriorityQueue<Vertex>();

        for (int i=0; i<nVertices;i++){
            unvisitied.add(vertices[i]);
        }




        //Build graph
        for (int i=0;i<mEdges;i++){
            int v = scan.nextInt()-1;
            int u = scan.nextInt()-1;
            int w = scan.nextInt();

            vertices[v].addNeighbour(new Edge(vertices[u], w));
            vertices[u].addNeighbour(new Edge(vertices[v], w));
            //heapQ.add();
        }
        
        int cost=0;
        unvisitied.peek().dist = 0;

        for (int i=0;i<nVertices;i++) {
            Vertex curr = unvisitied.poll();
            cost+= curr.dist!=Integer.MAX_VALUE ? curr.dist : 0;
            visited.add(curr.index);
            curr.updateNeighbourWeights(unvisitied, visited);
        }

        System.out.println(cost);
    }


    public static class Vertex implements Comparable{
        public int index;
        public int dist;
        public ArrayList<Edge> neighbours;

        public Vertex(int index, int dist){
            this.index=index;
            this.dist=dist;
            neighbours = new ArrayList<>();
        }

        public void updateNeighbourWeights(PriorityQueue<Vertex> pQ, HashSet<Integer> visited){
            for (Edge neighbour: neighbours){
                if (!visited.contains(neighbour.u.index) && neighbour.w < neighbour.u.dist) {
                    var v = new Vertex(neighbour.u.index, neighbour.w);
                    for (Edge e : neighbours) {
                        v.addNeighbour(e);
                    }
                    pQ.add(v);
                }
            }
        }

        public void addNeighbour(Edge neighbour) {
            neighbours.add(neighbour);
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof Vertex)){
                throw new UnsupportedOperationException("Unimplemented method 'compareTo'");    
            }
            // TODO Auto-generated method stub
            Vertex e2 = (Vertex) o;
            return this.dist - e2.dist; 
            // return e2.dist - this.dist;
        }
    }

    public static class Edge{
        public Vertex u;
        public int  w;
        
        public Edge (Vertex u, int w) {
            this.u=u;
            this.w=w;
        }
    }
}