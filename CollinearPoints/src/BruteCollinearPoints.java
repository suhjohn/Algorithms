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
        validatePoints(points);
        Arrays.sort(points);

        ArrayList<Point[]> lineSegmentEndpointArr = new ArrayList<>();
        this.lineSegmentArr = new ArrayList<>();
        for (int i = 0; i < points.length - 3; i++) {
            Point a = points[i];
            for (int j = i + 1; j < points.length - 2; j++) {
                Point b = points[j];
                double a_to_b = a.slopeTo(b);
                for (int k = j + 1; k < points.length - 1; k++) {
                    Point c = points[k];
                    double a_to_c = a.slopeTo(c);
                    if (a_to_b != a_to_c) continue; // small optimization
                    for (int l = k + 1; l < points.length; l++) {
                        Point d = points[l];
                        double a_to_d = a.slopeTo(d);
                        if (a_to_b == a_to_d) {
                            LineSegment newLineSegment = new LineSegment(a, d);
                            this.lineSegmentArr.add(newLineSegment);

//                            boolean duplicate = hasDuplicate(lineSegmentEndpointArr, a, d);
//                            if (!duplicate) {
//                                Point[] newLineSegmentEndpoints = {a, d};
//                                LineSegment newLineSegment = new LineSegment(a, d);
//                                lineSegmentEndpointArr.add(newLineSegmentEndpoints);
//                                this.lineSegmentArr.add(newLineSegment);
//                            }
                        }
                    }
                }
            }
        }

    }

    private boolean hasDuplicate(ArrayList<Point[]> lineSegmentEndpointArr, Point p, Point q) {
        for (Point[] endpoints : lineSegmentEndpointArr) {
            if (endpoints[0].compareTo(p) == 0 && endpoints[1].compareTo(q) == 0) return true;
        }
        return false;
    }


    private void validatePoints(Point[] points) {
        // check if point is null
        // check if null
        // check if same point
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            Point curr = points[i];
            for (int j = i + 1; j < points.length; j++) {
                if (points[j].toString().equals(curr.toString())) throw new IllegalArgumentException();
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
