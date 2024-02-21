package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DataLoginSession {
    private String employeeId;
    private LocalDateTime lastActivityTime;
    private String deviceId;
    private String status;
    private String appName;
    private String token;
}