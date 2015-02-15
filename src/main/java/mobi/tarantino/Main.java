package mobi.tarantino;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final double pi = Math.acos(-1);

    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            for (String filepath : args) {
                File source = new File(filepath);
                if (source.exists()) {
                    List<AbstractModel> pointList = new ArrayList<>(cylindrate(new TempFileReader(source).read(), 3, 10));
                    new TempFileWriter<>(filepath + ".obj", pointList).write();
                } else System.out.println("file not found");
            }
        }
        System.out.println("finish");
    }

    public static Point[] makePlanes(Point center, int edgeCount, double radius) {
        if (edgeCount < 3)
            throw new ArithmeticException("Вершин нужно не меньше 3-х");

        if (center != null) {
            Point[] figure = new Point[edgeCount];
            for (int i = 0; i < edgeCount; i++) {
                Point point = new Point();
                point.x = (float) (center.x + radius * Math.cos(pi/2 + 2 * i * pi / edgeCount));
                point.y = (float) (center.y + radius * Math.sin(pi/2 + 2 * i * pi / edgeCount));
                figure[i] = point;
            }
            return figure;
        } else
            return null;
    }

    private static double getFi(double x, double y) {
        if (x > 0 && y >= 0) {
            return Math.atan(y / x);
        } else if (x > 0 && y < 0) {
            return Math.atan(y / x + 2 * pi);
        } else if (x < 0) {
            return Math.atan(y / x + pi);
        } else if (x == 0 && y > 0) {
            return pi / 2;
        } else if (x == 0 && y < 0) {
            return -pi / 2;
        } else {
            return 0;
//            throw new ArithmeticException("NaN");
        }
    }

    public static List<AbstractModel> cylindrate(List<Point> points, int edgeCount, double radius) {
        List<Point> newPoints = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        Point[] topPlane = null;
        Point[] bottomPlane = null;


        for (Point point : points) {
            bottomPlane = topPlane;
            topPlane = makePlanes(point, edgeCount, radius);

            if (bottomPlane != null) {

                int lastPointIndex;

                for (int i = 0; i < edgeCount; i++) {
                    lastPointIndex = i != 0 ? i : edgeCount - 1;


                    Face face = new Face();
                    faces.add(face);

                    newPoints.add(bottomPlane[lastPointIndex]);
                    face.poinstIndexs.add(newPoints.size());

                    newPoints.add(bottomPlane[i]);
                    face.poinstIndexs.add(newPoints.size());

                    newPoints.add(topPlane[lastPointIndex]);
                    face.poinstIndexs.add(newPoints.size());

                    newPoints.add(topPlane[lastPointIndex]);
                    face.poinstIndexs.add(newPoints.size());
                }

            }
        }

        List<AbstractModel> models = new ArrayList<>();
        models.addAll(newPoints);
        models.addAll(faces);

        return models;
    }

    public static List<AbstractModel> shift(List<Point> points) {
        ArrayList<Point> shiftedList = new ArrayList<>();
        ArrayList<Face> shiftedFaces = new ArrayList<>();

        Point lastPoint = null;
        Point currentPoint = null;


        for (Point point : points) {
            lastPoint = currentPoint;
            currentPoint = point;

            if (lastPoint != null) {
                Face face = new Face();
                shiftedFaces.add(face);

                shiftedList.add(lastPoint);
                face.poinstIndexs.add(shiftedList.size());
                shiftedList.add(lastPoint.shift(0.01f));
                face.poinstIndexs.add(shiftedList.size());

                shiftedList.add(currentPoint);
                face.poinstIndexs.add(shiftedList.size());
                shiftedList.add(currentPoint.shift(0.01f));
                face.poinstIndexs.add(shiftedList.size());

            }
        }

        List<AbstractModel> models = new ArrayList<>();
        models.addAll(shiftedList);
        models.addAll(shiftedFaces);

        return models;
    }

}
