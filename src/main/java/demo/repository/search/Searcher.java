package demo.repository.search;

import demo.domain.entity.Region;
import demo.domain.search.Search;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;

/**
 * Searcher.
 */
public enum Searcher {

    INSTANCE;

    private static final Map<String, Item> ITEMS = new HashMap<String, Item>();

    private interface Item {
        Predicate apply(Search search, CriteriaBuilder builder, Root<Region> root);
    }

    static {
        ITEMS.put("eq", new Item() {
            @Override
            public Predicate apply(Search search, CriteriaBuilder builder, Root<Region> root) {
                return builder.equal(root.get(search.getField()), search.getValue());
            }
        });
        ITEMS.put("ne", new Item() {
            @Override
            public Predicate apply(Search search, CriteriaBuilder builder, Root<Region> root) {
                return builder.notEqual(root.get(search.getField()), search.getValue());
            }
        });
        ITEMS.put("nn", new Item() {
            @Override
            public Predicate apply(Search search, CriteriaBuilder builder, Root<Region> root) {
                return builder.isNotNull(root.get(search.getField()));
            }
        });
    }

    public void apply(Search search, CriteriaBuilder builder, CriteriaQuery<Region> query, Root<Region> root) {
        if (StringUtils.hasLength(search.getOperation())) {
            Item item = ITEMS.get(search.getOperation());
            if (item == null) {
                throw new RuntimeException("Search not supported");
            }
            query.where(item.apply(search, builder, root));
        }
    }
}
