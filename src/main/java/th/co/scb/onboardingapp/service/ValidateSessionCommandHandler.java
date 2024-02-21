package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.repository.LoginSessionJpaRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

@Component
@Slf4j
public class ValidateSessionCommandHandler {

    @Autowired
    private ObaAuthenticationProvider authenticationProvider;

    @Autowired
    private ObaTokenService tokenService;

    @Autowired
    private LoginSessionJpaRepository loginSessionRepository;

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TokenInfo validateSession(ObaAuthentication authentication) {
        String userName = authentication.getName();
        String appName = authentication.getAppName();

        List<DataLoginSession> verifyMultiLogin = authenticationProvider.getLoginSession(userName, appName);
        if (!verifyMultiLogin.isEmpty()) {
            String employeeId = verifyMultiLogin.get(0).getEmployeeId();
            LocalDateTime lastActivityTime = verifyMultiLogin.get(0).getLastActivityTime();
            LocalDateTime ldt = LocalDateTime.now();
            long diff = SECONDS.between(lastActivityTime, ldt);

            if (diff <= 200) {
                loginSessionRepository.updateDataValidateLoginSession(employeeId, ldt, appName);
            }
        }
        TokenInfo token = tokenService.create(authentication);
        authenticationProvider.validateSessionToThirdApp(userName, authentication, token);
        return token;
    }
    public Boolean validateSessionAlive(ValidateSessionAliveRequest data) {
        String userName = data.getUsername();
        String deviceId = data.getDeviceId();
        String appName = data.getAppName();
        return isSessionExist(deviceId, null, userName, appName);
    }



    private Boolean isSessionExist(String deviceId, ValidateBeforeSubmissionRequest data, String userName, String appName) {
        String user;
        if (data == null) {
            user = userName;
        } else {
            user = data.getUsername();
        }
        List<DataLoginSession> verifyMultiLogin = authenticationProvider.getLoginSession(user, appName);
        if (!verifyMultiLogin.isEmpty()) {
            String oldDeviceId = verifyMultiLogin.get(0).getDeviceId();
            LocalDateTime ldt = LocalDateTime.now();
            if ((!deviceId.equals(oldDeviceId))) {
                return false;
            } else {

                String formattedLdt = ldt.format(dateTimeFormatter);
                log.info("User {} , Time {} , appName {}",user,formattedLdt,appName);
                loginSessionRepository.updateDataValidateLoginSession(user, ldt, appName);
                return true;
            }
        }
        return true;
    }

}
