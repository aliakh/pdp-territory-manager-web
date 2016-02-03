package demo.mapper;

import demo.domain.Conflict;
import demo.domain.FranchiseResponse;
import demo.domain.RegionZip;
import demo.domain.entity.Region;
import demo.domain.entity.Zip;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Region MyBatis mapper.
 */
public interface RegionMapper {

    Zip getZip(String zipCode);

    List<Zip> getAllZips(@Param("franchiseId") int franchiseId,
                         @Param("radius") int radius);

    List<Conflict> getConflicts(@Param("franchiseId") int franchiseId,
                                @Param("radius") int radius,
                                @Param("make") String make);

    Integer getMaxRegionId();

    RegionZip getRegionZip(@Param("make") String make,
                           @Param("zipCode") String zipCode);

    void deleteAllRegionZips(@Param("regionId") int regionId);

    void deleteRegionZip(@Param("regionId") int regionId,
                         @Param("zipCode") String zipCode);

    void insertRegionZip(@Param("regionId") int regionId,
                         @Param("zipCode") String zipCode,
                         @Param("pinned") boolean pinned);

    void insertRegion(Region region);

    void updateRegion(Region region);

    List<FranchiseResponse> findFranchisesNameLike(@Param("term") String term);
}
