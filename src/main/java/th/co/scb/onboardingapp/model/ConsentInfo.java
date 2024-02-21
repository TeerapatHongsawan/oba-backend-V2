package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.List;

@Data
public class ConsentInfo {
    private String consentName;
    private String consentStatus;
    private String consentType;
    private String consentVersion;
    private String consentVersionRef;
    private Content content;
    private String contentSubType;
    private String createDate;
    private String documentId;
    private List<IndustryInfo> industryInfo;
    private List<ObjectiveInfo> objectiveInfo;
    private String productCode;
    private String productName;
    private String revisitedDate;
    private List<Scope> scopes;
    private String updateStatus;
    private String updateDesc;
}
