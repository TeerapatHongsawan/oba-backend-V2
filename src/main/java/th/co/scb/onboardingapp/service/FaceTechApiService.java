package th.co.scb.onboardingapp.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.facetech.FaceTechApi;
import th.co.scb.facetech.model.ConsentValidationRequest;
import th.co.scb.facetech.model.ConsentValidationResponse;
import th.co.scb.facetech.model.FaceVerificationRequest;
import th.co.scb.facetech.model.FaceVerificationResponse;
import th.co.scb.fatca.FatcaApi;
import th.co.scb.fatca.model.ValidateFatcaRequest;
import th.co.scb.fatca.model.ValidateFatcaResponse;
import th.co.scb.onboardingapp.helper.ObaHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FaceTechApiService {

    @Autowired
    FaceTechApi faceTechApi;

    @Autowired
    RestTemplate restTemplate;

    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;
    public ConsentValidationResponse consentValidation(ConsentValidationRequest consentValidationRequest, String rmId, String acceptLanguage){
        if(isEntApiMode) {
            return faceTechApi.consentValidation(consentValidationRequest, rmId,acceptLanguage);
        }else{
            org.springframework.http.HttpEntity<ConsentValidationRequest> request = new HttpEntity<>(consentValidationRequest,
                    ObaHelper.getHttpHeaders(faceTechApi.getApiConfig().getApisecret(), faceTechApi.getApiConfig().getSourceSystem(), faceTechApi.getApiConfig().getApikey(), acceptLanguage,null));
            return restTemplate.postForObject(
                    faceTechApi.getApiConfig().getBasePath() + "/v1/support/utility/faceTec/consent/validate",
                    request, ConsentValidationResponse.class);
        }
    }

    public FaceVerificationResponse faceCompareVerification(FaceVerificationRequest faceVerificationRequest, String rmId){
        if(isEntApiMode) {
            return faceTechApi.faceCompareVerification(faceVerificationRequest, rmId);
        }else{
            org.springframework.http.HttpEntity<FaceVerificationRequest> request = new HttpEntity<>(faceVerificationRequest,
                    ObaHelper.getHttpHeaders(faceTechApi.getApiConfig().getApisecret(), faceTechApi.getApiConfig().getSourceSystem(), faceTechApi.getApiConfig().getApikey(), null,null));


            return restTemplate.postForObject(
                    faceTechApi.getApiConfig().getBasePath() + "/v1/support/utility/faceTec/image/comparison",
                    request, FaceVerificationResponse.class);

        }
    }
}
