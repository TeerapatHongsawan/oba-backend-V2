package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ConflictException;
import th.co.scb.onboardingapp.model.enums.CaseErrorCodes;

import java.time.LocalDateTime;

@Component
public class CaseStampCaseFulfilmentDateCommandHandler {

    @Autowired
    private CaseService caseService;

    public void caseStampCaseFulfilmentDate(ObaAuthentication authentication) {

        if (StringUtils.isEmpty(authentication.getCaseId())) {
            throw new ConflictException(CaseErrorCodes.CASE_NOT_START.name(),
                    CaseErrorCodes.CASE_NOT_START.getMessage());
        }

        caseService.updateCase(authentication.getCaseId(), caseInfo -> caseInfo.setFulfilmentDate(LocalDateTime.now()));
    }
}
