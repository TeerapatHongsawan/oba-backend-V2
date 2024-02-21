package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OtpEmail {

    @JsonProperty("contactEmail")
    private String contactEmail;

    @JsonProperty("refcode")
    private String refcode;

    @JsonProperty("counter")
    private String counter;

    @JsonProperty("otpStatus")
    private String otpStatus;

    @JsonProperty("waitingPeriod")
    private String waitingPeriod;
}