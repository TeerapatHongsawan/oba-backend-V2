package th.co.scb.onboardingapp.service;

import io.github.openunirest.http.HttpMethod;
import io.github.openunirest.http.HttpResponse;
import io.github.openunirest.request.HttpRequestWithBody;
import kotlin.jvm.internal.Intrinsics;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.entapi.watchlist.ExternalAgencyWATCHLISTApi;
import th.co.scb.entapi.watchlist.model.WatchlistValidationRequest;
import th.co.scb.entapi.watchlist.model.WatchlistValidationResponseWithDetail;
import th.co.scb.entapi.watchlist.model.WatchlistValidationResultWithDetail;

import th.co.scb.indi.infrastructure.client.ApiException;
import th.co.scb.indi.infrastructure.client.RequestHeaderConfig;
import th.co.scb.indi.infrastructure.client.UnirestExtensionsKt;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.WatchlistRequest;
import th.co.scb.onboardingapp.model.WatchlistResponse;
import th.co.scb.vaultFaceTech.model.DecryptDataRequest;
import th.co.scb.vaultFaceTech.model.DecryptDataResponse;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static th.co.scb.onboardingapp.helper.ObaHelper.getHttpHeaders;


@Slf4j
@Service
public class WatchListService {

    private final ExternalAgencyWATCHLISTApi watchListApi;

    private final MappingHelper mappingHelper;

    private final TJLogProcessor tjLogProcessor;

    @Autowired
    RestTemplate restTemplate;


    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;
    @Autowired
    public WatchListService(ExternalAgencyWATCHLISTApi watchListApi, MappingHelper mappingHelper, TJLogProcessor tjLogProcessor) {
        this.watchListApi = watchListApi;
        this.mappingHelper = mappingHelper;
        this.tjLogProcessor = tjLogProcessor;
    }


    public List<WatchlistResponse> getPepInfo(@RequestBody WatchlistRequest[] data, ObaAuthentication auth) {
        List<WatchlistResponse> watchlistResponses;
        try {
            WatchlistValidationResponseWithDetail[] watchlistValidationResponseWithDetails = exactMatch(data);
            watchlistResponses = mappingHelper.mapAsList(Arrays.asList(watchlistValidationResponseWithDetails), WatchlistResponse.class);
            for (WatchlistResponse watchlistResponse : watchlistResponses) {
                if (watchlistResponse.getPersonDetails() != null) {
                    WatchlistValidationResultWithDetail hitPEP = Arrays.stream(watchlistResponse.getPersonDetails()).filter(res -> "PEP".equalsIgnoreCase(res.getFlagType())).findFirst().orElse(null);
                    if (hitPEP != null) {
                        tjLogProcessor.logTxnPep(auth.getCaseId(), "PEP");
                    } else {
                        tjLogProcessor.logTxnPep(auth.getCaseId(), "Pass");
                    }
                }
            }
        } catch (Exception ex) {
            tjLogProcessor.logTxnPep(auth.getCaseId(), "Fail");
            watchlistResponses = null;
        }
        return watchlistResponses;
    }


    public WatchlistValidationResponseWithDetail[] exactMatch(WatchlistValidationRequest[] validationReq) {
        if(isEntApiMode) {
            return watchListApi.exactMatch(validationReq);
        }else {
            String url =  watchListApi.getApiConfig().getBasePath() + "/v1/external-agency/watchlist/exactMatch";

            HttpEntity<WatchlistValidationRequest[]> request = new HttpEntity<>(validationReq,
                    getHttpHeaders(watchListApi.getApiConfig().getApisecret(), watchListApi.getApiConfig().getSourceSystem(), watchListApi.getApiConfig().getApikey(), null, null));

            WatchlistValidationResponseWithDetail[] watchlistValidationResponseWithDetails = restTemplate.postForObject(
                    url,
                    request, WatchlistValidationResponseWithDetail[].class);
            return watchlistValidationResponseWithDetails;
        }
    }

}
