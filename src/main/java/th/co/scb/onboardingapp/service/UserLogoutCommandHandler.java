package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.repository.LoginSessionRepository;

@Component
public class UserLogoutCommandHandler {

    @Autowired
    private LoginSessionRepository loginSessionRepository;

    @Autowired
    private ObaAuthenticationProvider authenticationProvider;

    public boolean logout(String employeeId, String deviceUUID, String appName, ObaAuthentication auth) {
        authenticationProvider.logoutSessionToThirdApp(employeeId, auth);
        boolean isSuccess = loginSessionRepository.deleteDataLoginSession(employeeId, deviceUUID, appName);
        return isSuccess;
    }
}
