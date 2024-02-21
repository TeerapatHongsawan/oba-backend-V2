package th.co.scb.onboardingapp.service.cseretrieval;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.individuals.CustProfileIndividualsApi;
import th.co.scb.entapi.individuals.model.IndividualV3;
import th.co.scb.entapi.individuals.model.PersonalMarketingInformation;

import th.co.scb.indi.infrastructure.client.ApiException;
import th.co.scb.onboardingapp.helper.ObaHelper;
import th.co.scb.onboardingapp.model.CaseInfo;

import java.util.concurrent.CompletableFuture;

@Service
@CaseContinueExistingQualifier
@ForeignerCaseContinueExistingQualifier
public class MarketingInfoRetrievalPlugin extends BaseRetrievalPlugin<PersonalMarketingInformation> {

    @Autowired
    private CustProfileIndividualsApi custProfileIndividualsApi;
    @Autowired
    RestTemplate restTemplate;



    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;
    @Override
    protected CompletableFuture<PersonalMarketingInformation> retrieveCase(CaseInfo caseInfo) {
        return getIndividualsMarketingInfoAsync(caseInfo.getReferenceId());
    }

    @Override
    protected void updateCase(CaseInfo caseInfo, PersonalMarketingInformation personalMarketingInformation) {
        caseInfo.getCustomerInfo().setMarketingInfo(personalMarketingInformation);
    }

    @Override
    protected void handleError(CaseInfo caseInfo, Throwable ex) {
        if (ex instanceof ApiException && ((ApiException) ex).getStatusCode() == 404) {
            caseInfo.getCustomerInfo().setMarketingInfo(new PersonalMarketingInformation());
        } else {
            super.handleError(caseInfo, ex);
        }
    }

    private CompletableFuture<PersonalMarketingInformation> getIndividualsMarketingInfoAsync(String partnerID){
        if(isEntApiMode) {
            return custProfileIndividualsApi.getIndividualsMarketingInfoAsync(partnerID);
        }else{
            CompletableFuture<PersonalMarketingInformation> future = new CompletableFuture<>();
            CompletableFuture.runAsync(() -> {
                try {
                    ResponseEntity<PersonalMarketingInformation> result = restTemplate.exchange(
                            custProfileIndividualsApi.getApiConfig().getBasePath() +  "/v1/party/cust-profile/individuals/{partnerID}/marketingInfo",
                            HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(custProfileIndividualsApi.getApiConfig().getApisecret(), custProfileIndividualsApi.getApiConfig().getSourceSystem(), custProfileIndividualsApi.getApiConfig().getApikey(), null,null))
                            ,PersonalMarketingInformation.class,partnerID);

                    future.complete(result.getBody());
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });

            return future;
        }

    }
}