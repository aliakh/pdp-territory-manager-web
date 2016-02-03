package demo.domain.search;

/**
 * Filter.
 */
public class Filter {

    final private Page page;
    final private Sort sort;
    final private Search search;

    public Filter(Page page, Sort sort, Search search) {
        this.page = page;
        this.sort = sort;
        this.search = search;
    }

    public Page getPage() {
        return page;
    }

    public Sort getSort() {
        return sort;
    }

    public Search getSearch() {
        return search;
    }
}
