package mobi.tarantino;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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

    /**
     * Создает многоугольник в плоскости X0Y (z=0), вписанный в окружность.
     *
     * @param center    координаты центра
     * @param edgeCount колличество вершин
     * @param radius    радиус описанной окружности
     * @return массив вершин
     */
    public static Point[] makePlanes(Point center, int edgeCount, double radius) {
        if (edgeCount < 3)
            throw new ArithmeticException("Вершин нужно не меньше 3-х");

        if (center != null) {
            Point[] figure = new Point[edgeCount];
            for (int i = 0; i < edgeCount; i++) {
                Point point = new Point();
                point.x = (float) (center.x + radius * Math.cos(pi / 2 + 2 * i * pi / edgeCount));
                point.y = (float) (center.y + radius * Math.sin(pi / 2 + 2 * i * pi / edgeCount));
                point.z = center.z;
                figure[i] = point;
            }
            return figure;
        } else
            return null;
    }

    public static Point[] makePlanes(int edgeCount, double radius) {
        return makePlanes(new Point(0, 0, 0), edgeCount, radius);
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
        return cylindrate(points, edgeCount, radius, true);
    }

    public static List<AbstractModel> cylindrate(List<Point> points, int edgeCount, double radius, boolean addFaces) {
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
        if (addFaces)
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

    /**
     * Вычисление определителя методом Краута за O (N3)
     * http://e-maxx.ru/algo/determinant_crout
     * @param a
     * @return
     */
    public static BigDecimal det(BigDecimal a[][]) {
        int n = a.length;
        try {

            for (int i = 0; i < n; i++) {
                boolean nonzero = false;
                for (int j = 0; j < n; j++)
                    if (a[i][j].compareTo(BigDecimal.ZERO) > 0)
                        nonzero = true;
                if (!nonzero)
                    return BigDecimal.ZERO;
            }

            BigDecimal scaling[] = new BigDecimal[n];
            for (int i = 0; i < n; i++) {
                BigDecimal big = BigDecimal.ZERO;
                for (int j = 0; j < n; j++)
                    if (a[i][j].abs().compareTo(big) > 0)
                        big = a[i][j].abs();
                scaling[i] = BigDecimal.ONE.divide
                        (big, 100, BigDecimal.ROUND_HALF_EVEN);
            }

            int sign = 1;

            for (int j = 0; j < n; j++) {

                for (int i = 0; i < j; i++) {
                    BigDecimal sum = a[i][j];
                    for (int k = 0; k < i; k++)
                        sum = sum.subtract(a[i][k].multiply(a[k][j]));
                    a[i][j] = sum;
                }

                BigDecimal big = BigDecimal.ZERO;
                int imax = -1;
                for (int i = j; i < n; i++) {
                    BigDecimal sum = a[i][j];
                    for (int k = 0; k < j; k++)
                        sum = sum.subtract(a[i][k].multiply(a[k][j]));
                    a[i][j] = sum;
                    BigDecimal cur = sum.abs();
                    cur = cur.multiply(scaling[i]);
                    if (cur.compareTo(big) >= 0) {
                        big = cur;
                        imax = i;
                    }
                }

                if (j != imax) {

                    for (int k = 0; k < n; k++) {
                        BigDecimal t = a[j][k];
                        a[j][k] = a[imax][k];
                        a[imax][k] = t;
                    }

                    BigDecimal t = scaling[imax];
                    scaling[imax] = scaling[j];
                    scaling[j] = t;

                    sign = -sign;
                }

                if (j != n - 1)
                    for (int i = j + 1; i < n; i++)
                        a[i][j] = a[i][j].divide
                                (a[j][j], 100, BigDecimal.ROUND_HALF_EVEN);

            }

            BigDecimal result = new BigDecimal(1);
            if (sign == -1)
                result = result.negate();
            for (int i = 0; i < n; i++)
                result = result.multiply(a[i][i]);

            return result.divide
                    (BigDecimal.valueOf(1), 0, BigDecimal.ROUND_HALF_EVEN);

        } catch (Exception e) {
            return BigDecimal.ZERO;
        }

    }

}
