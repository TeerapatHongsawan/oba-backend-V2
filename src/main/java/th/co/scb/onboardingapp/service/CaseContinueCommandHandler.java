package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ConflictException;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.enums.CaseErrorCodes;
import th.co.scb.onboardingapp.model.enums.CaseStatus;

@Component
public class CaseContinueCommandHandler {

    @Autowired
    CaseService caseService;

    @Autowired
    RetrievalService retrievalService;

    public CaseInfo continueCase(ObaAuthentication authentication) {
        /*
         * check status OPEN only Fetch following in parallel (existing customer)
         * (Async) individuals/{id}/accounts Marketing Consent Marketing Info Contact
         * Channels Addresses EzApp
         */

        CaseInfo caseInfo = caseService.getCase(authentication.getCaseId());

        if (!CaseStatus.OPEN.getValue().equals(caseInfo.getCaseStatus()) && "N".equalsIgnoreCase(caseInfo.getCustomerInfo().getIsExistingCustomer())) {
            throw new ConflictException(CaseErrorCodes.CASE_INVALID_STATUS.name(),
                    CaseErrorCodes.CASE_INVALID_STATUS.getMessage());
        }

        if (caseInfo.getApprovalInfo().stream()
                .anyMatch(it -> !"A".equals(it.getApprovalStatus()) && (!it.getFunctionCode().equalsIgnoreCase("M_101")
                        && !it.getFunctionCode().equalsIgnoreCase("M_102")))) {
            throw new ConflictException(CaseErrorCodes.CASE_INVALID_APPRL_STATUS.name(),
                    CaseErrorCodes.CASE_INVALID_APPRL_STATUS.getMessage());
        }

        if ("Y".equals(caseInfo.getCustomerInfo().getIsExistingCustomer())) {
            this.retrievalService.fetchContinueExistingCustomerData(caseInfo).join();
        }

        this.caseService.postContinue(caseInfo);

        return caseService.updateCase(authentication.getCaseId(), updatedCaseInfo -> {
            updatedCaseInfo.setCustomerInfo(caseInfo.getCustomerInfo());
            updatedCaseInfo.setPortfolioInfo(caseInfo.getPortfolioInfo());
        });
    }

}
