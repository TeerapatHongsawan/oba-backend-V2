package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.otp.TxnAuthOTPApi;
import th.co.scb.entapi.otp.model.DeliveryChannelDataModel;
import th.co.scb.entapi.otp.model.GenerationRequestDataModel;
import th.co.scb.entapi.otp.model.GenerationResponseDataModel;
import th.co.scb.facetech.model.FaceVerificationRequest;
import th.co.scb.facetech.model.FaceVerificationResponse;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ConflictException;
import th.co.scb.onboardingapp.exception.NotFoundException;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.helper.ObaHelper;
import th.co.scb.onboardingapp.model.CustomerContactChannel;
import th.co.scb.onboardingapp.model.CustomerInfo;
import th.co.scb.onboardingapp.model.GenerateOtpRequest;
import th.co.scb.onboardingapp.model.GenerateOtpResponse;
import th.co.scb.onboardingapp.model.entity.OtpEntity;
import th.co.scb.onboardingapp.repository.OtpJpaRepository;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class GenerateOtpCommandHandler {

    @Autowired
    private CaseLibraryService caseLibraryService;

    @Autowired
    private MappingHelper mappingHelper;

    @Autowired
    private OtpJpaRepository otpRepository;

    @Autowired
    private TJLogProcessor tjLogProcessor;

//    @Autowired
//    private TxnAuthOTPApi otpApi;
    @Autowired
    OtpApiService otpApiService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;

    private static final String CHANNEL_TYPE = "SMS";
    private static final String POLICYID = "SCB_Generic_OTPPolicy";
    private static final String OTP_INVALID_MOBILE_NO = "OTP_INVALID_MOBILE_NO";
    private static final String OTP_INVALID_MOBILE_NO_DESC = "required - otpmobile:VALUE:INVALID";
    private static final String OTP_ALREADY_SENT = "OTP_ALREADY_SENT";
    private static final String OTP_ALREADY_SENT_DESC = "caseId already sent OTP , please try again in a few second";
    private static final String DOCTYPE_ALIEN = "P7";
    private static final String DOCTYPE_PASSPORT = "P8";

    @Value("${indi.otp-delay-seconds}")
    private long otpDelayValue;

    @Value("${indi.otp.message}")
    private String otpMessage;

    @Value("${indi.otp.message.en}")
    private String otpMessageEn;

    public GenerateOtpResponse getGenerateOtpResponse(GenerateOtpRequest data, ObaAuthentication authentication) {
        if (!strIsNumericWithLength(data.getOtpMobile(), 10)) {
            throw new ConflictException(OTP_INVALID_MOBILE_NO, OTP_INVALID_MOBILE_NO_DESC);
        }

        // check existing mobile number
        CustomerInfo customerInfo = caseLibraryService.getCustomerInfo(authentication.getCaseId())
                .orElseThrow(() -> new NotFoundException("CASE ID : " + authentication.getCaseId()));

        List<CustomerContactChannel> contactChannels = customerInfo.getContactChannels();
        if (contactChannels == null || contactChannels.isEmpty()) {
            throw new NotFoundException("Contact Channel For case ID : " + authentication.getCaseId());
        }

        if (contactChannels.stream().noneMatch(c -> "002".equals(c.getContactTypeCode())
                && getMobileNumber(data.getOtpMobile()).equals(getMobileNumber(c.getContactNumber())))) {
            throw new NotFoundException("Mobile Number : " + data.getOtpMobile());
        }

         /*
            check if this caseId already sent OTP within 2 seconds
            save tokenUUID to db
         */
        // get Opt by case from DB
        Date today = new Date();
        OtpEntity otpCase = otpRepository.findById(authentication.getCaseId()).orElse(null);
        if (otpCase != null) {
            long diffTime = (today.getTime() - otpCase.getCreatedDate().getTime()) / 1000;
            if (diffTime < otpDelayValue) {
                throw new ConflictException(OTP_ALREADY_SENT, OTP_ALREADY_SENT_DESC);
            }
        }

        // send API
        GenerationRequestDataModel request = new GenerationRequestDataModel();
        request.setPolicyId(POLICYID);
        request.setOtpMessage(isForeigner(customerInfo.getDocType()) ? otpMessageEn : otpMessage);
        DeliveryChannelDataModel channel = new DeliveryChannelDataModel();
        channel.setChannelType(CHANNEL_TYPE);
        channel.setChannelAttrib1(data.getOtpMobile());
        request.setDeliveryChannels(new DeliveryChannelDataModel[]{channel});
        GenerationResponseDataModel response = otpApiService.generateOTP(request);

        tjLogProcessor.logTxnSendOTP(authentication.getCaseId(), response, data.getOtpMobile());
        // save token to db
        OtpEntity otpNew = new OtpEntity();
        otpNew.setCaseId(authentication.getCaseId());
        otpNew.setMobileNo(data.getOtpMobile());
        otpNew.setToken(response.getTokenUUID());
        otpNew.setReferenceNo(response.getReference());
        otpNew.setDuraiton(response.getValidDuration() + "");
        otpNew.setCreatedDate(new java.sql.Timestamp(today.getTime()));
        otpRepository.save(otpNew);

        // return response from API
        return mappingHelper.map(response, GenerateOtpResponse.class);
    }

    private boolean strIsNumericWithLength(String value, int len) {
        if (value == null || value.length() != len) {
            return false;
        }
        for (char c : value.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private String getMobileNumber(String mobileNumber) {
        return mobileNumber.trim().replace("-", "").replace("(", "").replace(")", "");
    }

    private boolean isForeigner(String docType) {
        return Arrays.stream(new String[]{DOCTYPE_ALIEN, DOCTYPE_PASSPORT}).anyMatch(s -> s.equalsIgnoreCase(docType));
    }

}
