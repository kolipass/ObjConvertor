package net.ilyi;

import mobi.tarantino.model.Point;

public class Quaternion {
    public double r, i, j, k;

    public Quaternion(double r, double i, double j, double k) {
        this.r = r;
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public Quaternion(Point point, double k) {
        this(k, point.x, point.y, point.z);
    }

    public double mag() {
        return Math.sqrt(r * r + i * i + j * j + k * k);
    }

    public String toString() {
        return r + " " + i + " " + j + " " + k;
    }

    public Quaternion inverse() {
        double mag = this.mag();
        return new Quaternion(r / mag, -i / mag, -j / mag, -k / mag);
    }

    public Quaternion unit() {
        double mag = this.mag();
        return new Quaternion(r, i / mag, j / mag, k / mag);
    }

    public Quaternion mul(Quaternion q) {
        double r = this.r * q.r - this.i * q.i - this.j * q.j - this.k * q.k;
        double i = this.r * q.i + this.i * q.r + this.j * q.k - this.k * q.j;
        double j = this.r * q.j - this.i * q.k + this.j * q.r + this.k * q.i;
        double k = this.r * q.k + this.i * q.j - this.j * q.i + this.k * q.r;
        return new Quaternion(r, i, j, k);
    }
}