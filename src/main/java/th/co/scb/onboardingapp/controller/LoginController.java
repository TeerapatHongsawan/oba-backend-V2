package th.co.scb.onboardingapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.helper.ObaHelper;
import th.co.scb.onboardingapp.model.DataItem;
import th.co.scb.onboardingapp.model.LoginRequest;
import th.co.scb.onboardingapp.model.SwitchBranchRequest;
import th.co.scb.onboardingapp.model.TokenInfo;
import th.co.scb.onboardingapp.model.ValidateSessionAliveRequest;
import th.co.scb.onboardingapp.service.LoginApplicationService;
import java.util.List;

@RestController
@Validated
public class LoginController {

    @Autowired
    private LoginApplicationService loginApplicationService;

    @PostMapping("/login")
    public List<DataItem> login(@Valid @RequestBody LoginRequest data, HttpServletResponse response, HttpServletRequest request) {
        return loginApplicationService.login(data, response, request);
    }

    @PostMapping("/v2/login")
    public ResponseEntity<List<DataItem>> loginV2(@Valid @RequestBody LoginRequest data, HttpServletResponse response, HttpServletRequest request) {
        return ResponseEntity.ok()
                .headers(ObaHelper.httpServletResponseToHttpHeaders(response))
                .body(loginApplicationService.login(data, response, request));

    }

    @GetMapping("/logout/{employeeId}/{deviceUUID}")
    public Boolean logout(String employeeId, String deviceUUID, String appName, ObaAuthentication authentication) {
        return loginApplicationService.logout(employeeId, deviceUUID, appName, authentication);
    }

    @PostMapping("/switch-branch")
    public void switchBranch(@RequestBody SwitchBranchRequest data, ObaAuthentication authentication, HttpServletResponse response) {
        loginApplicationService.switchBranch(data.getBranchId(), data.getDeviceIp(), authentication, response);
    }

    @PostMapping("/validate")
    public TokenInfo validate(ObaAuthentication authentication) {
        return loginApplicationService.validateSession(authentication);
    }
    @PostMapping("/validateSessionAlive")
    public Boolean validateSessionAlive(@RequestBody ValidateSessionAliveRequest data) {
        return loginApplicationService.validateSessionAlive(data);
    }

}
