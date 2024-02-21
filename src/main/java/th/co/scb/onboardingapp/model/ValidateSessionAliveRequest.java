package th.co.scb.onboardingapp.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ValidateSessionAliveRequest {
    @NotNull
    private String username;

    @NotNull
    private String deviceId;

    @NotNull
    private String appName;

}
