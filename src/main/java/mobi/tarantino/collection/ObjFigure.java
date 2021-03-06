package mobi.tarantino.collection;

import mobi.tarantino.Config;
import mobi.tarantino.model.AbstractModel;
import mobi.tarantino.model.Face;
import mobi.tarantino.model.ObjComment;
import mobi.tarantino.model.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Это коллекция, которая хранит в себе точки и их связи. Умеет печататься в формате OBJ
 */
public class ObjFigure extends Figure {
    protected List<ObjComment> comments = new ArrayList<>();
    protected List<Face> faces;
    protected Config config;

    public ObjFigure(Config config) {
        this.config = config;
    }

    public ObjFigure() {
        this(Config.getInstance());
    }

    public static boolean containsFases(Figure faceFigure) {
        return faceFigure != null && faceFigure instanceof ObjFigure && ((ObjFigure) faceFigure).faces != null;
    }

    public void addComment(ObjComment comment) {
        comments.add(comment);
    }

    public void addComments(List<ObjComment> comments) {
        comments.addAll(comments);
    }

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

    public ObjFigure addFigure(Figure faceFigure) {
        if (containsFases(faceFigure)) {
            addFigure((ObjFigure) faceFigure);
            return this;
        }
        int size = super.size();
        points.addAll(faceFigure.points);

        Face face = new Face();
        for (int i = 0; i < faceFigure.size(); i++) {
            face.poinstIndexs.add(i);
        }

        addFace(Face.shift(face, size));
        return this;
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
        if (points == null || points.size() == 0 && comments.isEmpty())
            return "Null";
        else {
            String result;
            result = getComments();

            if (!result.isEmpty()) {
                result += "\n\n";
            }

            for (Iterator<Point> iterator = points.iterator(); iterator.hasNext(); ) {

                Point point = iterator.next();
                result += point.toString();
                if (iterator.hasNext()) result += "\n";
            }

            result += facesToString(faces);
            return result;
        }
    }

    public String getComments() {
        String result = modelsToString(comments);
        if ("true".equals(config.get(Config.Field.PRINT_CONFIG))) {
            result += "\n" + new ObjComment(" ***Config*** ") + "\n" + config.toObjComment().toString();
        }
        if ("true".equals(config.get(Config.Field.PRINT_EXTRACTS))) {
            result += "\n" + new ObjComment(" ***Figure*** ").toString() + "\n" + printExtracts();
        }

        return result;
    }

    protected ObjComment printExtracts() {
        return new ObjComment("points size: " + points.size(), "faces size: " + faces.size());
    }

    protected <N extends AbstractModel> String modelsToString(List<N> faces) {
        String result = "";
        if (faces != null && faces.size() > 0) {
            for (Iterator<N> iterator = faces.iterator(); iterator.hasNext(); ) {
                result += iterator.next();
                if (iterator.hasNext()) {
                    result += "\n";
                }
            }
        }
        return result;
    }

    private String facesToString(List<Face> faces) {
        String result = "";
        if (faces != null && faces.size() > 0) {
            result += "\n\n";
            for (Iterator<Face> iterator = faces.iterator(); iterator.hasNext(); ) {
                result += faceToString(iterator.next());
                if (iterator.hasNext()) {
                    result += "\n";
                }
            }
        }
        return result;
    }

    protected String faceToString(Face face) {
        String result = "";
        Face point = Face.shift(face, 1);
        result += point.toString();
        return result;
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
