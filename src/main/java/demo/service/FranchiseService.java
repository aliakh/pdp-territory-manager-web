package demo.service;

import demo.domain.FranchiseResponse;
import demo.domain.entity.Franchise;

import java.util.List;

/**
 * Franchise service interface.
 */
public interface FranchiseService {

    Franchise find(int franchiseId);

    List<FranchiseResponse> findFranchisesNameLike(String term);
}
