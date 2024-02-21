package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.entapi.otp.model.ValidationResponseDataModel;

import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.GenerateOtpRequest;
import th.co.scb.onboardingapp.model.GenerateOtpResponse;
import th.co.scb.onboardingapp.model.ValidateOtpRequest;

@Service
public class OtpApplicationService {

    @Autowired
    private GenerateOtpCommandHandler generateOtpCommandHandler;

    @Autowired
    private OtpValidationCommandHandler otpValidationCommandHandler;

    public GenerateOtpResponse getGenerateOtpResponse(GenerateOtpRequest data, ObaAuthentication authentication) {
        return generateOtpCommandHandler.getGenerateOtpResponse(data, authentication);
    }

//    public ValidationResponseDataModel validateOtp(ValidateOtpRequest validateOtpRequest, ObaAuthentication authentication) {
//        return otpValidationCommandHandler.validateOtp(validateOtpRequest,authentication);
//    }
}
