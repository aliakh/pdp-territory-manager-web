package demo.domain.entity;

import demo.domain.Coord;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * ZIP.
 */
@Entity
@Table(schema = "names", name = "zip_code")
public class Zip implements Comparable<Zip> {

    @Id
    @Column(name = "zpc_zip_code")
    private String zipCode;

    //    @ColumnTransformer(read = "zip1_.zpc_map.getClobVal()", write = "?")
    @Column(name = "zpc_map")
    private String map;

    @Column(name = "zpc_latitude")
    private BigDecimal latitude;

    @Column(name = "zpc_longitude")
    private BigDecimal longitude;

    @Transient
    private ZipType zipType;

    @Transient
    private List<String> messages;

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Coord getCoord() {
        return new Coord(this.getLatitude(), this.getLongitude());
    }

    public ZipType getZipType() {
        return zipType;
    }

    public void setZipType(ZipType zipType) {
        this.zipType = zipType;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    @Override
    public int compareTo(Zip zip) {
        return this.zipCode.compareTo(zip.getZipCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Zip zip = (Zip) o;
        return zipCode.equals(zip.zipCode);
    }

    @Override
    public int hashCode() {
        return zipCode.hashCode();
    }
}
