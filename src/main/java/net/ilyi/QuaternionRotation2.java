package net.ilyi;

import mobi.tarantino.IcoSphereCreator;
import mobi.tarantino.PlateUtils;
import mobi.tarantino.collection.Figure;
import mobi.tarantino.collection.ObjFigure;
import mobi.tarantino.model.Face;
import mobi.tarantino.model.Point;
import mobi.tarantino.model.Vector;
import org.lwjgl.Sys;

import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Предполагает запускалку вращения вокруг вектора
 */
public class QuaternionRotation2 extends QuaternionRotation {

    public QuaternionRotation2(String arg) {
        super(arg);
    }

    public static void main(String[] args) {
        QuaternionRotation2 qr = new QuaternionRotation2(args.length > 0 ? args[0] : "1");
        qr.start();
    }

    @Override
    protected String[] render1() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();

        float radius = 1f;
        int edgeCount = 3;

        Point zero = new Point(0, 0, 0);
        Point zero1 = new Point(0, 0, 1);
        Point start = new Point(0, 0, 0);
        Point end = new Point(1, 1, 1);
        Point end2 = new Point(2, 0, 1);

        Vector zeroUnitVector = new Vector(zero, zero1);
        Vector vector = new Vector(start, end);

        Vector unitVector =
                Vector.makeUnitVector(
//                        zeroUnitVector
                        Vector.mul(vector, zeroUnitVector)
                );

        angle1 = (float) Vector.angle(zeroUnitVector, new Vector(start, end));
//        double phi = Math.PI / 180.0 * angle1;
        double phi = 180.0 * angle1 / Math.PI;
//        double phi = 0;


        glTranslatef(0.0f, 0.0f, -7.0f);

        glColor3f(0.0f, 0.0f, 0.5f);
//        drawHollowCircle(start, radius);
//        drawHollowCircle(end, radius);


        coordinateSystem();
        glColor3f(0.5f, 0.5f, 0.5f);

        Figure defaultPlane = PlateUtils.makeDefaultPlane(angle1, radius, edgeCount, unitVector);

        ObjFigure sphere = new IcoSphereCreator().create(1, 1);

//        drawFigure(sphere);
        Figure bottom = Figure.move(defaultPlane, new Vector(zero, start));
//        drawFigure(bottom);
//
        Figure top = Figure.move(defaultPlane, new Vector(zero, end));
//        drawFigure(top);
        Figure figure=PlateUtils.makeFollow(bottom, top);
        drawFigure(figure);
        System.out.println(figure);
//        System.out.println(new ObjFigure().addFigure(bottom).addFigure(top));
//
//        glColor3f(0.2f, 0.2f, 0.2f);
//        FigureFactory.makeFace(top, bottom).forEach(this::drawFigure);
//
//        glTranslatef(0.0f, 0.0f, -1.0f);
//        glColor3f(0.4f, 0.4f, 0.4f);
//        drawLine(start, end);
//        angle1 += 0.01f * delta;


        return new String[]{
                "angel: " + angle1,
                "phi: " + phi,
                "start: " + start + " end: " + end,
                "defaultPlane: " + defaultPlane,
//                "top: " + top,
//                "bottom: " + bottom,
        };
    }

    private void drawLine(Point start, Point end) {
        glBegin(GL_LINES);
        glColor3f(1.0f, 0.0f, 0.0f);
        glVertex3f(start.x, start.y, start.z);
        glVertex3f(end.x, end.y, end.z);
        glEnd();
    }

    private void drawFigure(Point[] trianglePoints) {
        glBegin(GL_POLYGON);
        for (int i = 0; i < trianglePoints.length; i++) {
            glVertex3d(trianglePoints[i].x, trianglePoints[i].y, trianglePoints[i].z);
        }
        glEnd();
    }

    private void drawFigure(ObjFigure figure) {
        figure.faceIterator().forEachRemaining(new Consumer<Face>() {
            @Override
            public void accept(Face face) {
                drawFigure(figure.getFigurePoints(face));
            }
        });
    }

    private void drawFigure(Figure figure) {
        glBegin(GL_POLYGON);
        int i = -1;
        for (Point point : figure) {
            i++;
//            glColor3f(i == 0 ? 1.0f : 0, i == 1 ? 1.0f : 0, i == 2 ? 1.0f : 0);
            glVertex3d(point.x, point.y, point.z);
        }
        glEnd();
    }

    void drawHollowCircle(Point center, float radius) {
        int lineAmount = 100; //# of triangles used to draw circle

        //GLfloat radius = 0.8f; //radius
        double twicePi = 2.0f * Math.PI;

        glBegin(GL_LINE_LOOP);
        for (int i = 0; i <= lineAmount; i++) {
            glVertex3d(
                    center.x + (radius * Math.cos(i * twicePi / lineAmount)),
                    center.y + (radius * Math.sin(i * twicePi / lineAmount)),
                    center.z
            );
        }
        glEnd();
    }

}
