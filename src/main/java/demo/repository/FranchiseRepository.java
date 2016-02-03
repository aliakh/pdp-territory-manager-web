package demo.repository;

import demo.domain.entity.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Franchise repository.
 */
@Repository
public interface FranchiseRepository extends JpaRepository<Franchise, Integer> {
}
