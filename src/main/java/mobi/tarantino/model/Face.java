package mobi.tarantino.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * obj face (f)
 * Грань в формате OBJ
 */
public class Face extends AbstractModel {
    public List<Integer> poinstIndexs;

    public Face(List<Integer> points) {
        this.poinstIndexs = points;
    }

//    public Face(Integer... points) {
//        this(Arrays.asList(points));
//    }

    public Face(int... points) {
        this();
        for (int point : points) {
            poinstIndexs.add(point);
        }
    }

    public Face() {
        this.poinstIndexs = new ArrayList<>();
    }

    public static Face shift(Face face, int position) {
        Face result = new Face();
        if (face.poinstIndexs != null)
            result.poinstIndexs.addAll(face.poinstIndexs.stream().map(i -> i + position).collect(Collectors.toList()));
        return result;
    }

    public Integer get(int index) {
        return poinstIndexs.get(index);
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
