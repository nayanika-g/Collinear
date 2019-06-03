import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private final Point[] pointSet;
    // private int numberOfSegments;
    private final ArrayList<LineSegment> lines;
    // private LineSegment[] segments;
    private boolean firstRun;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException();
        }
        if (points[points.length - 1] == null) {
            throw new java.lang.IllegalArgumentException();
        }
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) {
                throw new java.lang.IllegalArgumentException();
            }
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) {
                    throw new java.lang.IllegalArgumentException();
                }
                if (points[i].compareTo(points[j]) == 0) {
                    throw new java.lang.IllegalArgumentException();
                }
            }
        }
        // numberOfSegments = 0;
        pointSet = new Point[points.length];
        int i = 0;
        for (Point p : points) {
            pointSet[i++] = p;
        }
        sortByNaturalOrder(pointSet);
        // segments = new LineSegment[2 * pointSet.length];
        lines = new ArrayList<>();
        firstRun = true;
    }

    public int numberOfSegments() {
        return segments().length;
    }

    public LineSegment[] segments() {

        if (firstRun) {

            Point[] compareSet = new Point[pointSet.length - 1];
            for (int i = 0; i < pointSet.length; i++) {

                Point origin = pointSet[i];

                //copy all points in pointSet to compareSet
                int j = 0;
                for (int k = 0; k < pointSet.length; k++) {
                    if (pointSet[k] != origin) {
                        compareSet[j] = pointSet[k];
                        j++;
                    }
                }

                Arrays.sort(compareSet, origin.slopeOrder());
                // boolean[] slopes = sort(compareSet, origin.slopeOrder());

                // System.out.println("Origin = " + origin);
                // sort(compareSet, origin.slopeOrder());
                // printArray(compareSet);

                // find the segments
                for (LineSegment line : findSegments(origin, compareSet)) {
                    lines.add(line);
                }
            }
            firstRun = false;
        }
        // return the segments in array
        int n = 0;
        LineSegment[] segments = new LineSegment[lines.size()];
        for (LineSegment line : lines) {
            segments[n++] = line;
        }
        LineSegment[] copy = new LineSegment[lines.size()];
        System.arraycopy(segments, 0, copy, 0, lines.size());
        return copy;
    }

    /*private ArrayList<LineSegment> findSegments(Point origin, Point[] arr, boolean[] slopes) {

        ArrayList<LineSegment> segmentList = new ArrayList<>();

        for (int i = 0; i <= arr.length - 3; i++) {
            Point start = arr[i];
            Point end = arr[i];
            int count = 1;
            if (slopes[i] == false && slopes[i + 1] == true) {
                count++;
                for (int j = i + 1; j < arr.length; j++) {
                    if (slopes[j] == true) {
                        count++;
                        if (start.compareTo(arr[j]) < 0) {
                            start = arr[j];
                        }
                        if (end.compareTo(arr[j]) > 0) {
                            end = arr[j];
                        }
                        // end = arr[j];
                    } else {
                        break;
                    }
                }
            }
            if (count >= 4) {
                if (origin.compareTo(start) > 0) {
                    start = origin;
                }
                else if (origin.compareTo(end) < 0) {
                    end = origin;
                }
                if (origin == start) {
                    segmentList.add(new LineSegment(end, start));
                }
                i += count - 2;
            }
        }

        return segmentList;

    }*/

    private ArrayList<LineSegment> findSegments(Point origin, Point[] arr) {

        ArrayList<LineSegment> segmentList = new ArrayList<>();

        for (int i = 0; i <= arr.length - 3; i++) {
            double m = origin.slopeTo(arr[i]);
            Point start = arr[i];
            Point end = arr[i];
            int count = 2;  //contains points origin and arr[i]
            for (int j = i + 1; j < arr.length; j++) {
                if (origin.slopeTo(arr[j]) == m) {
                    count++;
                    end = arr[j];
                }
            }

            if (count >= 4) {
                if (origin.compareTo(start) > 0) {
                    start = origin;
                }
                if (origin.compareTo(end) < 0) {
                    end = origin;
                }
                if (origin == start) {
                    segmentList.add(new LineSegment(end, start));
                }
                i += count;
            }
        }
        return segmentList;
    }

    private static void sortByNaturalOrder(Point[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j].compareTo(arr[j - 1]) > 0) {
                    exch(arr, j, j - 1);
                }
            }
        }
    }

    /*private static void sort(Point[] arr, Comparator c) {
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (less(c, arr[j], arr[j - 1])) {
                    exch(arr, j, j - 1);
                }
            }
        }
    }*/

    /*private static boolean[] sort(Point[] arr, Comparator<Point> c) {
        boolean[] slopes = new boolean[arr.length];
        *//*for (int i = 0; i < slopes.length; i++) {
            slopes[i] = true;
        }*//*

        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                int v = compareValue(c, arr[j], arr[j - 1]);
                if (v < 0) {
                    exch(arr, j, j - 1);
                    exchBools(slopes, j, j - 1);
                }
                if (v == 0) {
                    slopes[j] = true;
                }
            }
        }
        return slopes;
    }*/

    /*private static boolean less(Comparator c, Point a, Point b) {
        return (c.compare(a, b) < 0);
    }

    private static boolean notEqual(Comparator c, Point a, Point b) {
        return (c.compare(a, b) != 0);
    }*/

    private static int compareValue(Comparator<Point> c, Point a, Point b) {
        return c.compare(a, b);
    }

    private static void exch(Object[] a, int i, int j) {
        Object temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static void exchBools(boolean[] b, int i, int j) {
        boolean temp = b[i];
        b[i] = b[j];
        b[j] = temp;
    }

    /*private void printArray(Object[] set) {
        for (Object item : set) {
            System.out.println(item);
        }
    }

    private void printBoolArray(boolean[] set) {
        for (boolean b : set) {
            System.out.println(b);
        }
    }*/

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

        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw resulting line segments

        FastCollinearPoints fcp = new FastCollinearPoints(points);
        for (LineSegment line : fcp.segments()) {
            StdOut.println(line);
            line.draw();
        }
        StdDraw.show();
        System.out.println("Number of segments: " + fcp.numberOfSegments());


    }
}
