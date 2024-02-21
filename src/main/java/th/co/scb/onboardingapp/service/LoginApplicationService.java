package th.co.scb.onboardingapp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.DataItem;
import th.co.scb.onboardingapp.model.LoginRequest;
import th.co.scb.onboardingapp.model.TokenInfo;
import th.co.scb.onboardingapp.model.ValidateSessionAliveRequest;
import java.util.List;

@Service
public class LoginApplicationService {

    @Autowired
    private UserLoginCommandHandler userLoginCommandHandler;

    @Autowired
    private UserLogoutCommandHandler userLogoutCommandHandler;

    @Autowired
    private UserSwitchBranchCommandHandler userSwitchBranchCommandHandler;

    @Autowired
    private ValidateSessionCommandHandler validateSessionCommandHandler;
    public List<DataItem> login(LoginRequest loginRequest, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        return userLoginCommandHandler.login(loginRequest, httpServletResponse, httpServletRequest);
    }

    public boolean logout(String employeeId, String deviceUUID, String applicationName, ObaAuthentication auth) {
        return userLogoutCommandHandler.logout(employeeId, deviceUUID, applicationName, auth);
    }


    public void switchBranch(String branchId, String deviceIp, ObaAuthentication authentication, HttpServletResponse httpServletResponse) {
        userSwitchBranchCommandHandler.switchBranch(branchId, deviceIp, authentication, httpServletResponse);
    }

    public TokenInfo validateSession(ObaAuthentication authentication) {
        return validateSessionCommandHandler.validateSession(authentication);
    }

    public Boolean validateSessionAlive(ValidateSessionAliveRequest validateSessionAliveRequest) {
        return validateSessionCommandHandler.validateSessionAlive(validateSessionAliveRequest);
    }


}
