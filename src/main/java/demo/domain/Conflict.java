package demo.domain;

import demo.domain.entity.Region;
import demo.domain.entity.Zip;

/**
 * Conflict.
 */
public class Conflict {

    private Region region;
    private boolean pinned;
    private Zip zip;

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public Zip getZip() {
        return zip;
    }

    public void setZip(Zip zip) {
        this.zip = zip;
    }
}
