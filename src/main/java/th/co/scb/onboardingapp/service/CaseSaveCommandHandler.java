package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.exception.ConflictException;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.enums.CaseErrorCodes;
import th.co.scb.onboardingapp.model.enums.CaseStatus;

@Component
public class CaseSaveCommandHandler {

    @Autowired
    CaseService caseService;

    public void saveCase(CaseInfo caseInfo) {
        /*
         * check if status = OPEN
         */
        caseService.updateCase(caseInfo.getCaseId(), loadedCaseInfo -> {
            if (!CaseStatus.OPEN.getValue().equals(loadedCaseInfo.getCaseStatus())) {
                throw new ConflictException(CaseErrorCodes.CASE_INVALID_STATUS.name(),
                        CaseErrorCodes.CASE_INVALID_STATUS.getMessage());
            }

            loadedCaseInfo.setCustomerInfo(caseInfo.getCustomerInfo());
            loadedCaseInfo.setProductInfo(caseInfo.getProductInfo());
            loadedCaseInfo.setDocumentInfo(caseInfo.getDocumentInfo());
            loadedCaseInfo.setPortfolioInfo(caseInfo.getPortfolioInfo());
            loadedCaseInfo.setBookingBranchId(caseInfo.getBookingBranchId());
            loadedCaseInfo.setAdditionalInfo(caseInfo.getAdditionalInfo());
        });
    }
}
