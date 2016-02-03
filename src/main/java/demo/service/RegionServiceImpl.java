package demo.service;

import demo.domain.RegionResponse;
import demo.domain.entity.Region;
import demo.domain.entity.Zip;
import demo.domain.search.Filter;
import demo.mapper.RegionMapper;
import demo.repository.RegionRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Region service implementation.
 */
@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private RegionHelper regionHelper;

    @Override
    public List<Region> findAll(Filter filter) {
        return regionRepository.findAll(filter);
    }

    @Override
    @Transactional(readOnly = true)
    public Region find(int regionId) {
        Region region = regionRepository.findOne(regionId);
        Hibernate.initialize(region.getZips());
        return region;
    }

    @Override
    public long count() {
        return regionRepository.count();
    }

    @Override
    public List<Zip> getAllZips(int franchiseId, int radius) {
        return regionMapper.getAllZips(franchiseId, radius);
    }

    @Override
    public RegionResponse createOrUpdateRegion(int franchiseId, int radius, boolean test) {
        return regionHelper.createOrUpdateRegion(franchiseId, radius, test);
    }
}
