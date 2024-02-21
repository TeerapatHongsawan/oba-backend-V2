package th.co.scb.onboardingapp.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.*;

import java.util.List;

@Slf4j
@Service
public class CaseApplicationService {

    @Autowired
    CaseCreateCommandHandler caseCreateCommandHandler;

    @Autowired
    CaseContinueCommandHandler caseContinueCommandHandler;

    @Autowired
    CaseSaveCommandHandler caseSaveCommandHandler;

    @Autowired
    CaseApproveCommandHandler caseApproveCommandHandler;

    @Autowired
    CaseLoadCommandHandler caseLoadCommandHandler;

    @Autowired
    CaseCloseCommandHandler caseCloseCommandHandler;

    @Autowired
    CaseRefreshCommandHandler caseRefreshCommandHandler;
    @Autowired
    private CaseApprovedListCommandHandler caseApprovedListCommandHandler;

    @Autowired
    private CaseValidationCommandHandler caseValidationCommandHandler;

    @Autowired
    private CaseStampCaseFulfilmentDateCommandHandler caseStampCaseFulfilmentDateCommandHandler;

    public CaseInfo createCase(CaseRequest caseRequest, ObaAuthentication authentication, HttpServletResponse httpServletResponse) {
        return caseCreateCommandHandler.createCase(caseRequest, authentication, httpServletResponse);
    }

    public CaseInfo continueCase(ObaAuthentication authentication) {
        return caseContinueCommandHandler.continueCase(authentication);
    }

    public void saveCase(CaseInfo caseInfo) {
        caseSaveCommandHandler.saveCase(caseInfo);
    }

    public CaseInfo approveCase(ApproveRequest approveRequest, ObaAuthentication authentication) {
        return caseApproveCommandHandler.approveCase(approveRequest, authentication);
    }

    public CaseInfo loadCase(String caseId, ObaAuthentication authentication, HttpServletResponse httpServletResponse) {
        return caseLoadCommandHandler.loadCase(caseId, authentication, httpServletResponse);
    }

    public void closeCase(ObaAuthentication authentication) {
        caseCloseCommandHandler.closeCase(authentication);
    }

    public CaseInfo refreshCase(ObaAuthentication authentication) {
        return caseRefreshCommandHandler.refreshCase(authentication);
    }
    public List<CaseItem> getCaseApprovedList(ObaAuthentication authentication) {
        return caseApprovedListCommandHandler.getCaseApprovedList(authentication);
    }

    public void validateCase(ObaAuthentication authentication) {
        caseValidationCommandHandler.validateCase(authentication);
    }

    public void caseStampCaseFulfilmentDate(ObaAuthentication authentication) {
        caseStampCaseFulfilmentDateCommandHandler.caseStampCaseFulfilmentDate(authentication);
    }

}
