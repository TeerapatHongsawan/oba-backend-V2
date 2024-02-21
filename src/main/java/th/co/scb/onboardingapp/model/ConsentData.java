package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ConsentData {
    private String consentSessionId;
    private String consentSessionTransactionId;
    private int sequence;
    private String stage;
    private String consentType;
    private String consentVersion;
    private String consentUpdateDate;
    private String consentStatus;
    private String vendor;
    private Map<String,String> content;
    private List<Scopes> scopes;
    private String updateStatus;
    private String  submissionInitiateStatusCode;
}
