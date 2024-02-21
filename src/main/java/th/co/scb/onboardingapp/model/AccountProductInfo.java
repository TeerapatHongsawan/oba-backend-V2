package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountProductInfo {
    private String productCode;
    private String productType;
    private String productName;
    private String productNameEN;
    private String email;
    private String accountCode;
    private String accountType;
    private String indiProductCode;
    private String beneficiaryRelation;
    private String additionalService;
    private String cbsProductCode;
    private String fcdFlag;
    private AccountAdditionalInfo additionalInfo;
}
