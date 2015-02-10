package net.ilyi;

import mobi.tarantino.Point;
import mobi.tarantino.Vector;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class QuaternionRotation {


    public static final int WIDTH = 640;
    public static final int HEIGHT = 360;
    protected int delta;
    private String type;

    public QuaternionRotation(String arg) {
        type = arg;
    }

    public static void main(String[] args) {
        QuaternionRotation qr = new QuaternionRotation(args.length > 0 ? args[0] : "1");
        qr.start();
    }

    protected void start() {
        initDisplay();
        initOpenGL();
        updateDelta();
        while (!Display.isCloseRequested()) {
            switch (type) {
                default:
                case "1":
                    render1();
                    break;
                case "3":
                    render3();
                    break;
            }
//            processInput();
//            Display.sync(60);
            Display.update();
//            updateDelta();
        }
        Display.destroy();
    }

    private void initDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle("GL");

            // blank icon
            ByteBuffer[] b = new ByteBuffer[1];
            b[0] = BufferUtils.createByteBuffer(256 * 4);
            Display.setIcon(b);

            Display.create();
        } catch (LWJGLException ex) {
            System.err.println("Couldn't set up display");
            System.exit(1);
        }
    }

    private void initOpenGL() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(45.0f, (float) WIDTH / HEIGHT, 0.001f, 10.0f);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnable(GL_DEPTH_TEST);
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    float camanglex = 0.0f;
    float camangley = 0.0f;
    float angle1 = 0.0f;
    float angle2 = 0.0f;
    float angle3 = 0.0f;
    float x = 0.0f;
    float y = 0.0f;
    float z = 1.0f;

    protected void render1() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();

        double phi = Math.PI / 180.0 * angle1;

        Point start = new Point(1, 1, 1);
        Point end = new Point(-1, 1, 1);

       Vector vector = new Vector(start, end);

        double cosphi = Math.cos(phi / 2.0);
        double sinphi = Math.sin(phi / 2.0);

        Quaternion q = new Quaternion(cosphi, vector.x * sinphi, vector.y * sinphi, vector.z * sinphi).unit();
        Quaternion p;

        Point[] figure = {
//                new Point(-0.5f, -0.5f),
//                new Point(0.0f, 0.5f, -0.5f),
//                new Point(0.0f, 0.5f, 0.5f)
                new Point(-0.0f, 1.0f, 1f),
                new Point(-1.0f, -1.0f, -1.0f),
                new Point(1.0f, -1.0f, 1.0f)
        };
// triangle
        glTranslatef(0.0f, 0.0f, -6.0f);
        glBegin(GL_TRIANGLES);

        glColor3f(1.0f, 0.0f, 0.0f);
        p = new Quaternion(figure[0], 0.0);
//        p = q.mul(p.mul(q.inverse()));
        glVertex3d(p.i, p.j, p.k);

        glColor3f(0.0f, 1.0f, 0.0f);
        p = new Quaternion(0.0, 0.5, -0.5, 0.0);
//        p = q.mul(p.mul(q.inverse()));
        glVertex3d(p.i, p.j, p.k);

        glVertex3d(0.0f, 0.0f, 1.0f);
        p = new Quaternion(0.0, 0.5, 0.5, 0.0);
//        p = q.mul(p.mul(q.inverse()));
        glVertex3d(p.i, p.j, p.k);

        glEnd();

        glBegin(GL_LINES);
        glColor3f(0.0f, 50.0f, 50.0f);
        glVertex3f(start.x, start.y, start.z);
        glVertex3f(end.x, end.y, end.z);
        glEnd();

        coordinateSystem();
        angle1 += 0.1f * delta;
    }

    protected void coordinateSystem() {
        //+X
        glColor3f(1, 0, 0);
        glBegin(GL_LINES);
        glVertex3f(0, 0, 0);
        glVertex3f(1, 0, 0);
        glEnd();
//+Y
        glColor3f(0, 1, 0);
        glBegin(GL_LINES);
        glVertex3f(0, 0, 0);
        glVertex3f(0, 1, 0);
        glEnd();
//+Z
        glColor3f(0, 0, 1);
        glBegin(GL_LINES);
        glVertex3f(0, 0, 0);
        glVertex3f(0, 0, 1);
        glEnd();
    }


    private void render3() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();

        glRotatef(camanglex, 1.0f, 0.0f, 0.0f);
        glRotatef(camangley, 0.0f, 1.0f, 0.0f);
        glTranslatef(-x, -y, -z);


        // quad
        Quaternion p;
        glTranslatef(0.0f, 0.0f, -2.0f);
        glBegin(GL_QUADS);

        glColor3f(0.0f, 0.0f, 0.0f);
        p = new Quaternion(0.0, -0.5, -0.5, 0.0);
        p = rotate(p.i, p.j, p.k, 0.0, 1.0, 0.0, angle1);
        p = rotate(p.i, p.j, p.k, 1.0, 0.0, 0.0, angle2);
        p = rotate(p.i, p.j, p.k, 1.0, 1.0, 1.0, angle3);
        glVertex3d(p.i, p.j, p.k);

        p = new Quaternion(0.0, 0.5, -0.5, 0.0);
        p = rotate(p.i, p.j, p.k, 0.0, 1.0, 0.0, angle1);
        p = rotate(p.i, p.j, p.k, 1.0, 0.0, 0.0, angle2);
        p = rotate(p.i, p.j, p.k, 1.0, 1.0, 1.0, angle3);
        glVertex3d(p.i, p.j, p.k);

        p = new Quaternion(0.0, 0.5, 0.5, 0.0);
        p = rotate(p.i, p.j, p.k, 0.0, 1.0, 0.0, angle1);
        p = rotate(p.i, p.j, p.k, 1.0, 0.0, 0.0, angle2);
        p = rotate(p.i, p.j, p.k, 1.0, 1.0, 1.0, angle3);
        glVertex3d(p.i, p.j, p.k);

        p = new Quaternion(0.0, -0.5, 0.5, 0.0);
        p = rotate(p.i, p.j, p.k, 0.0, 1.0, 0.0, angle1);
        p = rotate(p.i, p.j, p.k, 1.0, 0.0, 0.0, angle2);
        p = rotate(p.i, p.j, p.k, 1.0, 1.0, 1.0, angle3);
        glVertex3d(p.i, p.j, p.k);

        glEnd();

        angle1 += 0.5f * delta;
        angle2 += 0.1f * delta;
        angle3 += 0.05f * delta;
    }

    private Quaternion rotate(
            double x, double y, double z,
            double i, double j, double k,
            float angle) {
        double phi = Math.PI / 180.0 * angle;
        double cosphi = Math.cos(phi / 2.0);
        double sinphi = Math.sin(phi / 2.0);

        Quaternion q = new Quaternion(cosphi, i * sinphi, j * sinphi, k * sinphi).unit();
        Quaternion p = new Quaternion(0, x, y, z);
        return q.mul(p.mul(q.inverse()));
    }

    float mousespeed = 0.1f;

    private void processInput() {
        /* keyboard handling start */
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            x -= 0.001f * delta;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            x += 0.001f * delta;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            z -= 0.001f * delta;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            z += 0.001f * delta;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            y += 0.001f * delta;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            y -= 0.001f * delta;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            x = 0.0f;
            y = 0.0f;
            z = 1.0f;
        }
        /* keyboard handling end */
        
        /* mouse handling start */
        // mouse rotations
        if (Mouse.isGrabbed()) {
            int DX = Mouse.getDX();
            int DY = Mouse.getDY();
            camanglex -= DY * mousespeed;
            camangley += DX * mousespeed;

            // fix angles
            if (camanglex < -45.0f) {
                camanglex = -45.0f;
            } else if (camanglex > 45.0f) {
                camanglex = 45.0f;
            }

            if (camangley > 360.0f) {
                camangley -= 360.0f;
            } else if (camangley < 0.0f) {
                camangley += 360.0f;
            }
        }

        // mouse clicks
        while (Mouse.next()) {
            if (Mouse.isButtonDown(0))
                Mouse.setGrabbed(true);
            if (Mouse.isButtonDown(1))
                Mouse.setGrabbed(false);
        }
        /* mouse handling end */
    }


    private long prevTime = Sys.getTime();

    private void updateDelta() {
        long curr = Sys.getTime() * 1000 / Sys.getTimerResolution();
        delta = (int) (curr - prevTime);
        prevTime = curr;
    }
}
