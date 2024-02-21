package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsentContentInfo {
    private String consentType;
    private String consentVersion;
    private LocalDateTime consentUpdateDate;
    private String content;
    private String contentEN;
    private String contentName;
    private String contentTitle;
    private String consentStatus;
}
