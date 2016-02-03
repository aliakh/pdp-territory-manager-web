package demo.repository.search;

import demo.domain.entity.Region;
import demo.domain.search.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Sorter.
 */
public enum Sorter {

    INSTANCE;

    public void apply(Sort sort, CriteriaBuilder builder, CriteriaQuery<Region> query, Root<Region> root) {
        if ("asc".equalsIgnoreCase(sort.getOrder())) {
            query.orderBy(builder.asc(root.get(sort.getField())));
        } else {
            query.orderBy(builder.desc(root.get(sort.getField())));
        }
    }
}
