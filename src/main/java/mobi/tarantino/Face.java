package mobi.tarantino;

import java.util.ArrayList;
import java.util.List;

/**
 * face (f)
 */
public class Face extends AbstractModel {
    public List<Integer> poinstIndexs;

    public Face(List<Integer> points) {
        this.poinstIndexs = points;
    }

    public Face() {
        this.poinstIndexs = new ArrayList<>();
    }

    @Override
    public String toString() {
        String f = "f";
        for (Integer point : poinstIndexs) {
            f += " " + String.valueOf(point);
        }

        return f;
    }
}
