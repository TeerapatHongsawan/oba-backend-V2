package th.co.scb.onboardingapp.model.deposite;

import lombok.Data;
import th.co.scb.onboardingapp.model.AppFormModel;

@Data
public class KYC101Form extends AppFormModel {
    private String purposeSalary;
    private String purposeBusiness;
    private String purposeConsumption;
    private String approverName;
}
