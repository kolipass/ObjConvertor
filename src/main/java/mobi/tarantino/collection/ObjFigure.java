package mobi.tarantino.collection;

import mobi.tarantino.model.Face;
import mobi.tarantino.model.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Это коллекция, которая хранит в себе точки и их связи. Умеет печататься в формате OBJ
 */
public class ObjFigure extends Figure {
    protected List<Face> faces;

    @Override
    public Iterator<Point> iterator() {
        return points != null ? points.iterator() : null;
    }

    public Iterator<Face> faceIterator() {
        return faces != null ? faces.iterator() : null;
    }

    public int size() {
        return super.size() + (faces != null ? faces.size() : 0);
    }

    public void addFace(Face face) {
        if (faces == null) {
            faces = new ArrayList<>();
        }
        faces.add(face);
    }

    public void addFigure(Figure faceFigure) {
        if (faceFigure != null && faceFigure instanceof ObjFigure) {
            addFigure((ObjFigure) faceFigure);
            return;
        }
        int size = super.size();
        points.addAll(faceFigure.points);

        Face face = new Face();
        for (int i = 0; i < faceFigure.size(); i++) {
            face.poinstIndexs.add(i);
        }

        addFace(Face.shift(face, size));

    }

    public void addFigure(ObjFigure figure) {
        int size = super.size();
        points.addAll(figure.points);

        for (Face face : figure.faces) {
            addFace(Face.shift(face, size));
        }

    }

    public Face getFace(int i) {
        return faces != null ? faces.get(i) : null;
    }

    @Override
    public String toString() {
        if (points == null || points.size() == 0)
            return "Null";
        else {
            String result = "";
            for (Iterator<Point> iterator = points.iterator(); iterator.hasNext(); ) {
                Point point = iterator.next();
                result += point.toString();
                if (iterator.hasNext()) result += "\n";
            }

            if (faces != null && faces.size() > 0) {
                result += "\n\n";
                for (Iterator<Face> iterator = faces.iterator(); iterator.hasNext(); ) {
                    Face point = Face.shift(iterator.next(), 1);
                    result += point.toString();
                    if (iterator.hasNext()) {
                        result += "\n";
                    }
                }
            }
            return result;
        }
    }

    public Figure getFigurePoints(Face face) {
        if (face == null) {
            return null;
        }
        Figure result = new Figure();
        for (Integer position : face.poinstIndexs) {
            result.add(get(position));
        }

        return result;
    }
}
