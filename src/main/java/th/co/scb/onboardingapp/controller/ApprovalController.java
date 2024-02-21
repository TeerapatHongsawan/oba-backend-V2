package th.co.scb.onboardingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.ApprovalDto;
import th.co.scb.onboardingapp.service.ApprovalApplicationService;

import java.util.List;

@RestController
public class ApprovalController {

    @Autowired
    private ApprovalApplicationService approvalApplicationService;



    @GetMapping("/api/approval/remote")
    public List<ApprovalDto> getRemoteApprovals(ObaAuthentication auth) {
        return approvalApplicationService.getRemoteApproval(auth);
    }
}

