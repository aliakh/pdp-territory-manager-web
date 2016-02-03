package demo.repository;

import demo.domain.search.Filter;

import java.util.List;

/**
 * Filtered repository.
 */
public interface FilteredRepository<T> {

    List<T> findAll(Filter filter);
}
