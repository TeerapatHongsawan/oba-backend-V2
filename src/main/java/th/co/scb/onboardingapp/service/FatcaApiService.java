package th.co.scb.onboardingapp.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.fatca.FatcaApi;
import th.co.scb.fatca.model.ValidateFatcaRequest;
import th.co.scb.fatca.model.ValidateFatcaResponse;
import th.co.scb.onboardingapp.helper.ObaHelper;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FatcaApiService {

    @Autowired
    FatcaApi fatcaApi;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EntApiConfig entApiConfig;

    @Value("${spring.profiles.active:-}")
    List<String> activeProfile;

    public CompletableFuture validateFatcaAsync(@NotNull ValidateFatcaRequest validateFatcaRequest) {
        if(!activeProfile.contains("local")) {
            return fatcaApi.validateFatcaAsync(validateFatcaRequest);
        }else{
            HttpEntity<ValidateFatcaRequest> request = new HttpEntity<>(validateFatcaRequest,
                    ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null));
            CompletableFuture<ValidateFatcaResponse> future = new CompletableFuture<>();

            CompletableFuture.runAsync(() -> {
                try {
                    ValidateFatcaResponse validateFatcaResponse = restTemplate.postForObject(
                            entApiConfig.getBasePath() + "/v2/external-agency/fatca/validation",
                            request, ValidateFatcaResponse.class);

                    future.complete(validateFatcaResponse);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
            return future;
        }
    }
}
