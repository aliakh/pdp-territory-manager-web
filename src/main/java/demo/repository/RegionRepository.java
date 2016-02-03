package demo.repository;

import demo.domain.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Region repository.
 */
@Repository
public interface RegionRepository extends FilteredRepository<Region>, JpaRepository<Region, Integer> {

//CHECKSTYLE:OFF
    Region findByFranchise_FranchiseId(int franchiseId);
//CHECKSTYLE:ON
}
