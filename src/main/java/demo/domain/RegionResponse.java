package demo.domain;

import demo.domain.entity.Franchise;
import demo.domain.entity.Zip;

import java.util.List;

/**
 * Region response.
 */
public class RegionResponse {

    final private int franchiseId;
    final private int radius;
    final private boolean test;
    final private Franchise franchise;
    final private List<Zip> zips;
    final private List<String> messages;

    public RegionResponse(int franchiseId, int radius, boolean test, Franchise franchise, List<Zip> zips, List<String> messages) {
        this.franchiseId = franchiseId;
        this.radius = radius;
        this.test = test;
        this.franchise = franchise;
        this.zips = zips;
        this.messages = messages;
    }

    public int getFranchiseId() {
        return franchiseId;
    }

    public int getRadius() {
        return radius;
    }

    public boolean isTest() {
        return test;
    }

    public Franchise getFranchise() {
        return franchise;
    }

    public List<Zip> getZips() {
        return zips;
    }

    public List<String> getMessages() {
        return messages;
    }
}
