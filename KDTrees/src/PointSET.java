import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> pointSet = new TreeSet<>();

    // construct an empty set of points
    public PointSET() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (!this.pointSet.contains(p)) {
            this.pointSet.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return this.pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : this.pointSet) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        // Create an Iterable object
        ArrayList<Point2D> points = new ArrayList<>();

        // Iterate over the elements in the SET and if it is inside rect, add to the Iterable object
        for (Point2D point : this.pointSet) {
            if (rect.contains(point)) points.add(point);
        }
        // Return the Iterable
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        // Return null if set empty
        if (this.pointSet.isEmpty()) return null;

        // Calculate the first element's distance to p
        Point2D minPoint = this.pointSet.first();
        double minDistance = minPoint.distanceSquaredTo(p);

        // Iterate over the elements in the SET
        // Compare the current distance and update min
        for (Point2D currPoint : this.pointSet) {
            double currDistance = currPoint.distanceSquaredTo(p);
            if (currDistance < minDistance) {
                minPoint = currPoint;
                minDistance = currDistance;
            }
        }
        // Return the point with the min distance
        return minPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
