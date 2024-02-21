package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.ApprovalDto;
import th.co.scb.onboardingapp.model.ApprovalListEvent;
import th.co.scb.onboardingapp.model.entity.ApprovalEntity;
import th.co.scb.onboardingapp.model.LoginBranchInfo;

import java.util.List;

@Component
public class GetRemoteApprovalCommandHandler {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private MappingHelper mappingHelper;

    @Autowired
    private ApplicationEventPublisher publisher;

    public List<ApprovalDto> getRemoteApproval(ObaAuthentication authentication) {
        String approvalBranchId = employeeService.getLoginBranch(authentication.getName(), authentication.getBranchId()).map(LoginBranchInfo::getApprovalBranchId).orElse(null);
        List<ApprovalEntity> list = approvalService.getRemoteApprovals(authentication.getName(), approvalBranchId);

        List<ApprovalDto> remoteApprovals = mappingHelper.mapAsList(list, ApprovalDto.class);

        for (ApprovalDto approveRemote : remoteApprovals){

            publisher.publishEvent(new ApprovalListEvent("SEARCH", "Checker Approval List", authentication.getName(), approveRemote.getCaseId(), approveRemote.getUpdatedDate()));

        }

        return remoteApprovals;
    }
}
