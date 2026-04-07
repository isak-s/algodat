import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int N = scan.nextInt();
        int Q = scan.nextInt();

        Graph g = new Graph(N);

        for (int i = 0; i < N; i++) {
            g.addVertex(scan.next());
        }

        g.buildGraph();

        for (int i = 0; i < Q; i++) {
            String w1 = scan.next();
            String w2 = scan.next();
            int dist = g.distance(w1, w2);
            System.out.println(dist == Integer.MAX_VALUE ? "Impossible" : dist);
        }

        scan.close();
    }

    static class Graph {
        private ArrayList<Vertex> vertices;
        private HashMap<String, Vertex> vertexMap;
        private HashMap<String, List<Vertex>> buckets;

        public Graph(int n) {
            vertices = new ArrayList<>(n);
            vertexMap = new HashMap<>();
            buckets = new HashMap<>();
        }

        public void addVertex(String s) {
            Vertex v = new Vertex(s.toLowerCase());
            vertices.add(v);
            vertexMap.put(v.s, v);
        }

        public void buildGraph() {
            // Step 1: bucket vertices by frequency signature
            for (Vertex v : vertices) {
                String key = encode(v.freq);
                buckets.computeIfAbsent(key, k -> new ArrayList<>()).add(v);
            }

            for (Vertex v1 : vertices) {
                int[] need = new int[26];

                // last 4 chars of v1
                for (int i = 1; i < 5; i++) {
                    need[v1.s.charAt(i) - 'a']++;
                }

                // try all possible extra letters
                for (int c = 0; c < 26; c++) {
                    int[] candidate = need.clone();
                    candidate[c]++;

                    String key = encode(candidate);
                    List<Vertex> matches = buckets.get(key);

                    if (matches != null) {
                        for (Vertex v2 : matches) {
                            if (v1 != v2) {
                                v1.neighbors.add(v2);
                            }
                        }
                    }
                }
            }
        }

        public int distance(String a, String b) {
            Vertex start = vertexMap.get(a);
            Vertex target = vertexMap.get(b);

            if (start == null || target == null) return Integer.MAX_VALUE;

            Queue<Vertex> queue = new ArrayDeque<>();
            HashMap<Vertex, Integer> dist = new HashMap<>();

            queue.add(start);
            dist.put(start, 0);

            while (!queue.isEmpty()) {
                Vertex cur = queue.poll();
                int d = dist.get(cur);

                if (cur == target) return d;

                for (Vertex nei : cur.neighbors) {
                    if (!dist.containsKey(nei)) {
                        dist.put(nei, d + 1);
                        queue.add(nei);
                    }
                }
            }

            return Integer.MAX_VALUE;
        }

        private String encode(int[] freq) {
            StringBuilder sb = new StringBuilder();
            for (int f : freq) {
                sb.append(f).append('#');
            }
            return sb.toString();
        }

        static class Vertex {
            String s;
            int[] freq;
            ArrayList<Vertex> neighbors;

            Vertex(String s) {
                this.s = s;
                this.freq = new int[26];
                this.neighbors = new ArrayList<>();

                for (int i = 0; i < 5; i++) {
                    freq[s.charAt(i) - 'a']++;
                }
            }
        }
    }
}
