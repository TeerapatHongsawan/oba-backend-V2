package th.co.scb.onboardingapp.service.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.entapi.alerts.ServicingAlertsRegistrationApi;
import th.co.scb.entapi.alerts.model.FeeResponse;
import th.co.scb.onboardingapp.helper.ObaHelper;

@Service
public class ServicingAlertsRegistrationApiService {

    @Autowired
    ServicingAlertsRegistrationApi servicingAlertsRegistrationApi;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EntApiConfig entApiConfig;

    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;

    public FeeResponse[] getFeeProfileByProductType(@NotNull String productType, @Nullable String feeType) {
        if(isEntApiMode) {
            return servicingAlertsRegistrationApi.getFeeProfileByProductType(productType, feeType);
        }else{
            ResponseEntity<FeeResponse[]> entity =  restTemplate.exchange(
                    entApiConfig.getBasePath() + "/v1/customerServices/alerts/feesByProductType?productType="+productType,
                    HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null)),
                    FeeResponse[].class);
            return entity.getBody();
        }
    }
}
