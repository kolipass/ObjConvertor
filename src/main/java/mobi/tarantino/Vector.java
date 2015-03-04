package mobi.tarantino;

/**
 * Created by kolipass on 01.02.15.
 */
public class Vector extends Point {

    public Vector() {
    }

    public Vector(Point start, Point end) {
        this();
        x = end.x - start.x;
        y = end.y - start.y;
        z = end.z - start.z;
    }

    public static Vector makeUnitVector(Vector vector) {
        Vector unitVector = new Vector();

        double length = vector.length();

        unitVector.x = (float) (vector.x / length);
        unitVector.y = (float) (vector.y / length);
        unitVector.z = (float) (vector.z / length);


        return unitVector;
    }

    public static Vector mul(Vector a, Vector b) {
        Vector vector = new Vector();


        vector.x = a.y * b.z - a.z * b.y;
        vector.y = a.z * b.x - a.x * b.z;
        vector.z = a.x * b.y - a.y * b.x;


        return vector;
    }

    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public double mul(Vector vector) {
        return x * vector.x + y * vector.y + z * vector.z;
    }

    public static double angle(Vector a, Vector b) {
        return Math.acos(a.mul(b) / (a.length() * b.length()));
    }

}
