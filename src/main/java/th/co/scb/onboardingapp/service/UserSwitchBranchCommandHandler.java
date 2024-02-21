package th.co.scb.onboardingapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ErrorCodes;
import th.co.scb.onboardingapp.exception.InternalErrorException;
import th.co.scb.onboardingapp.exception.UnauthorizedException;
import th.co.scb.onboardingapp.model.InitSessionRequest;
import th.co.scb.onboardingapp.model.StartbizUserProfilesDetail;
import th.co.scb.onboardingapp.model.TokenInfo;
import th.co.scb.onboardingapp.model.TokenInitialSessionRequest;
import th.co.scb.onboardingapp.service.api.SessionApiService;
import lombok.extern.slf4j.Slf4j;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;


@Component
@Slf4j
public class UserSwitchBranchCommandHandler {

    @Autowired
    private ObaAuthenticationProvider authenticationProvider;

    @Autowired
    private GeneralParamService generalParamService;

    @Autowired
    private ObaTokenService tokenService;

    @Autowired
    private SessionApiService sessionApiService;

    public void switchBranch(String branchId, String deviceIp, ObaAuthentication auth, HttpServletResponse response) {
        ObaAuthentication authenticate = authenticate(auth.getName(), branchId, auth.getAppName(), auth.getStb());
        TokenInfo token = genToken(response, authenticate);
        TokenInitialSessionRequest tokenJson = new TokenInitialSessionRequest();
        tokenJson.setIpAddr(deviceIp);
        tokenJson.setToken(token.getAccessToken());
        try{
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(tokenJson);
            InitSessionRequest request = new InitSessionRequest();
            request.setUsername(auth.getName());
            request.setToken(json);
            sessionApiService.initialSessionToIOnboard(request);
        } catch (JsonProcessingException e){
            throw new InternalErrorException(ErrorCodes.S00001.name(), ErrorCodes.S00001.getMessage());
        }
    }

    private TokenInfo genToken(HttpServletResponse response, ObaAuthentication newAuth) {
        TokenInfo token = tokenService.create(newAuth);
        response.setHeader("X-TOKEN", token.getAccessToken());
        return token;
    }

    private ObaAuthentication authenticate(String username, String branchId, String appName, StartbizUserProfilesDetail stb) {
        validateLoginBranch(branchId);
        return authenticationProvider.authenticate(username, branchId, appName, stb);
    }

    private void validateLoginBranch(String branchId) {
        if (!isAllowLoginBranch(branchId)) {
            throw new UnauthorizedException(ErrorCodes.B00016.name(), ErrorCodes.B00016.getMessage());
        }
    }

    private boolean isAllowLoginBranch(String branchId) {
        Set<String> loginBranches = generalParamService.getAllowLoginBranchGeneralParam();
        log.info("branchId : {}", branchId);
        log.info("loginBranches : {}", loginBranches);
        return isEmpty(loginBranches) || loginBranches.contains(branchId);
    }
}
