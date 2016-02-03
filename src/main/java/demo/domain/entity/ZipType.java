package demo.domain.entity;

/**
 * ZIP type.
 */
public enum ZipType {

    OWN("ZIP is owned by region itself"),
    NEW("ZIP is free"),
    HOME_PINNED("ZIP is pinned - won"),
    WON("ZIP won by distance - region %d: %.2f mi, current region: %.2f mi"),
    LOST("ZIP lost by distance - region %d: %.2f mi, current region: %.2f mi"),
    LOST_PINNED("ZIP is pinned for region %d - lost");

    final private String message;

    private ZipType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
