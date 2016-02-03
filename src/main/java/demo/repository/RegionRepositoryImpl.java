package demo.repository;

import demo.domain.entity.Region;
import demo.domain.entity.Region_;
import demo.domain.search.Filter;
import demo.repository.search.Searcher;
import demo.repository.search.Sorter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Region repository implementation.
 */
public class RegionRepositoryImpl implements FilteredRepository<Region> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Region> findAll(Filter filter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Region> query = builder.createQuery(Region.class);
        Root<Region> root = query.from(Region.class);

        Sorter.INSTANCE.apply(filter.getSort(), builder, query, root);
        Searcher.INSTANCE.apply(filter.getSearch(), builder, query, root);

        CompoundSelection<Region> selection = builder.construct(Region.class,
                root.get(Region_.regionId),
                root.get(Region_.regionCode),
                root.get(Region_.regionDescription),
                root.get(Region_.regionMake),
                root.get(Region_.state),
                root.get(Region_.latitude),
                root.get(Region_.longitude),
                root.get(Region_.franchise),
                root.get(Region_.radius)
        );

        return entityManager.createQuery(query.select(selection))
                .setFirstResult((filter.getPage().getPage() - 1) * filter.getPage().getRows())
                .setMaxResults(filter.getPage().getRows())
                .getResultList();
    }
}
