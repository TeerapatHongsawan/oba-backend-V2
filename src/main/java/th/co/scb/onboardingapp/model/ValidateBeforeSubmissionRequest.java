package th.co.scb.onboardingapp.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ValidateBeforeSubmissionRequest {

    @NotNull
    private String username;

    @NotNull
    private String appName;

    @NotNull
    private String deviceId;

    @NotNull
    private String deviceIp;

    private String token;

}
