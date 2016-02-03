package demo.domain;

import demo.domain.entity.Franchise;
import demo.domain.entity.Zip;

import java.util.List;

/**
 * ZIPs response.
 */
public class ZipsResponse {

    final private int franchiseId;
    final private int radius;
    final private Franchise franchise;
    final private List<Zip> zips;

    public ZipsResponse(int franchiseId, int radius, Franchise franchise, List<Zip> zips) {
        this.franchiseId = franchiseId;
        this.radius = radius;
        this.franchise = franchise;
        this.zips = zips;
    }

    public int getFranchiseId() {
        return franchiseId;
    }

    public int getRadius() {
        return radius;
    }

    public Franchise getFranchise() {
        return franchise;
    }

    public List<Zip> getZips() {
        return zips;
    }
}
