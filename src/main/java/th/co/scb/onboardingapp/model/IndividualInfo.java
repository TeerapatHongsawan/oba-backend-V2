package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class IndividualInfo {

    @JsonProperty("addresses")
    private List<CustomerAddress> addresses;

    @JsonProperty("engTitle")
    private String engTitle;

    @JsonProperty("occupationIsicCode")
    private String occupationIsicCode;

    @JsonProperty("occupationDescription")
    private String occupationDescription;

    @JsonProperty("customerTypeCode")
    private String customerTypeCode;

    @JsonProperty("expDate")
    private String expDate;

    @JsonProperty("maritalStatusCode")
    private String maritalStatusCode;

    @JsonProperty("nationalityCode")
    private String nationalityCode;

    @JsonProperty("engLastName")
    private String engLastName;

    @JsonProperty("engFirstName")
    private String engFirstName;

    @JsonProperty("thaiLastName")
    private String thaiLastName;

    @JsonProperty("thaiSuffix")
    private String thaiSuffix;

    @JsonProperty("issueDate")
    private String issueDate;

    @JsonProperty("ocCode")
    private String ocCode;

    @JsonProperty("firstAcctOpenBranch")
    private String firstAcctOpenBranch;

    @JsonProperty("docType")
    private String docType;

    @JsonProperty("engMiddleName")
    private String engMiddleName;

    @JsonProperty("engSuffix")
    private String engSuffix;

    @JsonProperty("thaiTitle")
    private String thaiTitle;

    @JsonProperty("docNo")
    private String docNo;

    @JsonProperty("thaiFirstName")
    private String thaiFirstName;

    @JsonProperty("genderCode")
    private String genderCode;

    @JsonProperty("occupationCode")
    private String occupationCode;

    @JsonProperty("dob")
    private String dob;

    @JsonProperty("religionCode")
    private String religionCode;

    @JsonProperty("sourceOfIncome")
    private String sourceOfIncome;

    @JsonProperty("businessUnitOwnerCode")
    private String businessUnitOwnerCode;

    @JsonProperty("thaiMiddleName")
    private String thaiMiddleName;

    private String workPositionCode;
    private String thaiFullTitle;
    private String engFullTitle;

    private Long actualIncome;

    private String countryOfIssue;

    @JsonProperty("taxId")
    private String taxId;
}
