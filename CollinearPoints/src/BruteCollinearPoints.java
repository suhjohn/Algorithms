import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

/****
 * Corner cases. Throw a java.lang.IllegalArgumentException if the argument to the constructor is null,
 * if any point in the array is null,
 * or if the argument to the constructor contains a repeated point.
 *****/

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lineSegmentArr;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        this.lineSegmentArr = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            Point a = points[i];
            for (int j = 0; j < points.length; j++) {
                Point b = points[j];
                double a_to_b = a.slopeTo(b);
                // Edge case: if point is itself
                if (a_to_b == Double.NEGATIVE_INFINITY || i == j) continue;
                for (int k = 0; k < points.length; k++) {
                    Point c = points[k];
                    double a_to_c = a.slopeTo(c);
                    if (a_to_c == Double.NEGATIVE_INFINITY || a_to_b != a_to_c || i == k || j == k) continue;
                    for (int l = 0; l < points.length; l++) {
                        Point d = points[l];
                        double a_to_d = a.slopeTo(d);
                        if (i == l || j == l || k == l) continue;
                        if (a_to_d != Double.NEGATIVE_INFINITY && (a_to_b == a_to_d && a_to_c == a_to_d)) {
                            Point[] currPoints = {a, b, c, d};
                            Arrays.sort(currPoints);
                            LineSegment newLineSegment = new LineSegment(currPoints[0], currPoints[3]);
                            boolean exist = false;
                            for (LineSegment lineSegment : this.lineSegmentArr) {
                                if (newLineSegment.toString().equals(lineSegment.toString())) exist = true;
                            }
                            if (!exist) this.lineSegmentArr.add(newLineSegment);
//                            if (!this.lineSegmentArr.contains(newLineSegment)) this.lineSegmentArr.add(newLineSegment);
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.lineSegmentArr.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lineSegments = new LineSegment[this.numberOfSegments()];
        return this.lineSegmentArr.toArray(lineSegments);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.println(collinear.numberOfSegments());
        StdDraw.show();
    }

}
