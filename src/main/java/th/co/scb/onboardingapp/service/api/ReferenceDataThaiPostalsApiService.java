package th.co.scb.onboardingapp.service.api;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.entapi.ref_postals.ReferenceDataThaiPostalsApi;
import th.co.scb.entapi.ref_postals.model.ThaiPostals;
import th.co.scb.onboardingapp.helper.ObaHelper;

@Service
public class ReferenceDataThaiPostalsApiService {

    @Autowired
    ReferenceDataThaiPostalsApi referenceDataThaiPostalsApi;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EntApiConfig entApiConfig;

    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;

    public ThaiPostals getThaiPostals(@Nullable Integer pagingOffset, @Nullable Integer pagingLimit) {
        if(isEntApiMode) {
            return referenceDataThaiPostalsApi.getThaiPostals(pagingOffset, pagingLimit);
        }else{
            ResponseEntity<ThaiPostals> entity =  restTemplate.exchange(
                    entApiConfig.getBasePath() + "/v1/referenceData/common/thaiPostals",
                    HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null)),
                    ThaiPostals.class);
            return entity.getBody();
        }
    }
}
