package mobi.tarantino;

import bsim.geometry.BSimSphereMesh;
import mobi.tarantino.collection.BSimToObjFigure;
import mobi.tarantino.collection.Figure;
import mobi.tarantino.collection.ObjFigure;
import mobi.tarantino.model.Point;
import mobi.tarantino.model.Vector;
import net.ilyi.Quaternion;

import javax.vecmath.Vector3d;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kolipass on 11.03.15.
 */
public abstract class PlateUtils {
    enum NODE_TYPE {ISO_SPHERE, FOLLOW, NONE}

    public static final double pi = Math.acos(-1);

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

    /**
     * Строит цилиндр между точками
     *
     * @param points    набор точек, которые последовательно обходятся
     * @param edgeCount колличество углов фигуры
     * @param radius    радиус окружности описанной вокруг фигуры
     * @return м
     */
    public static ObjFigure cylindrate(List<Point> points, int edgeCount, float radius, NODE_TYPE nodeType) {
        ObjFigure figure = new ObjFigure();

        Point lastPoint = null;
        Figure lastFigure = null;
        Figure currentFigure = null;

        for (Point currentPoint : points) {
            if (lastPoint != null) {

                if (edgeCount >= 3) currentFigure = makePlates(lastPoint, currentPoint, edgeCount, radius);
                else currentFigure = shift(Arrays.asList(lastPoint, currentPoint), radius);

                figure.addFigure(currentFigure);
                makeFollow(radius, nodeType, figure, lastFigure, currentFigure, currentPoint);
            }

            lastPoint = currentPoint;
            lastFigure = currentFigure;
        }

        return figure;
    }

    private static void makeFollow(float radius, NODE_TYPE nodeType, ObjFigure figure, Figure lastFigure, Figure currentFigure, Point currentPoint) {
        if (lastFigure != null)

            switch (nodeType) {
                case ISO_SPHERE:
                    Figure node =
                            BSimToObjFigure.BSimToObjFigure(
                                    new BSimSphereMesh(
                                            new Vector3d(currentPoint.x, currentPoint.y, currentPoint.z), radius, 1));

                    figure.addFigure(node);
                    break;
                case FOLLOW:
                    node = makeFollow(lastFigure, currentFigure);
                    figure.addFigure(node);
                    break;
                case NONE:
                    break;
            }
    }

    private static Figure makeFollow(Figure lastFigure, Figure currentFigure) {
        Figure figure = new Figure();

        int size = lastFigure.points.size();

        int lastPointId = size - 1;
        int currentPointId = 0;

        for (int i = 0; i < size; i++) {

            figure.add(lastFigure.get(lastPointId));
            figure.add(currentFigure.get(lastPointId));


            figure.add(lastFigure.get(currentPointId));
            figure.add(currentFigure.get(currentPointId));

            currentPointId = lastPointId;
        }
        return figure;
    }


    public static ObjFigure makePlates(Point start, Point end, int edgeCount, float radius) {
        Point zero = new Point(0, 0, 0);
        Point zero1 = new Point(0, 0, 1);
        Vector zeroUnitVector = new Vector(zero, zero1);
        Vector vector = new Vector(start, end);

        Vector unitVector =
                Vector.makeUnitVector(
                        Vector.mul(vector, zeroUnitVector)
                );

        float angle1 = (float) Vector.angle(zeroUnitVector, new Vector(start, end));
        double phi = 180.0 * angle1 / Math.PI;

        Figure defaultPlane = makeDefaultPlane(angle1, radius, edgeCount, unitVector);

        Figure bottom = Figure.move(defaultPlane, new Vector(zero, start));
        Figure top = Figure.move(defaultPlane, new Vector(zero, end));

        ObjFigure result = new ObjFigure();
        FigureFactory.makeFace(top, bottom).forEach(result::addFigure);
        return result;
    }

    /**
     * Фабричный метод рисует вписанную в окружность правильную фигуру,повернутую на угол angle понаправлению вектора unitVector
     * Если угл равен нулю или единичный вектор не задан, то поворота не будет
     *
     * @param radius     радиус
     * @param edgeCount  колличество углов
     * @param unitVector единичный вектор
     * @return
     */
    public static Figure makeDefaultPlane(float angle, float radius, int edgeCount, Vector unitVector) {
        Point[] plane = makePlanes(edgeCount, radius);
        if (angle != 0 && unitVector != null && !unitVector.equals(Vector.NaN)) {
            plane = rotate(plane, getUnitQuaternion(unitVector, angle));
        }
        return new Figure(plane);
    }

    public static Quaternion getUnitQuaternion(Vector vector, double phi) {
        double cosphi = Math.cos(phi / 2.0);
        double sinphi = Math.sin(phi / 2.0);
        return new Quaternion(cosphi, vector.x * sinphi, vector.y * sinphi, vector.z * sinphi).unit();
    }

    public static Point[] rotate(Point[] trianglePoints, Quaternion q) {
        Point[] figure = new Point[trianglePoints.length];
        for (int i = 0; i < trianglePoints.length; i++) {
            Quaternion p = new Quaternion(trianglePoints[i], 0.0);
            p = q.mul(p.mul(q.inverse()));
            figure[i] = new Point(p.i, p.j, p.k);
        }
        return figure;
    }

    /**
     * "Растягивает" отрезоки в плоскость добавлением точек.смещенных относительнозаданной на radius. создается как положительное смещение, таки отрицательное
     *
     * @param points набор оригинальных точек (A,B,C...)
     * @param radius радиус смещения
     * @return набор точек в порядке  (A+r,B+r,B-r,A-r, B+r,C+r,C-r,B-r ...)
     */
    public static ObjFigure shift(List<Point> points, float radius) {
        ObjFigure figure = new ObjFigure();

        Point lastPoint;
        Point currentPoint = null;

        for (Point point : points) {
            lastPoint = currentPoint;
            currentPoint = point;

            if (lastPoint != null) {

                figure.add(lastPoint.shift(radius));
                figure.add(currentPoint.shift(radius));

                figure.add(currentPoint.shift(-2 * radius));
                figure.add(lastPoint.shift(-2 * radius));
            }
        }
        return figure;
    }

    /**
     * Вычисление определителя методом Краута за O (N3)
     * http://e-maxx.ru/algo/determinant_crout
     *
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
