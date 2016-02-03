package demo.domain;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * JqGrid response.
 */
public class JqgridResponse<T> {

    private List<T> rows;
    private int page;
    private int max;
    private long total;

    public JqgridResponse() {
    }

    public JqgridResponse(Page<T> pageable) {
        this.rows = pageable.getContent();
        this.page = pageable.getNumber() + 1;
        this.max = pageable.getSize();
        this.total = pageable.getTotalElements();
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
