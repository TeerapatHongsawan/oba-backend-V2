package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.entity.BranchEntity;
import th.co.scb.onboardingapp.repository.BranchRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Service
public class BranchMasterDataService {

    @Autowired
    private BranchRepository branchRepository;

    @Cacheable(value = "branchMap", unless = "#result == null")
    public Map<String, BranchEntity> branchMap() {
        return branches().stream()
                .collect(toMap(BranchEntity::getBranchId, Function.identity()));
    }

    @Cacheable(value = "allBranches", unless = "#result == null")
    public List<BranchEntity> branches() {
        return branchRepository.findAll();
    }


}
