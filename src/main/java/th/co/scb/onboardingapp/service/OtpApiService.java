package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.otp.TxnAuthOTPApi;
import th.co.scb.entapi.otp.model.GenerationRequestDataModel;
import th.co.scb.entapi.otp.model.GenerationResponseDataModel;
import th.co.scb.entapi.otp.model.ValidationRequestDataModel;
import th.co.scb.entapi.otp.model.ValidationResponseDataModel;
import th.co.scb.facetech.FaceTechApi;
import th.co.scb.facetech.model.ConsentValidationRequest;
import th.co.scb.facetech.model.ConsentValidationResponse;
import th.co.scb.facetech.model.FaceVerificationRequest;
import th.co.scb.facetech.model.FaceVerificationResponse;
import th.co.scb.onboardingapp.helper.ObaHelper;

@Service
public class OtpApiService {

    @Autowired
    private TxnAuthOTPApi otpApi;

    @Autowired
    RestTemplate restTemplate;

    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;
    public GenerationResponseDataModel generateOTP(GenerationRequestDataModel data) {
        if(isEntApiMode) {
               return otpApi.generateOTP(data);
        }else{
            HttpEntity<GenerationRequestDataModel> request = new HttpEntity<>(data,
                    ObaHelper.getHttpHeaders(otpApi.getApiConfig().getApisecret(), otpApi.getApiConfig().getSourceSystem(), otpApi.getApiConfig().getApikey(), null,null));
            return restTemplate.postForObject(
                    otpApi.getApiConfig().getBasePath() + "/v1/txnAuth/OTP/generation",
                    request, GenerationResponseDataModel.class);
        }
    }

    public ValidationResponseDataModel validateOTP(ValidationRequestDataModel data){
        if(isEntApiMode) {
            return otpApi.validateOTP(data);
        }else{
            HttpEntity<ValidationRequestDataModel> request = new HttpEntity<>(data,
                    ObaHelper.getHttpHeaders(otpApi.getApiConfig().getApisecret(), otpApi.getApiConfig().getSourceSystem(), otpApi.getApiConfig().getApikey(), null,null));


            return restTemplate.postForObject(
                    otpApi.getApiConfig().getBasePath() + "/v1/txnAuth/OTP/validation",
                    request, ValidationResponseDataModel.class);

        }
    }
}
