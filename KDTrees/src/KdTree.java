import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.TreeSet;

public class KdTree {
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean isVertical;

        public Node(Point2D p, RectHV rect, Node lb, Node rt, boolean isVertical) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
            this.isVertical = isVertical;
        }
    }

    private Node root = null;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.root == null;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (this.root == null) {
            this.root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0), null, null,
                    true);
        } else insert(this.root, this.root.rect, this.root.isVertical, p);
        this.size++;
    }

    private Node insert(Node node, RectHV rect, boolean isVertical, Point2D p) {
        if (node == null) {
            return new Node(p, rect, null, null, isVertical);
        }
        // For vertical lines, compare x to decide whether on the right side or left side.
        // For horizontal lines, compare y to decide whether on the top or bottom.
        int comp;
        RectHV subRect;
        if (isVertical) {
            comp = Double.compare(p.x(), node.p.x());
            if (comp < 0) {
                subRect = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
                node.lb = insert(node.lb, subRect, !node.isVertical, p);
            } else {
                subRect = new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                node.rt = insert(node.rt, subRect, !node.isVertical, p);
            }
        } else {
            comp = Double.compare(p.y(), node.p.y());
            if (comp < 0) {
                subRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
                node.lb = insert(node.lb, subRect, !node.isVertical, p);
            } else {
                subRect = new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
                node.rt = insert(node.rt, subRect, !node.isVertical, p);
            }
        }

        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to contains() is null");
        if (this.isEmpty()) return false;

        if (p.compareTo(this.root.p) == 0) return true;
        else return this.contains(this.root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node.p.compareTo(p) == 0) return true;

        int comp;
        if (node.isVertical) comp = Double.compare(p.x(), node.p.x());
        else comp = Double.compare(p.y(), node.p.y());

        if (comp < 0) return contains(node.lb, p);
        else return contains(node.rt, p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenRadius(0.01);

        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(this.root);
        while (!queue.isEmpty()) {
            Node node = queue.dequeue();
            if (node == null) continue;
            if (node.isVertical) {
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
                System.out.println(node.p.toString());
                System.out.println(String.format("%f , %f + %f , %f", node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax()));
            } else {
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
                System.out.println(node.p.toString());
                System.out.println(String.format("%f , %f + %f , %f", node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y()));
            }
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BLACK);
            node.p.draw();
            System.out.println(node.isVertical);

            queue.enqueue(node.lb);
            queue.enqueue(node.rt);
        }
    }

    // all points that are inside the rectangle (or on the boundary)

    /**
     * Start at the root and recursively search for points in both subtrees using the following pruning rule:
     * if the query rectangle does not intersect the rectangle corresponding to a node,
     * there is no need to explore that node (or its subtrees).
     * A subtree is searched only if it might contain a point contained in the query rectangle.
     **/
    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> range = new ArrayList<>();
        Queue<Node> nodesToSearch = new Queue<>();
        nodesToSearch.enqueue(this.root);
        while (!nodesToSearch.isEmpty()) {
            Node curr = nodesToSearch.dequeue();
            if (curr.isVertical) {
                if (rect.xmin() <= curr.p.x() && curr.p.x() <= rect.xmax()) {
                    nodesToSearch.enqueue(curr.lb);
                    nodesToSearch.enqueue(curr.rt);
                    if (rect.contains(curr.p)) range.add(curr.p);
                }
            } else {
                if (rect.ymin() <= curr.p.y() && curr.p.y() <= rect.ymax()) {
                    nodesToSearch.enqueue(curr.lb);
                    nodesToSearch.enqueue(curr.rt);
                    if (rect.contains(curr.p)) range.add(curr.p);
                }
            }
        }
        return range;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
//    public Point2D nearest(Point2D p) {
//
//    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        kdtree.draw();
    }

}
