package demo.service;

import demo.domain.FranchiseResponse;
import demo.domain.entity.Franchise;
import demo.mapper.RegionMapper;
import demo.repository.FranchiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Franchise service implementation.
 */
@Service
public class FranchiseServiceImpl implements FranchiseService {

    @Autowired
    private FranchiseRepository franchiseRepository;

    @Autowired
    private RegionMapper regionMapper;

    @Override
    public Franchise find(int franchiseId) {
        return franchiseRepository.findOne(franchiseId);
    }

    @Override
    public List<FranchiseResponse> findFranchisesNameLike(String term) {
        return regionMapper.findFranchisesNameLike('%' + term + '%');
    }
}
