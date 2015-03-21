package mobi.tarantino.collection;

import bsim.geometry.BSimMesh;
import bsim.geometry.BSimTriangle;
import bsim.geometry.BSimVertex;
import mobi.tarantino.model.Face;
import mobi.tarantino.model.Point;

import javax.vecmath.Vector3d;

/**
 * Created by kolipass on 19.03.15.
 */
public class BSimToObjFigure {

    public static ObjFigure BSimToObjFigure(BSimMesh bSimMesh) {
        ObjFigure figure = new ObjFigure();

        for (BSimVertex bSimVertex : bSimMesh.getVertices()) {
            Vector3d location = bSimVertex.getLocation();
            Point point = new Point(location.x, location.y, location.z);
            figure.add(point);
        }

        for (BSimTriangle bSimTriangle : bSimMesh.getFaces()) {
            figure.addFace(new Face(bSimTriangle.getPoints()));
        }

        return figure;
    }
}
