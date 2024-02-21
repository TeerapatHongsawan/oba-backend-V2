package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class ProductItem {
    private String accountNumber;
    private String accountNumberDisp;
    private String accountType;
    private String accountCode;
    private String productCode;
    private String productName;
    private String productNameEN;
    private String productType;
    private String email;
    private String objectiveCode;
    private String productTypeDesc;
    private AdditionalServices additionalServices;

    public ProductItem() {
    }
}
