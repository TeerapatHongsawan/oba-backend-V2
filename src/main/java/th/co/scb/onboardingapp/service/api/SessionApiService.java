package th.co.scb.onboardingapp.service.api;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import th.co.scb.onboardingapp.model.InitSessionRequest;

import java.util.List;

@Service
public class SessionApiService {
    @Autowired
    RestTemplate restTemplate;

    @Value("${spring.profiles.active:-}")
    List activeProfile;

    @Value("${api.iOnboard.basePath}")
    String iOnboardBasePath;

    @Value("${api.startBiz.basePath}")
    String startBizBasePath;

    @Value("${api.iOnboard.initial.session}")
    String iOnboardInitSession;

    @Value("${api.startBiz.initial.session}")
    String startBizInitSession;
    public boolean initialSessionToIOnboard(@NotNull InitSessionRequest requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(!Boolean.valueOf(iOnboardInitSession)) {
            return true;
        }else{
            boolean isSuccess = false;
            HttpEntity<InitSessionRequest> request = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<String> insertSession = restTemplate.exchange(
                        iOnboardBasePath + "/case/initialSessionThirdParty", HttpMethod.POST, request, String.class);
                if(insertSession.hasBody() && insertSession.getStatusCode().is2xxSuccessful() && insertSession.getBody().equalsIgnoreCase("success")){
                    isSuccess = true;
                }
            } catch (HttpClientErrorException e) {
                throw e;
            } catch (Exception e) {
                throw e;
            }
            return isSuccess;
        }
    }

    public boolean validateSessionToIOnboard(@NotNull InitSessionRequest requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(!Boolean.valueOf(iOnboardInitSession)) {
            return true;
        }else{
            boolean isSuccess = false;
            HttpEntity<InitSessionRequest> request = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<String> insertSession = restTemplate.exchange(
                        iOnboardBasePath + "/case/validateSessionThirdParty", HttpMethod.POST, request, String.class);
                if(insertSession.hasBody() && insertSession.getStatusCode().is2xxSuccessful() && insertSession.getBody().equalsIgnoreCase("success")){
                    isSuccess = true;
                }
            } catch (HttpClientErrorException e) {
                throw e;
            } catch (Exception e) {
                throw e;
            }
            return isSuccess;
        }
    }

    public boolean logoutSessionToIOnboard(@NotNull InitSessionRequest requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(!Boolean.valueOf(iOnboardInitSession)) {
            return true;
        }else{
            boolean isSuccess = false;
            HttpEntity<InitSessionRequest> request = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<String> insertSession = restTemplate.exchange(
                        iOnboardBasePath + "/case/logoutSessionThirdParty", HttpMethod.POST, request, String.class);
                if(insertSession.hasBody() && insertSession.getStatusCode().is2xxSuccessful() && insertSession.getBody().equalsIgnoreCase("success")){
                    isSuccess = true;
                }
            } catch (HttpClientErrorException e) {
                throw e;
            } catch (Exception e) {
                throw e;
            }
            return isSuccess;
        }
    }

    public boolean initialSessionToStartBiz(@NotNull InitSessionRequest requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(!Boolean.valueOf(startBizInitSession)) {
            return true;
        }else{
            boolean isSuccess = false;
            HttpEntity<InitSessionRequest> request = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<String> insertSession = restTemplate.exchange(
                        startBizBasePath + "/portalserver/initialSessionThirdParty", HttpMethod.POST, request, String.class);
                if(insertSession.hasBody() && insertSession.getStatusCode().is2xxSuccessful() && insertSession.getBody().equalsIgnoreCase("success")){
                    isSuccess = true;
                }
            } catch (HttpClientErrorException e) {
                throw e;
            } catch (Exception e) {
                throw e;
            }
            return isSuccess;
        }
    }

    public boolean validateSessionToStartBiz(@NotNull InitSessionRequest requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(!Boolean.valueOf(startBizInitSession)) {
            return true;
        }else{
            boolean isSuccess = false;
            HttpEntity<InitSessionRequest> request = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<String> insertSession = restTemplate.exchange(
                        startBizBasePath + "/portalserver/validateSessionThirdParty", HttpMethod.POST, request, String.class);
                if(insertSession.hasBody() && insertSession.getStatusCode().is2xxSuccessful() && insertSession.getBody().equalsIgnoreCase("success")){
                    isSuccess = true;
                }
            } catch (HttpClientErrorException e) {
                throw e;
            } catch (Exception e) {
                throw e;
            }
            return isSuccess;
        }
    }

    public boolean logoutSessionToStartBiz(@NotNull InitSessionRequest requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(!Boolean.valueOf(startBizInitSession)) {
            return true;
        }else{
            boolean isSuccess = false;
            HttpEntity<InitSessionRequest> request = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<String> insertSession = restTemplate.exchange(
                        startBizBasePath + "/portalserver/logoutSessionThirdParty", HttpMethod.POST, request, String.class);
                if(insertSession.hasBody() && insertSession.getStatusCode().is2xxSuccessful() && insertSession.getBody().equalsIgnoreCase("success")){
                    isSuccess = true;
                }
            } catch (HttpClientErrorException e) {
                throw e;
            } catch (Exception e) {
                throw e;
            }
            return isSuccess;
        }
    }

}
