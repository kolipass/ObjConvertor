package mobi.tarantino.model;

import mobi.tarantino.Config;

import java.math.BigDecimal;

public class BigDecimalPoint extends AbstractModel {
    private BigDecimal x = BigDecimal.ZERO;
    private BigDecimal y = BigDecimal.ZERO;
    private BigDecimal z = BigDecimal.ZERO;
    private Config config;

    public BigDecimalPoint(Config config) {
        this.config = config;
    }

    public BigDecimalPoint() {
        this(Config.getInstance());
    }

    public BigDecimalPoint(BigDecimal x, BigDecimal y, BigDecimal z) {
        this();
        setPoint(x, y, z);
    }

    public BigDecimalPoint(Point point) {
        this();
        setPoint(factory(point.x), factory(point.y), factory(point.z));
    }

    private void setPoint(BigDecimal x, BigDecimal y, BigDecimal z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BigDecimal getX() {
        return x;
    }

    public BigDecimalPoint setX(BigDecimal x) {
        this.x = x;
        return this;
    }

    public BigDecimalPoint setX(float x) {
        this.x = factory(x);
        return this;
    }

    public BigDecimal getY() {
        return y;
    }

    public BigDecimalPoint setY(BigDecimal y) {
        this.y = y;
        return this;
    }

    public BigDecimalPoint setY(float y) {
        this.y = factory(y);
        return this;
    }

    public BigDecimal getZ() {
        return z;
    }

    public BigDecimalPoint setZ(BigDecimal z) {
        this.z = z;
        return this;
    }

    public BigDecimalPoint setZ(float z) {
        this.z = factory(z);
        return this;
    }

    public BigDecimal factory(float value) {
        return new BigDecimal(value).setScale(config.getScale(), BigDecimal.ROUND_DOWN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BigDecimalPoint)) return false;

        BigDecimalPoint that = (BigDecimalPoint) o;

        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        if (y != null ? !y.equals(that.y) : that.y != null) return false;
        return !(z != null ? !z.equals(that.z) : that.z != null);

    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "v " + x + " " + y + " " + z;
    }
}
