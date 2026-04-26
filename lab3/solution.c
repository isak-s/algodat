/*
A friend of a friend is automatically a friend.
2 people have a certain compatability - amount of minutes it would take to become friends
It is possible to generate a path between any 2 people
Minimize the work to connect all.

Solving minimum spanning tree.
Prints the number of minutes needed to connect all people.

The solution below uses kruskal's algorithm.
our vertices in our MST start off as sets of only single vertices.
When we insert an edge between two vertices, we perform a union on the set.
This is done by marking the node that it connects from to be the parent of the
    node it connects to
When sets of size >= 2 are merged, the parent of the first set is set as the
    parents of the second set.

We begin with sorting all edges in ascending order.
Then we iterate over them.
    If they don't share the same parent node, we perform a union on the sets.
    Otherwise we skip that edge, and go on to the next slightly higher cost edge
        until we find one that connects 2 disjoint sets.

The input has to be a strongly connected graph for there to be a Spanning tree for it.
This algoritm asusmes we get a correct input, and will give misleading results
if the graph contains disjoint sets.

A spanning tree is a strongly connected graph with n nodes and n-1 edges.
There can be many spanning trees for a single input graph, and many with the same cost.

Proof by induction:
assume we have a gragh g with n vertices and m edges and need to create a MST.
We create a new graph, initialized with no edges.

The vertices are set into n different sets, representing sub-graphs of the final MST.

get the smallest weight edge e in g not yet processed.
    if the 2 nodes it connects are already connected, the weight of e must be
        higher than the weight of the existing connection, and should be ignored.
        The sub-graph stays a MST.
    if the edge connects 2 disjoint sub-graphs of the MST with the cheapest cost,
        the union will be a sub-graph of the MST since it is the edge is
        cheaper than all that come after.

The sorting of edges is O(mlog(m))
The implementation of Kruskals algorithm then iterates over all edges once,
and during each iteration it will get the parents of both vertices.
This operation will be O(1) on average since path compression is used.

total O(mlog(m)) since mlog(m) dominates over the actual building of the MST.

If an edge collapses, the graph becomes disjoint. There is no contingency.
For example if this was used to connect internet routers,
we would not be able to communicate between the cut off subgraphs.
Not acceptable for internet routning.
Acceptable for friend groups.
*/
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <stdbool.h>

typedef struct {
    int weight;
    int node1, node2;
} Edge;

int edgeComparator(const void* e1, const void* e2)
{
    return ((Edge*) e1)->weight - ((Edge*) e2)->weight;
}

int getParent(int* parents, const int child)
{
    int curr = child;
    for (; curr != parents[curr]; curr = parents[curr]) {}
    int rootParent = curr;
    // path compression for faster subsequent lookups:
    for (curr = child; curr != parents[curr]; curr = parents[curr]) {
        parents[curr] = rootParent;
    }
    return curr;
}

int totalWeightMST(int* parents, Edge* edges, int mEdges)
{

    qsort(edges, mEdges, sizeof(Edge), edgeComparator);
    // once they are all connected, we have achieved the minimum cost spanning tree
    // connected means that all parents are the same node
    int cost = 0;
    for (int i = 0; i < mEdges; i++) {
        Edge curr = edges[i];
        int p1 = getParent(parents, curr.node1);
        int p2 = getParent(parents, curr.node2);
        if (p1 != p2) {
            cost += curr.weight;
            parents[p1] = p2;
            // MST.add(curr).
            // We could build the spanning tree by adding only these edges
        }
    }
    return cost;
}

int main(int argc, char* argv)
{
    // 2 <= n <= 10^5
    // 1 <= m <= min(n * (n-1)/2, 3*10^6)

    int nPeople, mEdges;
    scanf("%d %d", &nPeople, &mEdges);

    int* parents = malloc(sizeof(int)*nPeople);
    for (int i = 0; i<nPeople; i++) {
        parents[i] = i;
    }

    Edge* edges = malloc(sizeof(Edge)*mEdges);
    // 3 integers per line. u, v, w
    // u and v are indices of people at the edge. W is the weight of the edge
    int u, v, w;
    for (int i = 0; i<mEdges; i++) {
        scanf("%d %d %d", &u, &v, &w);
        edges[i].node1 = (u-1);
        edges[i].node2 = (v-1);
        edges[i].weight = w;
    }

    printf("%d", totalWeightMST(parents, edges, mEdges));
}