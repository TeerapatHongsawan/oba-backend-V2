package th.co.scb.onboardingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import th.co.scb.entapi.otp.model.ValidationResponseDataModel;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.GenerateOtpRequest;
import th.co.scb.onboardingapp.model.GenerateOtpResponse;
import th.co.scb.onboardingapp.model.ValidateOtpRequest;
import th.co.scb.onboardingapp.service.OtpApplicationService;


@RestController
public class OtpController {

    @Autowired
    private OtpApplicationService otpApplicationService;

    @PostMapping("/api/otp/generate")
    public GenerateOtpResponse generate(@RequestBody GenerateOtpRequest data, ObaAuthentication auth) {
        return otpApplicationService.getGenerateOtpResponse(data, auth);
    }

//    @PostMapping("/api/otp/validate")
//    public ValidationResponseDataModel validate(@RequestBody ValidateOtpRequest data, ObaAuthentication auth) {
//        return otpApplicationService.validateOtp(data, auth);
//    }
}