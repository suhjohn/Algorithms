/***
 *
 * Corner cases:
 * All methods should throw a java.lang.IllegalArgumentException
 *      if any argument is null or if any argument vertex is invalid—not between 0 and G.V() - 1.
 *
 * Performance:
 * All methods (and the constructor) should take time at most proportional to E + V in the worst case,
 * where E and V are the number of edges and vertices in the digraph, respectively.
 * Your data type should use space proportional to E + V.
 * **/

import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class SAP {
    private final Digraph G;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = G;
    }

    private void validateVertex(int v) {
        int V = this.G.V();
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        } else {
            int V = this.G.V();
            Iterator i$ = vertices.iterator();

            int v;
            do {
                if (!i$.hasNext()) {
                    return;
                }

                v = (Integer) i$.next();
            } while (v >= 0 && v < V);

            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        DeluxBFS bfs = new DeluxBFS(this.G, v, w);
        return bfs.getAncestorDistance();
    }


    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        DeluxBFS bfs = new DeluxBFS(this.G, v, w);
        return bfs.getAncestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);

        DeluxBFS bfs = new DeluxBFS(this.G, v, w);
        return bfs.getAncestorDistance();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);

        DeluxBFS bfs = new DeluxBFS(this.G, v, w);
        return bfs.getAncestor();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        System.out.println("type: ");
        String type = StdIn.readString();
        if (type.equals("int")) {
            while (!StdIn.isEmpty()) {

                int v = StdIn.readInt();
                int w = StdIn.readInt();
                System.out.println("---- LENGTH ----");
                int length = sap.length(v, w);
                System.out.println("---- ANCESTOR ----");
                int ancestor = sap.ancestor(v, w);
                StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            }
        } else if (type.equals("iter")){
            while (!StdIn.isEmpty()) {
                ArrayList<Integer> vList = new ArrayList<>();
                ArrayList<Integer> wList = new ArrayList<>();
                String str = StdIn.readString();
                while (!str.equals("next")){
                    Integer v = Integer.parseInt(str);
                    vList.add(v);
                    str = StdIn.readString();
                }
                str = StdIn.readString();
                while (!str.equals("next")){
                    Integer w = Integer.parseInt(str);
                    wList.add(w);
                    str = StdIn.readString();
                }
                System.out.println("---- LENGTH ----");
                int length = sap.length(vList, wList);
                System.out.println("---- ANCESTOR ----");
                int ancestor = sap.ancestor(vList, wList);
                StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            }
        }
    }
}
