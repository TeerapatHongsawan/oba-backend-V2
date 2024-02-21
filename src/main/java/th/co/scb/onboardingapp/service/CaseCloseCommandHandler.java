package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ConflictException;
import th.co.scb.onboardingapp.model.enums.CaseErrorCodes;
import th.co.scb.onboardingapp.model.enums.CaseStatus;

@Component
public class CaseCloseCommandHandler {

    @Autowired
    private CaseService caseService;

    public void closeCase(ObaAuthentication authentication) {
        /*
         * check if status = OPEN|APPVL set status: CLOSE
         */
        if (StringUtils.isEmpty(authentication.getCaseId())) {
            throw new ConflictException(CaseErrorCodes.CASE_NOT_START.name(),
                    CaseErrorCodes.CASE_NOT_START.getMessage());
        }
        caseService.updateCase(authentication.getCaseId(), caseInfo -> {
            if (!CaseStatus.OPEN.getValue().equals(caseInfo.getCaseStatus())) {
                throw new ConflictException(CaseErrorCodes.CASE_INVALID_STATUS.name(),
                        CaseErrorCodes.CASE_INVALID_STATUS.getMessage());
            }

            caseInfo.setCaseStatus(CaseStatus.CLOSED.getValue());
        });
    }
}
