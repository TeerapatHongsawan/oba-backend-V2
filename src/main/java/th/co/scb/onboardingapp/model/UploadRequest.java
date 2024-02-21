package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class UploadRequest {
    private String docObj;
    private String mimeType;
    private CaseInfo caseInfo;
}
