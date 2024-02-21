package th.co.scb.onboardingapp.helper;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.TokenInfo;
import th.co.scb.onboardingapp.service.ObaTokenService;

@Service
public class TokenHelper {

    @Autowired
    ObaTokenService tokenService;

    public void setToken(ObaAuthentication authentication, HttpServletResponse httpServletResponse, CaseInfo caseInfo) {
        authentication.setCaseId(caseInfo.getCaseId());
        TokenInfo token = tokenService.create(authentication);
        httpServletResponse.setHeader("X-TOKEN", token.getAccessToken());
    }
}
