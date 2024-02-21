package th.co.scb.onboardingapp.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ConflictException;
import th.co.scb.onboardingapp.exception.NotFoundException;
import th.co.scb.onboardingapp.helper.TokenHelper;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.SummaryEvent;
import th.co.scb.onboardingapp.model.enums.CaseErrorCodes;
import th.co.scb.onboardingapp.model.LoginBranchInfo;


@Component
public class CaseLoadCommandHandler {

    @Autowired
    CaseService caseService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    TokenHelper tokenHelper;

    private static final String EXCEPTION_MESSAGE = "ApprovalBranch";

    public CaseInfo loadCase(String caseId, ObaAuthentication authentication, HttpServletResponse httpServletResponse) {
        /*
         * check if same branch add Set-caseId header to response
         */
        CaseInfo caseInfo = this.caseService.getCase(caseId);
        String approvalBranchId = this.employeeService.getLoginBranch(authentication.getName(), authentication.getBranchId())
                .map(LoginBranchInfo::getApprovalBranchId).orElseThrow(() -> new NotFoundException(EXCEPTION_MESSAGE));
        String caseApprovalBranchId = this.employeeService.getLoginBranch(caseInfo.getEmployeeId(), caseInfo.getBranchId())
                .map(LoginBranchInfo::getApprovalBranchId).orElseThrow(() -> new NotFoundException(EXCEPTION_MESSAGE));

        if (!caseApprovalBranchId.startsWith(approvalBranchId) || StringUtils.isEmpty(approvalBranchId)) {
            throw new ConflictException(CaseErrorCodes.CASE_INVALID_BRANCH.name(),
                    CaseErrorCodes.CASE_INVALID_BRANCH.getMessage());
        }

        if (caseInfo.getCaseStatus().equals("ONBD")) {
            this.applicationEventPublisher.publishEvent(new SummaryEvent("ADD", "Product & Service Submission", caseInfo));
        }

        tokenHelper.setToken(authentication, httpServletResponse, caseInfo);

        return caseInfo;
    }
}
