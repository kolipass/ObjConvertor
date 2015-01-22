package mobi.tarantino;

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
}
