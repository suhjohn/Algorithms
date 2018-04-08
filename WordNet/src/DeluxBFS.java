/**
 * The bottleneck operation is re-initializing arrays of length V to perform the BFS computations.
 * This must be done once for the first BFS computation, but if you keep track of which array entries change,
 * you can reuse the same array from computation to computation
 * (re-initializing only those entries that changed in the previous computation).
 * This leads to a dramatic savings when only a small number of entries change
 * (and this is the typical case for the wordnet digraph).
 * Note that if you have any other loops that iterates through all of the vertices,
 * then you must eliminate those to achieve a sublinear running time.
 * (An alternative is to replace the arrays with symbol tables, where, in constant time,
 * the constructor initializes the value associated with every key to be null.)
 * <p>
 * If you run the two breadth-first searches from v and w in lockstep
 * (alternating back and forth between exploring vertices in each of the two searches),
 * then you can terminate the BFS from v (or w)
 * as soon as the distance exceeds the length of the best ancestral path found so far.
 * <p>
 * Implement a software cache of recently computed length() and ancestor() queries.
 */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class DeluxBFS {
    private HashMap<Integer, Boolean> marked;
    private HashMap<Integer, Integer> distTo;
    private HashMap<Integer, Integer> edgeTo;


    private int ancestor = -1;
    private int ancestorDistance = -1;

    public DeluxBFS(Digraph G, int v, int w) {
        initialize();
        bfs(G, v, w);

    }

    public DeluxBFS(Digraph G, Iterable<Integer> sources, Iterable<Integer> goals) {
        initialize();
        bfs(G, sources, goals);
    }

    private void initialize() {
        this.marked = new HashMap<>();
        this.distTo = new HashMap<>();
        this.edgeTo = new HashMap<>();
    }

    public int getAncestor() {
        return this.ancestor;
    }

    public int getAncestorDistance() {
        return this.ancestorDistance;
    }


    private void bfs(Digraph G, int v, int w) {
        Queue<Integer> qV = new Queue<>();
        Queue<Integer> qW = new Queue<>();
        initQueue(v, qV);
        initQueue(w, qW);

        if (this.ancestor > -1) return;

        while (!qV.isEmpty() || !qW.isEmpty()) {
            if (!qV.isEmpty()) markSingle(G, qV, v);
            if (!qW.isEmpty()) markSingle(G, qW, w);
        }
    }

    private void initQueue(int index, Queue<Integer> queue) {
        queue.enqueue(index);
        if (this.marked.getOrDefault(index, null) != null) {
            this.ancestor = index;
            this.ancestorDistance = 0;
        }
        this.marked.put(index, true);
        this.distTo.put(index, 0);
    }

    private void bfs(Digraph G, Iterable<Integer> sources, Iterable<Integer> goals) {
        Queue<Integer[]> qSources = new Queue<>();
        Queue<Integer[]> qGoals = new Queue<>();
        initQueue(sources, qSources);
        initQueue(goals, qGoals);

        if (this.ancestor > -1) return;

        while (!qSources.isEmpty() || !qGoals.isEmpty()) {
            if (!qSources.isEmpty()) markList(G, qSources, 0);
            if (!qGoals.isEmpty()) markList(G, qGoals, 1);
        }
    }

    private void initQueue(Iterable<Integer> iterable, Queue<Integer[]> queue) {
        Iterator i$ = iterable.iterator();
        ArrayList<Integer> arr = new ArrayList<>();
        while (i$.hasNext()) {
            int s = (Integer) i$.next();
            if (this.marked.getOrDefault(s, false)) {
                this.ancestor = s;
                this.ancestorDistance = 0;
            }
            this.marked.put(s, true);
            this.distTo.put(s, 0);
            arr.add(s);
        }
        Integer[] indexes = new Integer[arr.size()];
        queue.enqueue(arr.toArray(indexes));
    }

    private void markSingle(Digraph G, Queue<Integer> queue, int root) {
        Integer index = queue.dequeue();
        for (int adj : G.adj(index)) {
            if (this.marked.getOrDefault(adj, false) && this.edgeTo.getOrDefault(adj, -1) != root) {
                this.ancestor = adj;
                this.ancestorDistance = this.distTo.get(adj) + this.distTo.get(index) + 1;
                return;
            }

            if (!this.marked.getOrDefault(adj, false)) {
                this.marked.put(adj, true);
                this.edgeTo.put(adj, root);
                this.distTo.put(adj, this.distTo.get(index) + 1);
                queue.enqueue(adj);
            }
        }
    }

    private void markList(Digraph G, Queue<Integer[]> queue, int root) {
        Integer[] indexes = queue.dequeue();
        ArrayList<Integer> arr = new ArrayList<>();

        for (int currIndex : indexes) {
            for (int adj : G.adj(currIndex)) {
                if (this.marked.getOrDefault(adj, false) && this.edgeTo.getOrDefault(adj, -1) != root) {
                    this.ancestor = adj;
                    this.ancestorDistance = this.distTo.get(adj) + this.distTo.get(currIndex) + 1;
                    return;
                }

                if (!this.marked.getOrDefault(adj, false)) {
                    this.marked.put(adj, true);
                    this.edgeTo.put(adj, root);
                    this.distTo.put(adj, this.distTo.get(currIndex) + 1);
                    arr.add(adj);
                }
            }
        }
        if (!arr.isEmpty()){
            Integer[] newIndexes = new Integer[arr.size()];
            queue.enqueue(arr.toArray(newIndexes));
        }
    }

}