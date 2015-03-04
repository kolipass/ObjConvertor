package mobi.tarantino;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolipass on 24.02.15.
 */
public class FigureFactory {
    public static List<Figure> makeFace(Figure top, Figure bottom) {
        if (top == null || bottom == null || top.size() != bottom.size()) {
            throw new ArithmeticException("Проблемы с входными данными");
        }
        List<Figure> result = new ArrayList<>();

        int lastPointIndex = -1;
        for (int i = 0; i < top.size(); i++) {
            lastPointIndex = i != 0 ? i -1: top.size() - 1;

            Figure figure = new Figure();

            figure.add(top.get(i));
            figure.add(bottom.get(i));

            figure.add(bottom.get(lastPointIndex));
            figure.add(top.get(lastPointIndex));

            result.add(figure);
        }

        return result;
    }
}
