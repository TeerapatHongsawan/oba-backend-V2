package th.co.scb.onboardingapp.model;

import lombok.Data;
import th.co.scb.entapi.fatca.model.FATCAW9Form;

@Data
public class W9FormDetails extends FATCAW9Form {
    private String customerNameThai;
    private String requesterNameAddress;
    private String listAccountNo;
}
