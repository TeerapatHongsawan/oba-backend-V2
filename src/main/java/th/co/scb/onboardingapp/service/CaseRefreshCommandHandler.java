package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ConflictException;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.enums.CaseErrorCodes;

@Component
public class CaseRefreshCommandHandler {

    @Autowired
    private CaseService caseService;

    public CaseInfo refreshCase(ObaAuthentication authentication) {
        if (StringUtils.isEmpty(authentication.getCaseId())) {
            throw new ConflictException(CaseErrorCodes.CASE_NOT_START.name(), CaseErrorCodes.CASE_NOT_START.getMessage());
        }

        return caseService.getCase(authentication.getCaseId());
    }
}
