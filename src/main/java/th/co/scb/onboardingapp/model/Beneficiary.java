package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class Beneficiary {
    private String orderNo;
    private String titleName;
    private String firstName;
    private String lastName;
    private String relationship;
    private String relationshipOther;
    private String mobileNumber;
    private String idCardNumber;
    private int percentage;
    private String docType;
}
