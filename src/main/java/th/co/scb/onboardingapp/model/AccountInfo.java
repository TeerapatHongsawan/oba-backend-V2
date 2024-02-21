package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
    private String accountCode;
    private String accountName;
    private String accountNameEn;
    private String accountTypeCode;
    private String accountDetails;
    private String accountDetailsEn;
    private String singleSelect;
    private List<AccountProductInfo> products;
    private String saleSheetLink;
    private String saleSheetLinkFCD;
    private String saleSheetLinkEn;
    private AccountInfoAdditionalDetails additionalDetails;
    private AccountInfoAdditionalDetails additionalDetailsFCD;
}
