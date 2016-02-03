package demo.domain.entity;

import demo.domain.Coord;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * Region.
 */
@Entity
@Table(schema = "names", name = "dealer_pdp_region")
public class Region {

    @Id
    @Column(name = "region_id")
    private int regionId;

    @Column(name = "region_code")
    private String regionCode;

    @Column(name = "region_desc")
    private String regionDescription;

    @Column(name = "region_make")
    private String regionMake;

    @Column(name = "state")
    private String state;

//    @ColumnTransformer(read = "region0_.region_map.getClobVal()", write = "?")
    @Column(name = "region_map")
    private String map;

    @ManyToMany
    @Fetch(FetchMode.SELECT)
    @JoinTable(
        name = "dealer_pdp_zip_region", schema = "names",
        joinColumns = {@JoinColumn(name = "region_id", referencedColumnName = "region_id", nullable = false)},
        inverseJoinColumns = {@JoinColumn(name = "zip_code", referencedColumnName = "zpc_zip_code", nullable = false)})
    private Collection<Zip> zips;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @OneToOne
    @JoinColumn(name = "cdd_franchise_id")
    private Franchise franchise;

    @Transient
    private Integer franchiseId;

    @Column(name = "radius_mi")
    private int radius;

    @Transient
    private boolean created;

    public Region() {
    }

    public Region(int regionId, String regionCode, String regionDescription, String regionMake, String state,
                  BigDecimal latitude, BigDecimal longitude, Franchise franchise, int radius) {
        this.regionId = regionId;
        this.regionCode = regionCode;
        this.regionDescription = regionDescription;
        this.regionMake = regionMake;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.franchise = franchise;
        this.radius = radius;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionDescription() {
        return regionDescription;
    }

    public void setRegionDescription(String regionDescription) {
        this.regionDescription = regionDescription;
    }

    public String getRegionMake() {
        return regionMake;
    }

    public void setRegionMake(String regionMake) {
        this.regionMake = regionMake;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Collection<Zip> getZips() {
        return zips;
    }

    public void setZips(Collection<Zip> zips) {
        this.zips = zips;
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

    public void setCoord(Coord coord) {
        setLatitude(coord.getLatitude());
        setLongitude(coord.getLongitude());
    }

    public Franchise getFranchise() {
        return franchise;
    }

    public void setFranchise(Franchise franchise) {
        this.franchise = franchise;
    }

    public Integer getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(Integer franchiseId) {
        this.franchiseId = franchiseId;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }
}
