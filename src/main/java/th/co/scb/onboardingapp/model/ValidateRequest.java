package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class ValidateRequest {
    private String username;
    private String secret;
}
