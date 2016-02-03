package demo.domain.search;

/**
 * Pagination.
 */
public class Page {

    final private int page;
    final private int rows;

    public Page(int page, int rows) {
        this.page = page;
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public int getRows() {
        return rows;
    }
}