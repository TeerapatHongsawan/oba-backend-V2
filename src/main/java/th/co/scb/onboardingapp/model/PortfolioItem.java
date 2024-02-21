package th.co.scb.onboardingapp.model;

import lombok.Data;
import th.co.scb.entapi.custinfo.model.CustomerAccount;
import th.co.scb.entapi.deposits.model.AcctInfo;

@Data
public class PortfolioItem extends CustomerAccount {
    private AcctInfo additionalAccountInfo;
    private String email;
    private Command command;
    private String subAccountType;
    private String cbsProductCode;
    private String subProductType;
    private String onlineOpenFlag;
    private String multiTermFlag;
    private String productName;
    private String parentAccount;
    private String ePassCbsProductCode;
    private String epassProductCode;
    private String epassProductName;
}