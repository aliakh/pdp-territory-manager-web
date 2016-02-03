package demo.domain.search;

/**
 * Search.
 */
public class Search {

    final private String field;
    final private String value;
    final private String operation;

    public Search(String field, String value, String operation) {
        this.field = field;
        this.value = value;
        this.operation = operation;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public String getOperation() {
        return operation;
    }
}
