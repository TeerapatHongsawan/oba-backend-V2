package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.facetech.FaceTechApi;
import th.co.scb.facetech.model.ConsentValidationRequest;
import th.co.scb.facetech.model.ConsentValidationResponse;
import th.co.scb.facetech.model.FaceVerificationRequest;
import th.co.scb.facetech.model.FaceVerificationResponse;
import th.co.scb.onboardingapp.helper.ObaHelper;
import java.util.UUID;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class FaceTechConsentValidateService {

    @Autowired
    FaceTechApiService faceTechApiService;

    public ConsentValidationResponse getConsentValidate(String referenceId) {
        ConsentValidationRequest consentValidationRequest = new ConsentValidationRequest();
        String[] consentType = {"001"};
        consentValidationRequest.setConsentType(consentType);
        String rmId = nonNull(referenceId) ? ObaHelper.formatRmId(referenceId) : UUID.randomUUID().toString();
        return faceTechApiService.consentValidation(consentValidationRequest, rmId, "th");
    }


}