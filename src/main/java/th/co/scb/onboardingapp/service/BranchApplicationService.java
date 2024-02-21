package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.entity.BranchEntity;

import java.util.List;

@Service
public class BranchApplicationService {

    @Autowired
    private BranchMasterDataService branchMasterDataService;

    public List<BranchEntity> getBranchs() {
        return branchMasterDataService.branches();
    }
}
