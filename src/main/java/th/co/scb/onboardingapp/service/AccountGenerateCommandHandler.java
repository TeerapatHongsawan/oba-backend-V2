package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.deposits.AccountsDepositsApi;
import th.co.scb.entapi.deposits.model.GenDepAcctRequest;
import th.co.scb.entapi.deposits.model.GenDepAcctResponse;
import th.co.scb.facetech.model.FaceVerificationRequest;
import th.co.scb.facetech.model.FaceVerificationResponse;
import th.co.scb.onboardingapp.helper.ObaHelper;

@Component
public class AccountGenerateCommandHandler {

    @Autowired
    private AccountsDepositsApi accountsDepositsApi;
    @Autowired
    RestTemplate restTemplate;

    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;
    public GenDepAcctResponse generateAccount(GenDepAcctRequest request) {
        return genDepositAccount(request);
    }

    public GenDepAcctResponse genDepositAccount(GenDepAcctRequest request){
        if(isEntApiMode) {
            return accountsDepositsApi.genDepositAccount(request);
        }else{
           HttpEntity<GenDepAcctRequest> request2 = new HttpEntity<>(request,
                    ObaHelper.getHttpHeaders(accountsDepositsApi.getApiConfig().getApisecret(), accountsDepositsApi.getApiConfig().getSourceSystem(), accountsDepositsApi.getApiConfig().getApikey(), null,null));

            return restTemplate.postForObject(
                    accountsDepositsApi.getApiConfig().getBasePath() + "/v1/accounts/deposits/generation",
                    request2, GenDepAcctResponse.class);

        }
    }
}
