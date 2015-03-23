package mobi.tarantino.collection;

/**
 * Created by kolipass on 22.03.15.
 */

import java.util.ArrayList;
import java.util.List;

public class SpecificFigure extends ObjFigure {
    List<Figure> specificFigures = new ArrayList<>();

    public SpecificFigure addSpecificFigure(Figure figure, boolean autoAdd) {
        specificFigures.add(figure);
        if (autoAdd) {
            addFigure(figure);
        }
        return this;
    }

    public List<Figure> getSpecificFigures() {
        return specificFigures;
    }

    public Figure getSpecificFigure(int index) {
        return specificFigures.get(index);
    }

}