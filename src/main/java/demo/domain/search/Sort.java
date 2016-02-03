package demo.domain.search;

/**
 * Sorting.
 */
public class Sort {

    final private String field;
    final private String order;

    public Sort(String field, String order) {
        this.field = field;
        this.order = order;
    }

    public String getField() {
        return field;
    }

    public String getOrder() {
        return order;
    }
}