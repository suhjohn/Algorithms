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
    private ArrayList<LineSegment> lineSegmentList = new ArrayList<>();


    //     finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        validatePoints(points);
        Arrays.sort(points);

        Point[] pointsCopy = points.clone();
        for (int i = 0; i < points.length; i++) {
            Arrays.sort(pointsCopy);
            Point p = pointsCopy[i];
            Arrays.sort(pointsCopy, p.slopeOrder());

            // Add Line Segment Points //
            Double prevSlope = null;
            Double currSlope = null;
            int count = 0;


            for (int j = 0; j < points.length; j++) {
                Point q = pointsCopy[j];
                currSlope = p.slopeTo(q);

                // If the slope changed or we're at the last point, check if we've seen more than 3.
                if (prevSlope != null && (!prevSlope.equals(currSlope))) {
                    if (count >= 3 && p.compareTo(pointsCopy[j - count]) < 0) {
                        lineSegmentList.add(new LineSegment(p, pointsCopy[j - 1]));
                    }
                    count = 0;
                }
                count++;
                prevSlope = currSlope;
            }

            // Edge case for when loop with the last set of elements having the same slope
            if (count >= 3 & p.compareTo(pointsCopy[pointsCopy.length - count]) < 0)
                lineSegmentList.add(new LineSegment(p, pointsCopy[pointsCopy.length - 1]));
        }

    }

    /**
     * validates the points from the constructor.
     * Checks if the argument points itself isn ull, if any point in points is null, or duplicates exist.
     *
     * @param points array of points to validate
     */
    private void validatePoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException(); // check if null
        // check if point is null
        // check if null
        // check if same point
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            if (points[i].compareTo(points[i + 1]) == 0) {

                throw new IllegalArgumentException();
            }
        }
    }


    // the number of line segments
    public int numberOfSegments() {
        return this.lineSegmentList.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lineSegments = new LineSegment[this.numberOfSegments()];
        return this.lineSegmentList.toArray(lineSegments);
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
