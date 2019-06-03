import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BruteCollinearPoints {

    private final Point[] pointSet;
    // private final int numSegments;
    private final ArrayList<Object[]> dataList;
    private final LineSegment[] segments;
    private boolean firstRun;


    public BruteCollinearPoints(Point[] points) {
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
        firstRun = true;
        // numSegments = 0;
        pointSet = new Point[points.length];
        int i = 0;
        for (Point p : points) {
            pointSet[i++] = p;
        }
        sortByNaturalOrder(pointSet);
        dataList = new ArrayList<>();
        segments = new LineSegment[pointSet.length];
    }

    public int numberOfSegments() {
        return segments().length;
    }

    public LineSegment[] segments() {

        if (firstRun) {
            boolean firstTime = true;

            for (Point[] arr : combinations(pointSet)) {

                double m = arr[0].slopeTo(arr[1]);
                if (m == arr[0].slopeTo(arr[2])) {
                    if (m == arr[0].slopeTo(arr[3])) {
                        if (firstTime) {
                            addData(dataList, arr[0], arr[3], m);
                            firstTime = false;
                        } else {
                            Object[] item = add(arr[0], arr[3], m, dataList);
                            if (item != null) {
                                if (item.length > 0) {
                                    dataList.remove(item);
                                }
                            } else {
                                addData(dataList, arr[0], arr[3], m);
                            }
                        }
                    }
                }
            }
            // numSegments = dataList.size();
            // segments = new LineSegment[dataList.size()];
            int i = 0;
            for (Object[] item : dataList) {
                segments[i++] = new LineSegment((Point) item[0], (Point) item[1]);
            }
            firstRun = false;
        }
        LineSegment[] copy = new LineSegment[dataList.size()];
        System.arraycopy(segments, 0, copy, 0, dataList.size());
        return copy;
    }


    private void printList(ArrayList<Object[]> list) {
        for (Object[] arr : list) {
            for (Object item : arr) {
                System.out.print(item);
            }
            System.out.println();
        }
    }

    private ArrayList<Point[]> combinations(Point[] arr) {
        ArrayList<Point[]> superList = new ArrayList<>();
        int m = 1;

        while (m <= arr.length - 3) {
            int count = 1;
            for (int i = m; i <= arr.length - 3; i++) {
                int count2 = 1;
                for (int j = i + 1; j < arr.length - 1; j++) {
                    for (int k = j + 1; k < arr.length; k++) {
                        Point[] list = new Point[4];
                        list[0] = (arr[i - count]);
                        list[1] = (arr[j - count2]);
                        list[2] = (arr[j]);
                        list[3] = (arr[k]);
                        superList.add(list);
                    }
                    count2++;
                }
                count++;
            }
            m++;
        }
        return superList;
    }

    private void addData(ArrayList<Object[]> dataList, Point start, Point end, double m) {
        Object[] data = new Object[3];
        data[0] = start;
        data[1] = end;
        data[2] = m;
        dataList.add(data);
    }

    private static Object[] add(Point start, Point end, double m, ArrayList<Object[]> list) {
        Object[] item = null;
        for (Object[] arr : list) {
            if ((arr[0]) == start && (arr[1] == end)) { //same segment
                item = new Object[0];
            } else if ((arr[0] == start) && (arr[2].equals(m))) {
                Point p = (Point) arr[1];
                if (p.compareTo(end) < 0) {     //longer segment
                    item = arr;
                } else {
                    item = new Object[0];
                }
            } else if (arr[1] == end && (arr[2].equals(m))) {
                Point p = (Point) arr[0];
                if (p.compareTo(start) > 0) {   //longer segment
                    item = arr;
                } else {
                    item = new Object[0];
                }
            }
        }
        return item;
    }

    private static void sortByNaturalOrder(Point[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j].compareTo(arr[j - 1]) < 0) {
                    exchange(arr, j, j - 1);
                }
            }
        }
    }

    private static void exchange(Point[] arr, int m, int n) {
        Point temp = arr[n];
        arr[n] = arr[m];
        arr[m] = temp;
    }

    public static void main(String[] args) {

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

        BruteCollinearPoints collinear = new BruteCollinearPoints(points);


        for (LineSegment line : collinear.segments()) {
            StdOut.println(line);
            line.draw();
        }
        StdDraw.show();

        System.out.println("Number of Segments = " + collinear.numberOfSegments());
    }
}
