package th.co.scb.onboardingapp.model;

import lombok.Data;
import th.co.scb.onboardingapp.model.common.AppFormDocumentType;

@Data
public class AppForm {
    private String accountNumber;
    private AppFormDocumentType docType;
    private String dateTime;
    private String branchName;
    private String applicationNo;
    private int pageNumber;
    private int totalPage;
    private String formName;
    private int pageOrder;
    private byte[] pdfFormPage;
}
