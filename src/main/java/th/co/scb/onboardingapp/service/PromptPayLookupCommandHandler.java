package th.co.scb.onboardingapp.service;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.entapi.docs_vchan.model.IDCard;
import th.co.scb.entapi.sales_services.CustomerServicesV2Api;
import th.co.scb.entapi.sales_services.model.PromptPayLookupResponseV2;
import th.co.scb.onboardingapp.helper.ObaHelper;
import th.co.scb.onboardingapp.model.PromptPayProxy;
import th.co.scb.onboardingapp.model.Proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class PromptPayLookupCommandHandler {

    @Autowired
    private CustomerServicesV2Api customerServicesV2Api;

    @Autowired
    EntApiConfig entApiConfig;
    @Autowired
    RestTemplate restTemplate;
    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;
    public PromptPayLookupResponseV2 lookupPromptPay(PromptPayProxy proxies) {
        List<String> reqProxyId = new ArrayList<>();
        List<String> reqProxyType = new ArrayList<>();
        for (Proxy p : proxies.getProxies()) {
            reqProxyType.add(p.getProxyType());
            reqProxyId.add(p.getProxyId());
        }
        return bulkLookupProxyStatus(reqProxyType.toArray(new String[0]), reqProxyId.toArray(new String[0]));
    }

    public PromptPayLookupResponseV2 bulkLookupProxyStatus(@NotNull String[] proxyType, @NotNull String[] proxyID) {


        if(isEntApiMode) {
            return customerServicesV2Api.bulkLookupProxyStatus(proxyType, proxyID);
        }else{

            String url = entApiConfig.getBasePath() +  "/v2/customerServices/promptPay/lookupStatus";;
            CompletableFuture<IDCard> future = new CompletableFuture<>();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    . queryParam("proxyType", (Object[]) proxyType)
                    .queryParam("proxyID", (Object[]) proxyID);
            ResponseEntity<PromptPayLookupResponseV2> result = restTemplate.exchange(builder.toUriString(),
                    org.springframework.http.HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null))
                    ,PromptPayLookupResponseV2.class);
            return result.getBody();

        }
    }
}
