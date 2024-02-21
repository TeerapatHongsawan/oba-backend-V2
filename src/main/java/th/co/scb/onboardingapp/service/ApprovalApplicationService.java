package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.ApprovalDto;


import java.util.List;

@Service
public class ApprovalApplicationService {

    @Autowired
    private GetRemoteApprovalCommandHandler getRemoteApprovalCommandHandler;

    public List<ApprovalDto> getRemoteApproval(ObaAuthentication authentication) {
        return getRemoteApprovalCommandHandler.getRemoteApproval(authentication);
    }
}
