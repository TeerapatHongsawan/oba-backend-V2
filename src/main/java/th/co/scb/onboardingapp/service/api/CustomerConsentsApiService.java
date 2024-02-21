package th.co.scb.onboardingapp.service.api;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.entapi.csent_customer_consent.CustomerConsentsV2Api;
import th.co.scb.entapi.csent_customer_consent.model.InitiateRequestv2;
import th.co.scb.entapi.csent_customer_consent.model.InitiateResponsev2;
import th.co.scb.entapi.custinfo.model.Custinfos;
import th.co.scb.entapi.detica.model.DeticaValidationRequest;
import th.co.scb.entapi.detica.model.DeticaValidationResponse;
import th.co.scb.onboardingapp.helper.ObaHelper;

import java.util.concurrent.CompletableFuture;

@Service
public class CustomerConsentsApiService {

    @Autowired
    CustomerConsentsV2Api customerConsentsV2Api;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EntApiConfig entApiConfig;

    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;


    public CompletableFuture initiateV2Async(@NotNull String acceptLanguage, @NotNull InitiateRequestv2 body) {
        if(isEntApiMode) {
            return customerConsentsV2Api.initiateV2Async(acceptLanguage, body);
        }else{

            HttpEntity<InitiateRequestv2> request = new HttpEntity<>(body,
                    ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), acceptLanguage,null));
            CompletableFuture<InitiateResponsev2> future = new CompletableFuture<>();

            CompletableFuture.runAsync(() -> {
                try {
                    InitiateResponsev2 initiateResponsev2 = restTemplate.postForObject(
                            entApiConfig.getBasePath() + "/v2/customer/consents/initiate",
                            request, InitiateResponsev2.class);

                    future.complete(initiateResponsev2);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });

            return future;

        }
    }
}
