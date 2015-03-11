package mobi.tarantino.collection;

import mobi.tarantino.model.Point;
import mobi.tarantino.model.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Модель, описывающая фигуру
 */
public class Figure implements Iterable<Point> {
    public List<Point> points = new ArrayList<>();

    public Figure(List<Point> points) {
        this.points = points;
    }

    public Figure(Point[] points) {
        this(new ArrayList<Point>(Arrays.asList(points)));
    }

    public Figure() {

    }

    public void add(Point e) {
        if (points == null) {
            points = new ArrayList<>();
        }
        points.add(e);
    }

    @Override
    public Iterator<Point> iterator() {
        return points != null ? points.iterator() : null;
    }

    public int size() {
        return points != null ? points.size() : 0;
    }

    public Point get(int i) {
        return points != null ? points.get(i) : null;
    }

    @Override
    public String toString() {
        if (points == null || points.size() == 0)
            return "Null";
        else {
            String result = "\n";
            for (Point point : points) {
                result += point.toString() + "\n";
            }
            return result;
        }
    }

    public static Figure move(Figure figure, Vector vector) {
        Figure result = new Figure();
        for (Point point : figure.points) {
            result.add(new Point(point.x + vector.x, point.y + vector.y, point.z + vector.z));
        }
        return result;
    }
}
