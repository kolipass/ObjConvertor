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
    public static List<Point> makePlanes(Point center, int edgeCount, double radius) {
        if (center != null) {
            List<Point> figure = new ArrayList<>();
            for (int i = 1; i <= edgeCount; i++) {
                Point point = new Point();
                point.x = (float) (center.x + radius * Math.cos(getFi(center.x, center.y) + 2 * i * pi / edgeCount));
                point.y = (float) (center.x + radius * Math.sin(getFi(center.x, center.y) + 2 * i * pi / edgeCount));
                figure.add(point);
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

        List<Point> topPlane = null;
        List<Point> bottomPlane = null;


        for (Point point : points) {
            bottomPlane = topPlane;
            topPlane = makePlanes(point, edgeCount, radius);

            if (bottomPlane != null) {

                int lastPointIndex;

                for (int i = 0; i < edgeCount; i++) {
                    lastPointIndex = i != 0 ? i : edgeCount - 1;


                    Face face = new Face();
                    faces.add(face);

                    newPoints.add(bottomPlane.get(lastPointIndex));
                    face.poinstIndexs.add(newPoints.size());

                    newPoints.add(bottomPlane.get(i));
                    face.poinstIndexs.add(newPoints.size());

                    newPoints.add(topPlane.get(lastPointIndex));
                    face.poinstIndexs.add(newPoints.size());

                    newPoints.add(topPlane.get(lastPointIndex));
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
