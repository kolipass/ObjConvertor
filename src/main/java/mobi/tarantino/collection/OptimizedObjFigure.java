package mobi.tarantino.collection;

import mobi.tarantino.model.BigDecimalPoint;
import mobi.tarantino.model.Face;
import mobi.tarantino.model.ObjComment;
import mobi.tarantino.model.Point;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Оптимизируем методом BigDecimal)
 */
public class OptimizedObjFigure extends ObjFigure {
    protected Set<BigDecimalPoint> pointSet;

    public OptimizedObjFigure(ObjFigure parent) {
        super();
        super.points = parent.points;
        super.faces = parent.faces;
        super.comments = parent.comments;
    }

    public static Set<BigDecimalPoint> makeSet(Iterator<Point> iterator) {
        Set<BigDecimalPoint> pointSet = new LinkedHashSet<>();
        while (iterator.hasNext()) pointSet.add(new BigDecimalPoint(iterator.next()));
        return pointSet;

    }

    protected Face makeFaceFromSet(Face face, List<BigDecimalPoint> list) {
        Face result = new Face();
        result.poinstIndexs.addAll(
                face
                        .poinstIndexs
                        .stream()
                        .map(lastIndex -> getNewPosition(list, lastIndex))
                        .collect(Collectors.toList()));
        return result;
    }

    private Integer getNewPosition(List<BigDecimalPoint> convertedSet, Integer lastIndex) {
        return convertedSet.indexOf(new BigDecimalPoint(points.get(lastIndex)));
    }

    @Override
    public String toString() {
        if (points == null || points.size() == 0)
            return "Null";
        else {
            pointSet = makeSet(points.iterator());


            String result = commentsToString();

            if (!result.isEmpty()) {
                result += "\n\n";
            }

            for (Iterator<BigDecimalPoint> iterator = pointSet.iterator(); iterator.hasNext(); ) {
                BigDecimalPoint point = iterator.next();
                result += point.toString();
                if (iterator.hasNext()) result += "\n";
            }

            if (faces != null && faces.size() > 0) {
                result += "\n\n";
                ArrayList<BigDecimalPoint> list = new ArrayList<>(pointSet);

                for (Iterator<Face> iterator = faces.iterator(); iterator.hasNext(); ) {
                    result += faceToString(makeFaceFromSet(iterator.next(), list));
                    if (iterator.hasNext()) {
                        result += "\n";
                    }
                }
            }
            return result;
        }
    }

    @Override
    protected ObjComment printExtracts() {
        return super.printExtracts().add("pointSet size: " + pointSet.size());
    }
}
