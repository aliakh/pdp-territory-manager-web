package demo.domain;

import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * Coordinate.
 */
public class Coord {

    private BigDecimal latitude;
    private BigDecimal longitude;

    public Coord(BigDecimal latitude, BigDecimal longitude) {
        Assert.notNull(latitude);
        Assert.notNull(longitude);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude.setScale(6);
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude.setScale(6);
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public BigDecimal getDistance(Coord coordinate) {
        BigDecimal x = this.latitude.subtract(coordinate.latitude);
        BigDecimal y = this.longitude.subtract(coordinate.longitude);
        double z = Math.sqrt(x.pow(2).add(y.pow(2)).doubleValue());
        return new BigDecimal(z * 65).setScale(6, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Coord that = (Coord) o;
        return latitude.equals(that.latitude) && longitude.equals(that.longitude);
    }

    @Override
    public int hashCode() {
        int result = latitude.hashCode();
        result = 31 * result + longitude.hashCode();
        return result;
    }
}
