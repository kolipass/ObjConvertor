package mobi.tarantino;

import mobi.tarantino.collection.ObjFigure;
import mobi.tarantino.model.Face;
import mobi.tarantino.model.Point;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by kolipass on 13.03.15.
 * http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
 */
public class IcoSphereCreator {

    private ObjFigure geometry;
    private int index;
    private Dictionary<Integer, Integer> middlePointIndexCache;

    // add vertex to mesh, fix position to be on unit sphere, return index
    private int addVertex(Point p) {
        double length = Math.sqrt(p.x * p.x + p.y * p.y + p.z * p.z);
        geometry.add(new Point(p.x / length, p.y / length, p.z / length));
        return index++;
    }

    // return index of point in the middle of p1 and p2
    private int getMiddlePoint(int p1, int p2) {
        // first check if we have it already
        boolean firstIsSmaller = p1 < p2;
        int smallerIndex = firstIsSmaller ? p1 : p2;
        int greaterIndex = firstIsSmaller ? p2 : p1;
        int key = smallerIndex + greaterIndex;

        Integer ret = middlePointIndexCache.get(key);
        if (ret != null) {
            return ret;
        }

        // not in cache, calculate it
        Point point1 = this.geometry.get(p1);
        Point point2 = this.geometry.get(p2);
        Point middle = new Point(
                (point1.x + point2.x) / 2.0,
                (point1.y + point2.y) / 2.0,
                (point1.z + point2.z) / 2.0);

        // add vertex makes sure point is on unit sphere
        int i = addVertex(middle);

        // store it, return index
        this.middlePointIndexCache.put(key, i);
        return i;
    }

    public ObjFigure create(int recursionLevel,float r) {
        this.geometry = new ObjFigure();
        this.middlePointIndexCache = new Hashtable<Integer, Integer>();
        this.index = 0;

        makeVertices(r);


        // create 20 triangles of the icosahedron
        List<Face> triangles = new ArrayList<>();

        // 5 faces around point 0
        triangles.add(new Face(0, 11, 5));
        triangles.add(new Face(0, 5, 1));
        triangles.add(new Face(0, 1, 7));
        triangles.add(new Face(0, 7, 10));
        triangles.add(new Face(0, 10, 11));

        // 5 adjacent faces
        triangles.add(new Face(1, 5, 9));
        triangles.add(new Face(5, 11, 4));
        triangles.add(new Face(11, 10, 2));
        triangles.add(new Face(10, 7, 6));
        triangles.add(new Face(7, 1, 8));

        // 5 faces around point 3
        triangles.add(new Face(3, 9, 4));
        triangles.add(new Face(3, 4, 2));
        triangles.add(new Face(3, 2, 6));
        triangles.add(new Face(3, 6, 8));
        triangles.add(new Face(3, 8, 9));

        // 5 adjacent faces
        triangles.add(new Face(4, 9, 5));
        triangles.add(new Face(2, 4, 11));
        triangles.add(new Face(6, 2, 10));
        triangles.add(new Face(8, 6, 7));
        triangles.add(new Face(9, 8, 1));


        // refine triangles
        for (int i = 0; i < recursionLevel; i++) {
            List<Face> faces2 = new ArrayList<>();
            for (Face triangle : triangles) {
                // replace triangle by 4 triangles
                int a = getMiddlePoint(triangle.get(0), triangle.get(1));
                int b = getMiddlePoint(triangle.get(1), triangle.get(2));
                int c = getMiddlePoint(triangle.get(2), triangle.get(0));

                faces2.add(new Face(triangle.get(0), a, c));
                faces2.add(new Face(triangle.get(1), b, a));
                faces2.add(new Face(triangle.get(2), c, b));
                faces2.add(new Face(a, b, c));
            }
            triangles = faces2;
        }

        // done, now add triangles to mesh
        triangles.forEach(this.geometry::addFace);

        return geometry;
    }

    /**
     * Create 12 vertices of a icosahedron
     * http://en.wikipedia.org/wiki/Regular_icosahedron#Cartesian_coordinates
     *
     * @param r
     */
    private void makeVertices(float r) {
        double t = (1.0 + Math.sqrt(5.0)) / 2.0;

        addVertex(new Point(-1, t, 0).mul(r));
        addVertex(new Point(1, t, 0).mul(r));
        addVertex(new Point(-1, -t, 0).mul(r));
        addVertex(new Point(1, -t, 0).mul(r));

        addVertex(new Point(0, -1, t).mul(r));
        addVertex(new Point(0, 1, t).mul(r));
        addVertex(new Point(0, -1, -t).mul(r));
        addVertex(new Point(0, 1, -t).mul(r));

        addVertex(new Point(t, 0, -1).mul(r));
        addVertex(new Point(t, 0, 1).mul(r));
        addVertex(new Point(-t, 0, -1).mul(r));
        addVertex(new Point(-t, 0, 1).mul(r));
    }
}