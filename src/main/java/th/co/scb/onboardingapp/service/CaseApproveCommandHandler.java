package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ConflictException;
import th.co.scb.onboardingapp.exception.NotFoundException;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.enums.CaseErrorCodes;
import java.util.Objects;

@Component
public class CaseApproveCommandHandler {

    @Autowired
    ApprovalService approvalService;

    @Autowired
    CaseService caseService;

    @Autowired
    TJLogProcessor tjLogProcessor;

    public CaseInfo approveCase(ApproveRequest approveRequest, ObaAuthentication authentication) {
        /*
         * check status APPVL only check approval service validate approvalStatus &
         * caseId & functionCode update ApprovalList if no remaining ApprovalInfo ->
         * status: OPEN save to db
         */

        ApprovalDto approval = approvalService.getApproval(approveRequest.getApprovalId())
                .orElseThrow(() -> new NotFoundException("Approval"));

        String caseId = "EC".equalsIgnoreCase(approveRequest.getFunctionCode()) ? approval.getCaseId() : authentication.getCaseId();
        boolean notSameCase = !Objects.equals(caseId, approval.getCaseId());
        boolean notSameFunctionCode = !Objects.equals(approveRequest.getFunctionCode(), approval.getFunctionCode());
        boolean isInvalidStatus = approval.getApprovalStatus() != ApprovalStatus.A
                && approval.getApprovalStatus() != ApprovalStatus.R;

        if (notSameCase || notSameFunctionCode || isInvalidStatus) {
            throw new ConflictException(CaseErrorCodes.CASE_INVALID_APPRL_STATUS.name(),
                    CaseErrorCodes.CASE_INVALID_APPRL_STATUS.getMessage());
        }

        return caseService.updateCase(caseId, caseInfo -> {
            caseService.updateApproval(caseInfo, approval);
            TJLogApproval tjLogApproval = TJLogApproval.builder().functionCode(approval.getFunctionCode())
                    .approvalRequired(approval.getApprovalRequired())
                    .approvalStatus(approval.getApprovalStatus().name()).approvalCount(approval.getApprovalCount())
                    .build();
            tjLogProcessor.logApprovalTxn(caseInfo, tjLogApproval);
        });
    }
}
