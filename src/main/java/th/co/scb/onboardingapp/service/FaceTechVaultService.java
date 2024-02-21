package th.co.scb.onboardingapp.service;

import io.github.openunirest.http.HttpResponse;
import io.github.openunirest.request.HttpRequestWithBody;
import kotlin.jvm.internal.Intrinsics;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.entapi.ref_common.model.OccupationSubTypes;
import th.co.scb.indi.infrastructure.client.ApiException;
import th.co.scb.indi.infrastructure.client.UnirestExtensionsKt;
import th.co.scb.onboardingapp.config.ext.service.VaultFaceTechApiServiceConfig;
import th.co.scb.onboardingapp.helper.ObaHelper;

import th.co.scb.vaultFaceTech.VaultFaceTechApi;
import th.co.scb.vaultFaceTech.model.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static th.co.scb.onboardingapp.helper.ObaHelper.getHttpHeaders;

@Service
@Slf4j
public class FaceTechVaultService {

    @Autowired
    private VaultFaceTechApi vaultFaceTechApi;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EntApiConfig entApiConfig;
    @Autowired
    VaultFaceTechApiServiceConfig vaultConfig;
    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;

    @Cacheable(value = "faceTechKey", unless = "#result == null")
    public String getFaceTechKey() {
     //   LoginResponse loginResponse = vaultFaceTechApi.login();
        LoginResponse loginResponse = login();

        DataRequest dataRequest = new DataRequest();
        dataRequest.setToken(Objects.requireNonNull(loginResponse.getAuth()).getClientToken());
      //  DataResponse dataResponse = vaultFaceTechApi.getData(dataRequest);
        DataResponse dataResponse =  getData(dataRequest);

        DecryptDataRequest decryptDataRequest = new DecryptDataRequest();
        decryptDataRequest.setToken(Objects.requireNonNull(loginResponse.getAuth()).getClientToken());
        decryptDataRequest.setCiphertext(dataResponse.getData().getData().getFaceTechSecretKey());
     //   DecryptDataResponse decryptDataResponse =  vaultFaceTechApi.decryptData(decryptDataRequest);
        DecryptDataResponse decryptDataResponse =  decryptData(decryptDataRequest);
        return new String(Base64.decodeBase64(Objects.requireNonNull(decryptDataResponse.getData()).getPlaintext()));
    }

    public LoginResponse login() {
        if(isEntApiMode) {
            return vaultFaceTechApi.login();
        }else {
            LoginRequest requestBody = new LoginRequest();
            requestBody.setRoleId(vaultConfig.getVaultFacetechRoleId());
            requestBody.setSecretId(vaultConfig.getVaultFacetechSecretId());

            HttpEntity<LoginRequest> request = new HttpEntity<>(requestBody,
                    getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null, null));

            LoginResponse loginResponse = restTemplate.postForObject(
                    vaultConfig.getVaultFacetechServer() + vaultConfig.getVaultFacetechLoginUrl(),
                    request, LoginResponse.class);

            log.debug("LoginResponse -> {}", loginResponse);

            return loginResponse;
        }

    }
//
    public DataResponse getData( DataRequest dataRequest) {
        if(isEntApiMode) {
            return vaultFaceTechApi.getData(dataRequest);
        }else {
        Map<String,String> header = new HashMap<>();
        header.put("X-Vault-Token", dataRequest.getToken());
        HttpEntity<DataRequest> request = new HttpEntity<>(dataRequest,
                getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,header));
        String url = vaultConfig.getVaultFacetechServer() + vaultConfig.getVaultFacetechSecretUrl();
        ResponseEntity<DataResponse> response =  restTemplate.exchange(
                url,
                HttpMethod.GET, request,
                DataResponse.class);
            return response.getBody();
        }
    }

    public DecryptDataResponse decryptData( DecryptDataRequest dataRequest) {
        if(isEntApiMode) {
            return vaultFaceTechApi.decryptData(dataRequest);
        }else {
            String url = vaultConfig.getVaultFacetechServer() + vaultConfig.getVaultFacetechTransitUrl();
            Map<String, String> header = new HashMap<>();
            header.put("X-Vault-Token", dataRequest.getToken());
            HttpEntity<DecryptDataRequest> request = new HttpEntity<>(dataRequest,
                    getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null, header));

            DecryptDataResponse decryptDataResponse = restTemplate.postForObject(
                    url,
                    request, DecryptDataResponse.class);
            return decryptDataResponse;
        }
    }
}
