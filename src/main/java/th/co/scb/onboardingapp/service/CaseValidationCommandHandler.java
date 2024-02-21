package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.SmartValidator;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ConflictException;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.enums.CaseErrorCodes;

@Component
public class CaseValidationCommandHandler {

    @Autowired
    private CaseService caseService;

    @Autowired(required = false)
    private SmartValidator validator;

    public void validateCase(ObaAuthentication authentication) {
        CaseInfo caseInfo = caseService.getCase(authentication.getCaseId());
        validateData(caseInfo);
    }

    private void validateData(CaseInfo caseInfo) {
        DataBinder binder = new DataBinder(caseInfo);
        binder.setValidator(validator);
        binder.validate();
        BindingResult result = binder.getBindingResult();
        if (result.hasErrors()) {
            throw new ConflictException(CaseErrorCodes.CASE_DATA_INVALID.name(),
                    CaseErrorCodes.CASE_DATA_INVALID.getMessage());
        }
    }
}
