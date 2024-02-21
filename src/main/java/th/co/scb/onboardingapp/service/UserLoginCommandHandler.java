package th.co.scb.onboardingapp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ErrorCodes;
import th.co.scb.onboardingapp.exception.InternalErrorException;
import th.co.scb.onboardingapp.exception.UnauthorizedException;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.repository.LoginSessionRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
public class UserLoginCommandHandler {

    @Autowired
    private ServerVersionService serverVersionService;

    @Autowired
    private SecurityLogger securityLogger;

    @Autowired
    private GeneralParamService generalParamService;

    @Autowired
    private ObaAuthenticationProvider authenticationProvider;

    @Autowired
    private LoginSessionRepository loginSessionRepository;

    @Autowired
    private Environment environment;

    @Autowired
    private ObaTokenService tokenService;

    @Autowired
    private EmployeeService employeeService;

    @Value("${devices.check.enable:true}")
    private boolean devicesCheckEnable;

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String ACTIVE = "active";

    public List<DataItem> login(LoginRequest loginRequest, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        if (!"web".equals(loginRequest.getDeviceId()) || !environment.acceptsProfiles("hack")) {
            serverVersionService.checkVersion(loginRequest.getVersion());
        }
        GeneralParam isVerifyMultiLogin = generalParamService.getVerifyMultiLogin();
        boolean isVerify = (isVerifyMultiLogin.getValue().get("isVerify") != null) && ("true".equals(isVerifyMultiLogin.getValue().get("isVerify").toString()));
        try {
            List<DataItem> branchList = authenticationProvider.getEmployeeBranch(loginRequest.getUsername(), loginRequest.getPassword());
            branchList.forEach(item -> log.info("Username:[{}]  IOnboard/IProfile branch: {}", loginRequest.getUsername(), item));

            StartbizUserProfilesDetail stb = employeeService.getStartBizEmp(loginRequest.getUsername());
            log.info("Username:[{}]  Startbiz: {}", loginRequest.getUsername(), stb);

            if (devicesCheckEnable) {
                if (("web".equals(loginRequest.getDeviceId())) && (!branchList.isEmpty()) && (stb == null)) {
                    throw new UnauthorizedException(ErrorCodes.NOT_ALLOWED_DEVICES.name(), ErrorCodes.NOT_ALLOWED_DEVICES.getMessage());
                }
            }

            ObaAuthentication auth = authenticate(loginRequest, branchList, stb);


            List<DataLoginSession> verifyMultiLogin = authenticationProvider.getLoginSession(loginRequest.getUsername(), loginRequest.getAppName());
            String token = loginRequest.getToken();
            String deviceId = loginRequest.getDeviceId();
            //verifyMultiLogin check Duplicate EmployeeId
            if (!verifyMultiLogin.isEmpty()) {
                String employeeId = verifyMultiLogin.get(0).getEmployeeId();
                String oldDeviceId = verifyMultiLogin.get(0).getDeviceId();
                LocalDateTime lastActivityTime = verifyMultiLogin.get(0).getLastActivityTime();
                LocalDateTime ldt = LocalDateTime.now();
                long diff = SECONDS.between(lastActivityTime, ldt);

                //If lastActivityTime between 3.33 min system will update dateTime.
                if (diff <= 200) {
                    if (deviceId.equals(oldDeviceId) && loginRequest.getAppName().equals(verifyMultiLogin.get(0).getAppName())) {
                        this.updateDataLoginSession(employeeId, ldt, deviceId, loginRequest.getAppName());
                    } else if (deviceId.equals(oldDeviceId) && !loginRequest.getAppName().equalsIgnoreCase(verifyMultiLogin.get(0).getAppName())) {
                        this.insertDataLoginSession(loginRequest.getUsername(), LocalDateTime.now(), loginRequest.getDeviceId(), ACTIVE, loginRequest.getAppName(), token);
                        return branchList;
                    } else {
                        throw new UnauthorizedException(ErrorCodes.BLOCKING_MULTI_LOGIN.name(), ErrorCodes.BLOCKING_MULTI_LOGIN.getMessage());
                    }
                } else if (diff > 200 && !deviceId.equals(oldDeviceId)) {
                    if (!loginRequest.getIsConfirmLoginDuplicateUser()) {
                        List<DataItem> alertMsgDuplicateLogin = new ArrayList<>();
                        DataItem dataItem = new DataItem();
                        dataItem.setText("CONFIRM_TO_LOGIN");
                        dataItem.setCode("000000");
                        alertMsgDuplicateLogin.add(dataItem);

                        return alertMsgDuplicateLogin;
                    } else {
                        this.updateDataLoginSession(employeeId, ldt, deviceId, loginRequest.getAppName());
                    }
                } else {
                    if (loginRequest.getAppName().equals(verifyMultiLogin.get(0).getAppName())) {
                        this.updateDataLoginSession(employeeId, ldt, deviceId, loginRequest.getAppName());
                    } else {
                        this.insertDataLoginSession(loginRequest.getUsername(), LocalDateTime.now(), loginRequest.getDeviceId(), ACTIVE, loginRequest.getAppName(), token);
                        return branchList;
                    }
                }
            }
            TokenInfo tokenInfo = genToken(httpServletResponse, auth);
            authenticationProvider.initialSessionToThirdApp(loginRequest, auth, tokenInfo);
            securityLogger.log(loginRequest.getAppName(), loginRequest.getUsername(), "Login", httpServletRequest, loginRequest.getDeviceId(), true, "");
            //INSERT DATA.
            if (verifyMultiLogin.isEmpty() && isVerify) {
                this.insertDataLoginSession(loginRequest.getUsername(), LocalDateTime.now(), loginRequest.getDeviceId(), ACTIVE, loginRequest.getAppName(), token);
            }
            return branchList;
        } catch (UnauthorizedException ex) {
            securityLogger.log(loginRequest.getAppName(), loginRequest.getUsername(), "Login", httpServletRequest, loginRequest.getDeviceId(), false, ex.getMessage());
            throw ex;
        } catch (ResourceAccessException e) {
            throw new InternalErrorException(ErrorCodes.S00001.name(), ErrorCodes.S00001.getMessage());
        }
    }

    private ObaAuthentication authenticate(LoginRequest loginRequest, List<DataItem> branchList, StartbizUserProfilesDetail stb) {
        if (branchList.isEmpty() && stb == null) {
            log.error("User [{}] does not exist in IOnboard/IProfile and Startbiz", loginRequest.getUsername());
            throw new UnauthorizedException(ErrorCodes.B00016.name(), ErrorCodes.B00016.getMessage());
        }

        String username = loginRequest.getUsername();
        if (branchList.size() == 1) {
            String branchId = branchList.get(0).getCode();
            return authenticate(username, branchId, loginRequest.getAppName(), stb);
        } else {
            return new ObaAuthentication(username, Collections.emptySet(), null, null, loginRequest.getAppName(), stb);
        }
    }

    private ObaAuthentication authenticate(String username, String branchId, String appName, StartbizUserProfilesDetail stb) {
        validateLoginBranch(branchId);
        return authenticationProvider.authenticate(username, branchId, appName, stb);
    }

    private void insertDataLoginSession(String employeeId, LocalDateTime lastActivityTime, String deviceId, String status, String appName, String token) {
        loginSessionRepository.insertDataLoginSession(employeeId, lastActivityTime.format(dateTimeFormatter), deviceId, status, appName, token);
    }

    private void updateDataLoginSession(String employeeId, LocalDateTime ldt, String deviceId, String appName) {
        loginSessionRepository.updateDataLoginSession(employeeId, ldt.format(dateTimeFormatter), deviceId, appName);
    }

    private TokenInfo genToken(HttpServletResponse response, ObaAuthentication newAuth) {
        TokenInfo token = tokenService.create(newAuth);
        response.setHeader("X-TOKEN", token.getAccessToken());
        return token;
    }

    private void validateLoginBranch(String branchId) {
        if (!isAllowLoginBranch(branchId)) {
            log.info("Not allow branch login!");
            throw new UnauthorizedException(ErrorCodes.B00016.name(), ErrorCodes.B00016.getMessage());
        }
    }

    private boolean isAllowLoginBranch(String branchId) {
        Set<String> loginBranches = generalParamService.getAllowLoginBranchGeneralParam();
        return isEmpty(loginBranches) || loginBranches.contains(branchId);
    }


}
