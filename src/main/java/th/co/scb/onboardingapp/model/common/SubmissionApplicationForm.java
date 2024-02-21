package th.co.scb.onboardingapp.model.common;

import lombok.Data;
import th.co.scb.onboardingapp.model.AppForm;

import java.util.List;

@Data
public class SubmissionApplicationForm {
    private String uploadSessionId;
    private String docType;
    private String docNameTH;
    private String docNameEN;
    private String accountNumber;
    private String mimeType;
    private List<AppForm> appForms;
}
