
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;


import java.util.ArrayList;

public class KdTree {
    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private final boolean isVertical;

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
            this.size++;
        } else {
            if (!this.contains(p)) {
                insert(this.root, this.root.rect, this.root.isVertical, p);
                this.size++;
            }
        }
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
        return !this.isEmpty() && p.compareTo(this.root.p) == 0 || this.contains(this.root, p);
    }

    /****
     * Recursive implementation of contains
     */
    private boolean contains(Node node, Point2D p) {
        if (node == null) return false;
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
            } else {
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BLACK);
            node.p.draw();

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
            if (curr == null) continue;
            if (curr.isVertical) {
                if (rect.xmin() <= curr.p.x() && curr.p.x() <= rect.xmax() &&
                        (rect.ymin() <= curr.rect.ymax() || rect.ymax() >= curr.rect.ymin())) {
                    nodesToSearch.enqueue(curr.lb);
                    nodesToSearch.enqueue(curr.rt);
                    if (rect.contains(curr.p)) range.add(curr.p);
                }
                if (curr.rect.intersects(rect)) {
                    if (curr.p.x() < rect.xmin()) nodesToSearch.enqueue(curr.rt);
                    else if (curr.p.x() > rect.xmax()) nodesToSearch.enqueue(curr.lb);
                }
            } else {
                if (rect.ymin() <= curr.p.y() && curr.p.y() <= rect.ymax() &&
                        (rect.xmin() <= curr.rect.xmax() || rect.xmax() >= curr.rect.xmin())) {
                    nodesToSearch.enqueue(curr.lb);
                    nodesToSearch.enqueue(curr.rt);
                    if (rect.contains(curr.p)) range.add(curr.p);
                }
                if (curr.rect.intersects(rect)) {
                    if (curr.p.y() < rect.ymin()) nodesToSearch.enqueue(curr.rt);
                    else if (curr.p.y() > rect.ymax()) nodesToSearch.enqueue(curr.lb);
                }
            }

        }
        return range;
    }

    /***
     *  Start at the root and recursively search in both subtrees using the following pruning rule:
     *
     *  if the closest point discovered so far is closer than the distance between the query point and the rectangle corresponding to a node,
     *  there is no need to explore that node (or its subtrees).
     *  That is, search a node only only if it might contain a point that is closer than the best one found so far.
     *
     *  The effectiveness of the pruning rule depends on quickly finding a nearby point.
     *  To do this, organize the recursive method so that
     *  when there are two possible subtrees to go down,
     *  you always choose the subtree that is on the same side of the splitting line as the query point as the first subtree to explore —
     *      the closest point found while exploring the first subtree may enable pruning of the second subtree.
     * */
    public Point2D nearest(Point2D p) {
        if (this.isEmpty()) return null;
        if (this.root == null) return null;

        Node nearest;
        if (p.x() < this.root.p.x()) {
            nearest = this.nearest(p, this.root.lb, this.root, p.distanceSquaredTo(this.root.p));
            return this.nearest(p, this.root.rt, nearest, p.distanceSquaredTo(nearest.p)).p;
        } else {
            nearest = this.nearest(p, this.root.rt, this.root, p.distanceSquaredTo(this.root.p));
            return this.nearest(p, this.root.lb, nearest, p.distanceSquaredTo(nearest.p)).p;
        }
    }

    private Node nearest(Point2D p, Node node, Node nearestNode, Double nearestDist) {
        if (node == null || node.rect.distanceSquaredTo(p) > nearestDist) return nearestNode;
        Double currDist = node.p.distanceSquaredTo(p);

        if (currDist < nearestDist) {
            nearestDist = currDist;
            nearestNode = node;
        }
        // If the min distance from point to rectangle is smaller than nearest, then we should look for more.
        // We will first look for the zone(x if vertical and y if horizontal) where the query p is, then the other zone.
        // Otherwise, return the node.
        int comp = this.compare(node, p);
        if (comp > 0) {
            nearestNode = this.nearest(p, node.lb, nearestNode, nearestDist);
            nearestDist = nearestNode.p.distanceSquaredTo(p);
            nearestNode = this.nearest(p, node.rt, nearestNode, nearestDist);
        } else {
            nearestNode = this.nearest(p, node.rt, nearestNode, nearestDist);
            nearestDist = nearestNode.p.distanceSquaredTo(p);
            nearestNode = this.nearest(p, node.lb, nearestNode, nearestDist);
        }

        return nearestNode;
    }

    private int compare(Node node, Point2D p) {
        if (node.isVertical) {
            return Double.compare(node.p.x(), p.x());
        } else {
            return Double.compare(node.p.y(), p.y());
        }
    }

    private Node findNearestNeighbor(Queue<Node> nodesToSearch, Point2D p, Node currNearestNeighbor) {
        double currNearestDistance = currNearestNeighbor.p.distanceSquaredTo(p);
        while (!nodesToSearch.isEmpty()) {
            Node curr = nodesToSearch.dequeue();
            double currDistance = curr.p.distanceSquaredTo(p);
            // Update Nearest
            if (currDistance < currNearestDistance) {
                currNearestDistance = currDistance;
                currNearestNeighbor = curr;
            }
            if (this.canHaveNearerPoint(curr.lb, p, currNearestDistance)) nodesToSearch.enqueue(curr.lb);
            if (this.canHaveNearerPoint(curr.rt, p, currNearestDistance)) nodesToSearch.enqueue(curr.rt);
        }
        return currNearestNeighbor;
    }

    private boolean canHaveNearerPoint(Node node, Point2D p, Double currNearestDistance) {
        return node != null && node.rect.distanceSquaredTo(p) < currNearestDistance;
    }

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
