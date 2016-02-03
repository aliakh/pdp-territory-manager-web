package demo.service;

import demo.domain.RegionResponse;
import demo.domain.entity.Region;
import demo.domain.entity.Zip;
import demo.domain.search.Filter;

import java.util.List;

/**
 * Region service interface.
 */
public interface RegionService {

    List<Region> findAll(Filter filter);

    Region find(int regionId);

    long count();

    List<Zip> getAllZips(int franchiseId, int radius);

    RegionResponse createOrUpdateRegion(int franchiseId, int radius, boolean test);
}
