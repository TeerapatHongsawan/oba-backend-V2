package th.co.scb.onboardingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.scb.onboardingapp.model.entity.BranchEntity;
import th.co.scb.onboardingapp.service.BranchApplicationService;


import java.util.List;

@RestController
public class BranchController {

    @Autowired
    private BranchApplicationService branchApplicationService;

    @GetMapping("/api/branch")
    public List<BranchEntity> browse() {
        return branchApplicationService.getBranchs();
    }
}
