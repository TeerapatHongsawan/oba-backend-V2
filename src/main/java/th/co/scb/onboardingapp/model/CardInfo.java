package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardInfo {
    private String engTitle;
    private String engFirstName;
    private String engMiddleName;
    private String engLastName;
    private String thaiTitle;
    private String thaiFirstName;
    private String thaiMiddleName;
    private String thaiLastName;
    private CardInfoAddress address;
    private String docType;
    private String docNo;
    private String expDate;
    private String issueDate;
    private String genderCode;
    private String dob;
    private String photo;
    private String laserCode;
    private String issueRequestNo;
    private boolean manualKeyIn;
    private byte[] smartcardImage;
    private String nationalityCode;
    private String countryOfIssue;
    private String oldDocNo;
}
