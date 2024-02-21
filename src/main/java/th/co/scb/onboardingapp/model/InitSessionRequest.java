package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class InitSessionRequest {
    private String username;
    private String token;
}

