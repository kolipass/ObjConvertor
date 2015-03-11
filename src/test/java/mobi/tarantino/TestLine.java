package mobi.tarantino;

import mobi.tarantino.model.Point;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by kolipass on 04.03.15.
 */
public class TestLine {
    @Test
    public void testLineContainsPoint() {
        assertTrue(Point.lineContainsPoint(new Point(0, 0, 0), new Point(1, 1, 1), new Point(2, 2, 2)));
        assertFalse(Point.lineContainsPoint(new Point(0, 0, 1), new Point(1, 1, 1), new Point(2, 2, 2)));
    }

    @Test
    public void plateContainsPoint() {
        assertTrue(Point.plateContainsPoint(new Point(0, 0, 0), new Point[]{new Point(1, 1, 1), new Point(2, 2, 2)}));
        assertTrue(Point.plateContainsPoint(new Point(0, 0, 1), new Point[]{new Point(1, 1, 1), new Point(2, 2, 2)}));
        assertFalse(Point.plateContainsPoint(new Point(-1, -1, -1), new Point[]
                {new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)}));
    }

}
