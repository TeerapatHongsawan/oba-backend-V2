package th.co.scb.onboardingapp.service.api;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.entapi.detica.ExternalAgencyDETICAApi;
import th.co.scb.entapi.detica.model.DeticaValidationRequest;
import th.co.scb.entapi.detica.model.DeticaValidationResponse;
import th.co.scb.onboardingapp.helper.ObaHelper;
import java.util.concurrent.CompletableFuture;

@Service
public class DeticaApiService {

    @Autowired
    ExternalAgencyDETICAApi externalAgencyDETICAApi;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EntApiConfig entApiConfig;

    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;

    public CompletableFuture validateBlacklistAsync(@NotNull DeticaValidationRequest[] validationReq) {
        if(isEntApiMode) {
            return externalAgencyDETICAApi.validateBlacklistAsync(validationReq);
        }else{
            HttpEntity<DeticaValidationRequest[]> request = new HttpEntity<>(validationReq,
                    ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null));
            CompletableFuture<DeticaValidationResponse[]> future = new CompletableFuture<>();

            CompletableFuture.runAsync(() -> {
                try {
                    DeticaValidationResponse[] deticaValidationResponses = restTemplate.postForObject(
                            entApiConfig.getBasePath() + "/v1/external-agency/detica/blacklist/validation",
                            request, DeticaValidationResponse[].class);

                    future.complete(deticaValidationResponses);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });

            return future;
        }

    }
}
