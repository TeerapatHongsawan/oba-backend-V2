package th.co.scb.onboardingapp.model.deposite;

import lombok.Builder;
import lombok.Data;
import th.co.scb.onboardingapp.model.AppFormModel;

import java.util.List;

@Data
@Builder
public class CDDReport extends AppFormModel {
    private String passportNo;
    private String nationality;
    private String dob;
    private String firstNameTH;
    private String middleNameTH;
    private String lastNameTH;
    private String firstNameEN;
    private String middleNameEN;
    private String lastNameEN;
    private String address;
    private String tel;
    private List<CDDResult> cddResultList;
    private String alertID;
    private List<Detica> deticaList;
    private String branch;
    private String staffID;
    private String app;
    private String transactionName;
    private String description;
}