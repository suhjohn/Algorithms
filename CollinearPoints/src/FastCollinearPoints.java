/***********
 * Given a point p, the following method determines whether p participates in a set of 4 or more collinear points.

 Think of p as the origin.
 For each other point q, determine the slope it makes with p.
 Sort the points according to the slopes they makes with p.
 Check if any 3 (or more) adjacent points in the sorted order
 have equal slopes with respect to p. If so, these points, together with p, are collinear.
 *
 *
 Performance requirement.
 The order of growth of the running time of your program should be n2 log n in the worst case
 and it should use space proportional to n plus the number of line segments returned.

 FastCollinearPoints should work properly even if the input has 5 or more collinear points.
 **********/


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lineSegmentArr;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        Point[] pointsCopy = points;
        this.lineSegmentArr = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            Arrays.sort(pointsCopy, p.slopeOrder());

            double[] slopes = new double[pointsCopy.length];
            for (int z = 0; z < pointsCopy.length; z++){
                slopes[z] = p.slopeTo(pointsCopy[z]);
            }

            Double prevSlope = null;
            Double currSlope = null;
            int count = 0;
            for (int j = 0; j < points.length; j++) {
                Point q = pointsCopy[j];
                currSlope = p.slopeTo(q);
                if (currSlope.equals(Double.NEGATIVE_INFINITY)) continue;
                if (!currSlope.equals(prevSlope)) {
                    if (count >= 3) {
                        // Get max point from p
                        Arrays.sort(pointsCopy, j - count, j);
                        Point max_q = pointsCopy[j-1];
                        // create line segment
                        LineSegment lineSeg = new LineSegment(p, max_q);
                        // 1. Duplicates 2. Sub Segments handling
                        this.lineSegmentArr.add(lineSeg);
                    }
                    count = 0;
                }
                count++;
                prevSlope = currSlope;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        System.out.println(collinear.numberOfSegments());
        StdDraw.show();
    }

}
