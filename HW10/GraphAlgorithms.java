import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

/**
 * Your implementation of various different graph algorithms.
 *
 * @author Lucas Luwa
 * @version 1.2823
 * @userid lluwa3 (i.e. gburdell3)
 * @GTID 903593176 (i.e. 900000000)
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class GraphAlgorithms {

    /**
     * Performs a breadth first search (bfs) on the input graph, starting at
     * the parameterized starting vertex.
     *
     * When exploring a vertex, explore in the order of neighbors returned by
     * the adjacency list. Failure to do so may cause you to lose points.
     *
     * You may import/use java.util.Set, java.util.List, java.util.Queue, and
     * any classes that implement the aforementioned interfaces, as long as they
     * are efficient.
     *
     * The only instance of java.util.Map that you may use is the
     * adjacency list from graph. DO NOT create new instances of Map
     * for BFS (storing the adjacency list in a variable is fine).
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the bfs on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph
     */
    public static <T> List<Vertex<T>> bfs(Vertex<T> start, Graph<T> graph) {
        if (start == null || graph == null || !graph.getAdjList().containsKey(start)) {
            throw new IllegalArgumentException("Either your input values are null or the start value "
                    + "doesn't exist in the graph");
        }
        Queue<Vertex<T>> bfsQueue = new LinkedList<>();
        List<Vertex<T>> retVal = new LinkedList<>();
        Set<Vertex<T>> bfsSet = new HashSet<>();

        bfsQueue.add(start);
        bfsSet.add(start);
        //        while (!bfsQueue.isEmpty() && bfsSet.size() < graph.getEdges().size()) {
        while (!bfsQueue.isEmpty() && bfsSet.size() <= graph.getVertices().size()) {
            Vertex<T> holder = bfsQueue.remove();
            retVal.add(holder);
            for (VertexDistance<T> dist: graph.getAdjList().get(holder)) {
                if (!bfsSet.contains(dist.getVertex())) {
                    bfsSet.add(dist.getVertex());
                    bfsQueue.add(dist.getVertex());
                }
            }
        }
        return retVal;
    }

    /**
     * Performs a depth first search (dfs) on the input graph, starting at
     * the parameterized starting vertex.
     *
     * When exploring a vertex, explore in the order of neighbors returned by
     * the adjacency list. Failure to do so may cause you to lose points.
     *
     * *NOTE* You MUST implement this method recursively, or else you will lose
     * all points for this method.
     *
     * You may import/use java.util.Set, java.util.List, and
     * any classes that implement the aforementioned interfaces, as long as they
     * are efficient.
     *
     * The only instance of java.util.Map that you may use is the
     * adjacency list from graph. DO NOT create new instances of Map
     * for DFS (storing the adjacency list in a variable is fine).
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the dfs on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph
     */
    public static <T> List<Vertex<T>> dfs(Vertex<T> start, Graph<T> graph) {
        if (start == null || graph == null || !graph.getAdjList().containsKey(start)) {
            throw new IllegalArgumentException("Either your input values are null or the start value "
                    + "doesn't exist in the graph");
        }
        List<Vertex<T>> retVal = new LinkedList<>();
        Set<Vertex<T>> dfsSet = new HashSet<>();
        dfsHelper(start, graph, retVal, dfsSet);
        return retVal;
    }

    /**
     *
     * @param start item to start at.
     * @param graph graph provided
     * @param retList list defined in dfs
     * @param dfsSet set defined in dfs
     * @param <T> generic type
     */
    private static <T> void dfsHelper(Vertex<T> start, Graph<T> graph, List<Vertex<T>> retList, Set<Vertex<T>> dfsSet) {
        dfsSet.add(start);
        retList.add(start);
        for (VertexDistance<T> dist: graph.getAdjList().get(start)) {
            if (!dfsSet.contains(dist.getVertex())) {
                dfsHelper(dist.getVertex(), graph, retList, dfsSet);
            }
        }
    }

    /**
     * Finds the single-source shortest distance between the start vertex and
     * all vertices given a weighted graph (you may assume non-negative edge
     * weights).
     *
     * Return a map of the shortest distances such that the key of each entry
     * is a node in the graph and the value for the key is the shortest distance
     * to that node from start, or Integer.MAX_VALUE (representing
     * infinity) if no path exists.
     *
     * You may import/use java.util.PriorityQueue,
     * java.util.Map, and java.util.Set and any class that
     * implements the aforementioned interfaces, as long as your use of it
     * is efficient as possible.
     *
     * You should implement the version of Dijkstra's where you use two
     * termination conditions in conjunction.
     *
     * 1) Check if all of the vertices have been visited.
     * 2) Check if the PQ is empty yet.
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the Dijkstra's on (source)
     * @param graph the graph we are applying Dijkstra's to
     * @return a map of the shortest distances from start to every
     * other node in the graph
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph.
     */
    public static <T> Map<Vertex<T>, Integer> dijkstras(Vertex<T> start,
                                                        Graph<T> graph) {
        if (start == null || graph == null || !graph.getAdjList().containsKey(start)) {
            throw new IllegalArgumentException("Either your input values are null or the start value "
                    + "doesn't exist in the graph");
        }
        Queue<VertexDistance<T>> pQ = new PriorityQueue<>();
        List<Vertex<T>> vS = new LinkedList<>();
        Map<Vertex<T>, Integer> dM = new HashMap<>();
        for (Vertex<T> v: graph.getAdjList().keySet()) {
            if (v.equals(start)) {
                dM.put(v, 0);
            } else {
                dM.put(v, Integer.MAX_VALUE);
            }
        }
        pQ.add(new VertexDistance<>(start, 0));
        while (!pQ.isEmpty() && vS.size() < graph.getEdges().size() - 1) {
            VertexDistance<T> holder = pQ.remove();
            if (!vS.contains(holder)) {
                vS.add(holder.getVertex());
                for (VertexDistance<T> dist : graph.getAdjList().get(holder.getVertex())) {
                    int calculatedDist = holder.getDistance() + dist.getDistance();
                    if (dM.get(dist.getVertex()) > calculatedDist) {
                        dM.put(dist.getVertex(), calculatedDist);
                        pQ.add(new VertexDistance<>(dist.getVertex(), calculatedDist));
                    }
                }
            }
        }
        return dM;
    }

    /**
     * Runs Kruskal's algorithm on the given graph and returns the Minimal
     * Spanning Tree (MST) in the form of a set of Edges. If the graph is
     * disconnected and therefore no valid MST exists, return null.
     *
     * You may assume that the passed in graph is undirected. In this framework,
     * this means that if (u, v, 3) is in the graph, then the opposite edge
     * (v, u, 3) will also be in the graph, though as a separate Edge object.
     *
     * The returned set of edges should form an undirected graph. This means
     * that every time you add an edge to your return set, you should add the
     * reverse edge to the set as well. This is for testing purposes. This
     * reverse edge does not need to be the one from the graph itself; you can
     * just make a new edge object representing the reverse edge.
     *
     * You may assume that there will only be one valid MST that can be formed.
     *
     * Kruskal's will also require you to use a Disjoint Set which has been
     * provided for you. A Disjoint Set will keep track of which vertices are
     * connected given the edges in your current MST, allowing you to easily
     * figure out whether adding an edge will create a cycle. Refer
     * to the DisjointSet and DisjointSetNode classes that
     * have been provided to you for more information.
     *
     * You should NOT allow self-loops or parallel edges into the MST.
     *
     * By using the Disjoint Set provided, you can avoid adding self-loops and
     * parallel edges into the MST.
     *
     * You may import/use java.util.PriorityQueue,
     * java.util.Set, and any class that implements the aforementioned
     * interfaces.
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param graph the graph we are applying Kruskals to
     * @return the MST of the graph or null if there is no valid MST
     * @throws IllegalArgumentException if any input is null
     */
    public static <T> Set<Edge<T>> kruskals(Graph<T> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph is null. Please try again.");
        }
        DisjointSet<Vertex<T>> dS = new DisjointSet<>();
        Set<Edge<T>> mST = new HashSet<>();
        PriorityQueue<Edge<T>> pQ = new PriorityQueue<>(graph.getEdges());
        while (!pQ.isEmpty() && mST.size() < graph.getEdges().size() - 1) {
            Edge<T> currEdge = pQ.poll();
            if (currEdge == null) {
                return null;
            }
            Vertex<T> u = currEdge.getU();
            Vertex<T> v = currEdge.getV();
            if (dS.find(u) != dS.find(v)) {
                dS.union(dS.find(u), dS.find(v));
                dS.union(u, v);
                mST.add(new Edge<>(v, u, currEdge.getWeight()));
                mST.add(currEdge);
            }
        }
        return (mST.size() < (2 * (graph.getVertices().size() - 1))) ? null : mST;
    }
}
