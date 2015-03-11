package mobi.tarantino;

import mobi.tarantino.model.Point;
import net.ilyi.Quaternion;

/**
 * Created by kolipass on 02.02.15.
 */
public class QuaternionFactory {
    public static Quaternion quaternion(double r, Point point) {
        return new Quaternion(r, point.x, point.y, point.z);
    }
}
