package mobi.tarantino.model;

import mobi.tarantino.PlateUtils;

import java.math.BigDecimal;

/**
 * geometric vertices (v)
 */
public class Point extends AbstractModel {
    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;
    public float x;
    public float y;
    public float z;

    public Point() {
    }

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(double x, double y, double z) {
        this((float) x, (float) y, (float) z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;

        Point point = (Point) o;

        return Float.compare(point.x, x) == 0
                && Float.compare(point.y, y) == 0
                && Float.compare(point.z, z) == 0;

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "v " + x + " " + y + " " + z;
    }

    public Point shift(float shiftValue) {
        Point point = new Point();
        point.x = this.x + shiftValue;
        point.y = this.y + shiftValue;
        point.z = this.z + shiftValue;
        return point;
    }

    /**
     * Проверяет принадлежность точки к плоскости
     *
     * @param point точка, которую надо проверить
     * @param plate набор точек плоскости, хватит 3-х. если мешьше, то всегда будет true
     * @return
     */
    public static boolean plateContainsPoint(Point point, Point[] plate) {
        if (plate.length <= 2) {
            return true;
        }
        if (lineContainsPoint(plate[0], plate[1], plate[2])) {
            return true;
        }
        BigDecimal[][] matrix = {
                {new BigDecimal(point.x), new BigDecimal(point.y), new BigDecimal(point.z), BigDecimal.ONE},
                {new BigDecimal(plate[0].x), new BigDecimal(plate[0].y), new BigDecimal(plate[0].z), BigDecimal.ONE},
                {new BigDecimal(plate[1].x), new BigDecimal(plate[1].y), new BigDecimal(plate[1].z), BigDecimal.ONE},
                {new BigDecimal(plate[2].x), new BigDecimal(plate[2].y), new BigDecimal(plate[2].z), BigDecimal.ONE},
        };


        return PlateUtils.det(matrix).equals(BigDecimal.ZERO);

    }

    /**
     * Проверяет принадлежность точки к прямой
     *
     * @param point точка, которую надо проверить
     * @param a,    b набор точек плоскости, хватит 2-х. если мешьше, то всегда будет true
     * @return
     */
    public static boolean lineContainsPoint(Point point, Point a, Point b) {
        if (point.equals(a) || point.equals(a) || a.equals(b)) {
            return true;
        } else {
            float first = (point.x - a.x) / (b.x - a.x);
            float second = (point.y - a.y) / (b.y - a.y);
            float third = (point.z - a.z) / (b.z - a.z);

            return first == second && first == third;
        }

    }
}
