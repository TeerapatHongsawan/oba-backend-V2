package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.scb.entapi.otp.TxnAuthOTPApi;
import th.co.scb.entapi.otp.model.ValidationRequestDataModel;
import th.co.scb.entapi.otp.model.ValidationResponseDataModel;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ConflictException;
import th.co.scb.onboardingapp.model.ValidateOtpRequest;
import th.co.scb.onboardingapp.model.entity.OtpEntity;
import th.co.scb.onboardingapp.repository.OtpJpaRepository;


@Component
public class OtpValidationCommandHandler {

    @Autowired
    private OtpJpaRepository otpRepository;

    @Autowired
    private TxnAuthOTPApi otpApi;

    @Autowired
    private TJLogProcessor tjLogProcessor;

    private static final String OTP_NOT_FOUND = "OTP_NOT_FOUND";
    private static final String OTP_NOT_FOUND_DESC = "Not found otp token for this case id: ";

//    public ValidationResponseDataModel validateOtp(ValidateOtpRequest data, ObaAuthentication auth) {
//        OtpEntity optCase = this.otpRepository.findById(auth.getCaseId()).orElse(null);
//        if (optCase == null) {
//            throw new ConflictException(OTP_NOT_FOUND, OTP_NOT_FOUND_DESC + auth.getCaseId());
//        }
//
//        ValidationRequestDataModel request = new ValidationRequestDataModel();
//        request.setOtp(data.getOtp());
//        request.setTokenUUID(optCase.getToken());
//        this.tjLogProcessor.logTxnEnterOTP(auth.getCaseId(), optCase.getToken());
//
//        return otpApi.validateOTP(request);
//    }
}
