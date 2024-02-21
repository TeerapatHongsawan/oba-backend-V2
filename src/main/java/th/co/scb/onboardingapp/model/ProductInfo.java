package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import th.co.scb.entapi.deposits.model.AccountAddress;

import java.util.List;

@Data
public class ProductInfo extends EcInfo {

    @JsonProperty("additionalServices")
    private AdditionalServices additionalServices;

    @JsonProperty("recommendFlag")
    private boolean recommendFlag;

    @JsonProperty("accountCode")
    private String accountCode;

    @JsonProperty("accountShortName")
    private String accountShortName;

    @JsonProperty("accountAddress")
    private AccountAddress accountAddress;

    @JsonProperty("selectedReason")
    private String selectedReason;

    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("accountNumberDisp")
    private String accountNumberDisp;

    @JsonProperty("additionalServiceFlag")
    private String additionalServiceFlag;

    @JsonProperty("cbsProductCode")
    private String cbsProductCode;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("objectiveCode")
    private String objectiveCode;

    private String objectiveCodeOther;

    @JsonProperty("productCode")
    private String productCode;

    @JsonProperty("emailFlag")
    private String emailFlag;

    @JsonProperty("accountLongName")
    private String accountLongName;

    @JsonProperty("productNameEN")
    private String productNameEN;

    @JsonProperty("beneRelFlag")
    private String beneRelFlag;

    @JsonProperty("applicationId")
    private String applicationId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("productType")
    private String productType;

    @JsonProperty("originalRequestUID")
    private String originalRequestUID;

    private List<Beneficiary> beneficiaryList;
    private String indiProductCode;

    private String accountType;

    private Integer depositAmount;
    private boolean taxExempt;
    private boolean backDateFlag;
    private String backDate;

    private String packageProduct;
    private String BOT48DeclarationStatus;
    private String currencyCode;
    private String currencyDesc;
    private boolean serviceChargeWaive;
    private String objectiveType;
    private String obligationType;

}