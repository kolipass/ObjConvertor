package mobi.tarantino.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kolipass on 12.04.15.
 */
public class ObjComment extends AbstractModel {
    private List<String> comments = new ArrayList<>();

    public ObjComment add(String comment) {
        this.comments.add(comment);
        return this;
    }

    public ObjComment add(Object config) {
//        this.comments.add(comment);
        return this;
    }

    public ObjComment addAllComments(List<String> comments) {
        this.comments.addAll(comments);
        return this;
    }

    @Override
    public String toString() {
        String result = "";
        for (Iterator<String> iterator = comments.iterator(); iterator.hasNext(); ) {
            result += "#" + iterator.next();
            if (iterator.hasNext()) {
                result += "\n";
            }
        }
        return result;
    }
}
