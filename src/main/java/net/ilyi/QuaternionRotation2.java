package net.ilyi;

import mobi.tarantino.Point;
import mobi.tarantino.Vector;

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
        Point zero1 = new Point(1, 1, 0);
        Point start = new Point(0, 0, 0);
        Point end = new Point(2, 0, 1);
        Point end2 = new Point(2, 0, 1);

        Vector startVector = new Vector(zero, zero1);
        Vector unitVector =
                Vector.makeUnitVector(
                        new Vector(start, end)
                );

        angle1 = (float) Vector.angle(startVector, new Vector(start, end));
//        double phi = Math.PI / 180.0 * angle1;
        double phi = 180.0 * angle1 / Math.PI;
//        double phi = 0;


        glTranslatef(0.0f, 0.0f, -7.0f);

        glColor3f(0.0f, 0.0f, 0.5f);
        drawHollowCircle(start.x, start.y, radius);
        drawHollowCircle(end.x, end.y, radius);


        coordinateSystem();
        glColor3f(0.5f, 0.5f, 0.5f);
//        drawFigure(Main.makePlanes(start, edgeCount, radius), getUnitQuaternion(unitVector, phi));
//        drawFigure(Main.makePlanes(end, edgeCount, radius), getUnitQuaternion(unitVector, phi));
        glTranslatef(0.0f, 0.0f, -1.0f);
        glColor3f(0.4f, 0.4f, 0.4f);
        drawLine(start, end);
//        angle1 += 0.01f * delta;

        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, 800, 600, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glClear(GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
//        Color.white.bind();
//        font.drawString(0, 0, "zero.toString()");
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        return new String[]{"angel: " + angle1};
    }

    private Quaternion getUnitQuaternion(Vector vector, double phi) {
        double cosphi = Math.cos(phi / 2.0);
        double sinphi = Math.sin(phi / 2.0);
        return new Quaternion(cosphi, vector.x * sinphi, vector.y * sinphi, vector.z * sinphi).unit();
    }

    private void drawLine(Point start, Point end) {
        glBegin(GL_LINES);
        glColor3f(1.0f, 0.0f, 0.0f);
        glVertex3f(start.x, start.y, start.z);
        glVertex3f(end.x, end.y, end.z);
        glEnd();
    }

    private void drawFigure(Point[] trianglePoints, Quaternion q) {
        glBegin(GL_POLYGON);
        for (int i = 0; i < trianglePoints.length; i++) {
            Quaternion p = new Quaternion(trianglePoints[i], 0.0);
            p = q.mul(p.mul(q.inverse()));
            glVertex3d(p.i, p.j, p.k);
        }
        glEnd();
    }

    void drawHollowCircle(float x, float y, float radius) {
        int lineAmount = 100; //# of triangles used to draw circle

        //GLfloat radius = 0.8f; //radius
        double twicePi = 2.0f * Math.PI;

        glBegin(GL_LINE_LOOP);
        for (int i = 0; i <= lineAmount; i++) {
            glVertex2d(
                    x + (radius * Math.cos(i * twicePi / lineAmount)),
                    y + (radius * Math.sin(i * twicePi / lineAmount))
            );
        }
        glEnd();
    }

}
