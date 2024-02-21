package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class GenerateOtpResponse {
    private Integer validDuration;
    private String otp;
    private String otpMessage;
    private String reference;
}
