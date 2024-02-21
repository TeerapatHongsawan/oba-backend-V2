package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.entapi.indikyc.CustProfileIndiKYCApi;
import th.co.scb.entapi.indikyc.model.PreCalculateKYCIndividualRequest;
import th.co.scb.entapi.indikyc.model.PreCalculateKYCIndividualResponse;
import th.co.scb.facetech.model.FaceVerificationRequest;
import th.co.scb.facetech.model.FaceVerificationResponse;
import th.co.scb.onboardingapp.helper.ObaHelper;

@Slf4j
@Component
public class KycCalculationCommandHandler {

    @Autowired
    private CustProfileIndiKYCApi custProfileIndiKYCApi;
    @Autowired
    RestTemplate restTemplate;

    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;
    public PreCalculateKYCIndividualResponse getPreCalculateKYCIndividualResponse(PreCalculateKYCIndividualRequest data) {
        log.info("CountrySourceOfIncome:" + data.getCountrySourceOfIncome());
        log.info("NationalityCode:" + data.getNationalityCode());
        log.info("OccupationCode:" + data.getOccupationCode());
        PreCalculateKYCIndividualResponse response = preCalculateKYCScore(data);

        log.info("KYC score" + response.getKycScore());
        return response;
    }

    public PreCalculateKYCIndividualResponse preCalculateKYCScore(PreCalculateKYCIndividualRequest data) {
        if(isEntApiMode) {
            return custProfileIndiKYCApi.preCalculateKYCScore(data);
        }else{
            org.springframework.http.HttpEntity<PreCalculateKYCIndividualRequest> request = new HttpEntity<>(data,
                    ObaHelper.getHttpHeaders(custProfileIndiKYCApi.getApiConfig().getApisecret(), custProfileIndiKYCApi.getApiConfig().getSourceSystem(), custProfileIndiKYCApi.getApiConfig().getApikey(), null,null));
            return restTemplate.postForObject(
                    custProfileIndiKYCApi.getApiConfig().getBasePath() + "/v1/party/cust-profile/indikyc/precalculation",
                    request, PreCalculateKYCIndividualResponse.class);

        }

    }
}
