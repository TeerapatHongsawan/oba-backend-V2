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
import th.co.scb.entapi.custinfo.CustProfileCustinfoApi;
import th.co.scb.entapi.custinfo.model.Custinfos;
import th.co.scb.onboardingapp.helper.ObaHelper;

@Service
public class CustProfileApiService {

    @Autowired
    CustProfileCustinfoApi custProfileCustinfoApi;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EntApiConfig entApiConfig;

    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;

    public Custinfos listCustinfoByID(@NotNull String idNumber, @Nullable String idType) {

        if(isEntApiMode) {
            return custProfileCustinfoApi.listCustinfoByID(idNumber, idType);
        }else{
            ResponseEntity<Custinfos> entity =  restTemplate.exchange(
                    entApiConfig.getBasePath() + "/v1/party/cust-profile/custinfo/findByID?idNumber=" + idNumber + "&idType=" + idType,
                    HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null)),
                    Custinfos.class);
            return entity.getBody();
        }
    }
}
