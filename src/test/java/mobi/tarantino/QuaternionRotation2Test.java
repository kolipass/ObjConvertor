package mobi.tarantino;

import net.ilyi.QuaternionRotation2;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by kolipass on 04.03.15.
 */
public class QuaternionRotation2Test {

    @Test
    public void testLineContainsPoint() {
        float radius = 1f;
        int edgeCount = 3;

        Point zero = new Point(0, 0, 0);
        Point zero1 = new Point(0, 0, 1);
        Point start = new Point(-1, -1, 0);
        Point end = new Point(1, 1, 1);
        Point end2 = new Point(2, 0, 1);
        Vector zeroUnitVector = new Vector(zero, zero1);
        Vector vector = new Vector(start, end);

        Vector unitVector =
                Vector.makeUnitVector(
//                        zeroUnitVector
                        Vector.mul(vector, zeroUnitVector)
                );

        float angle1 = (float) Vector.angle(zeroUnitVector, new Vector(start, end));
        Figure defaultPlane = new Figure(QuaternionRotation2.rotate(Main.makePlanes(edgeCount, radius), QuaternionRotation2.getUnitQuaternion(unitVector, angle1)));

        angle1 = (float) Vector.angle(zeroUnitVector, new Vector(start, end));
//        drawFigure(defaultPlane);
        Figure bottom = Figure.move(defaultPlane, new Vector(zero, start));

        assertTrue(Point.plateContainsPoint(start, bottom.points.stream().toArray(Point[]::new)));

        Figure top = Figure.move(defaultPlane, new Vector(zero, end));


        assertTrue(Point.plateContainsPoint(end, top.points.stream().toArray(Point[]::new)));
    }

}