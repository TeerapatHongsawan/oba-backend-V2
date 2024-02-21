package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class GenerateOtpRequest {
    private String otpMobile;
    private String otpType;
}

