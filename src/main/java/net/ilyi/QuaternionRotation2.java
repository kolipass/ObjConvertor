package net.ilyi;

import mobi.tarantino.Main;
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
    protected void render1() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();


        Point zero = new Point(0, 0, 0);
        Point zero1 = new Point(1, 1, 0);
        Point start = new Point(0, 0, 0);
        Point end = new Point(1, 1, 1);

        Vector startVector = new Vector(zero, zero1);
        Vector vector = Vector.makeUnitVector(new Vector(start, end));

        angle1 = (float) Vector.angle(startVector, new Vector(start, end));
//        double phi = Math.PI / 180.0 * angle1;
        double phi =  180.0 * angle1/Math.PI;
        Point[] trianglePoints =
                Main.makePlanes(start, 3, 1);


        double cosphi = Math.cos(phi / 2.0);
        double sinphi = Math.sin(phi / 2.0);
        Quaternion q = new Quaternion(cosphi, vector.x * sinphi, vector.y * sinphi, vector.z * sinphi).unit();
        Quaternion p;

//// triangle
        glTranslatef(0.0f, 0.0f, -7.0f);

        glColor3f(0.0f, 0.0f, 1.0f);
        drawHollowCircle(start.x, start.y, 1);


        coordinateSystem();
        glColor3f(0.5f, 0.5f, 0.5f);
        glBegin(GL_POLYGON);
        for (int i = 0; i < trianglePoints.length; i++) {
            p = new Quaternion(trianglePoints[i], 0.0);
            p = q.mul(p.mul(q.inverse()));
            glVertex3d(p.i, p.j, p.k);
        }
        glEnd();

        glBegin(GL_LINES);
        glColor3f(1.0f, 0.0f, 0.0f);
        glVertex3f(start.x, start.y, start.z);
        glVertex3f(end.x, end.y, end.z);
        glEnd();
//        angle1 += 0.01f * delta;
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
