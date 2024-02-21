package th.co.scb.onboardingapp.service.api;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.entapi.external_agency_dopa.ExternalAgencyDOPAApi;
import th.co.scb.entapi.external_agency_dopa.model.FindByCardInfoRequest;
import th.co.scb.entapi.external_agency_dopa.model.FindByCardInfoResponse;
import th.co.scb.onboardingapp.helper.ObaHelper;
import java.util.concurrent.CompletableFuture;

@Service
public class DopaApiService {

    @Autowired
    ExternalAgencyDOPAApi dopaApi;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EntApiConfig entApiConfig;

    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;

    public CompletableFuture dOPAFindByCardInfoV2Async(@NotNull FindByCardInfoRequest requestBody) {
        if(isEntApiMode) {
            return dopaApi.dOPAFindByCardInfoV2Async(requestBody);
        }else{
            HttpEntity<FindByCardInfoRequest> request = new HttpEntity<>(requestBody,
                    ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null));
            CompletableFuture<FindByCardInfoResponse> future = new CompletableFuture<>();

            CompletableFuture.runAsync(() -> {
                try {
                    FindByCardInfoResponse findByCardInfoResponse = restTemplate.postForObject(
                            entApiConfig.getBasePath() + "/v2/external-agency/dopa/citizenIDStatus/findByCardInfo",
                            request, FindByCardInfoResponse.class);

                    future.complete(findByCardInfoResponse);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
            return future;
        }
    }
}
