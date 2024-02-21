package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class TokenInitialSessionRequest {
    private String ipAddr;
    private String token;
}
