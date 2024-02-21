package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
@Table(name = "ACCT_PROD_DETAIL")
public class AccountProductEntity {

    @Id
    @Column(name = "INDI_PROD_CODE", nullable = false, length = 8)
    private String indiProductCode;

    @Column(name = "ACCT_CODE", nullable = false, length = 4)
    private String accountCode;

    @Column(name = "PROD_CODE", nullable = false, length = 4)
    private String productCode;

    @Column(name = "SUB_ACCT_TYPE", nullable = false, length = 15)
    private String productType;

    @Column(name = "EMAIL", nullable = false, length = 1)
    private String email;

    @Column(name = "BENE_REL", nullable = false, length = 1)
    private String beneficiaryRelation;

    @Column(name = "ADDITIONAL_SERVICE", nullable = false, length = 1)
    private String additionalService;

    @Column(name = "PROD_NAME", nullable = false, length = 256)
    private String productName;

    @Column(name = "CBS_PROD_CODE", length = 4)
    private String cbsProductCode;

    @Column(name = "PROD_NAME_ENG", length = 255)
    private String productNameEN;

    @Column(name = "ADDITIONAL_JS", columnDefinition = "JSON")
    private String additionalInfo;

    @Column(name = "ACTIVE", length = 1)
    private String active;

    @Column(name = "FCD_FLAG", length = 1)
    private String fcdFlag;

    @Column(name = "PROD_MKTNAME_TH", length = 255)
    private String productMarketingName;

    @Column(name = "PROD_MKTNAME_EN", length = 255)
    private String productMarketingNameEN;

    @Column(name = "PROD_COVERPAGE_TH", length = 255)
    private String productCoverPageName;

    @Column(name = "PROD_COVERPAGE_EN", length = 255)
    private String productCoverPageNameEN;

    @Column(name = "PROD_SIGNATURE_EN", length = 255)
    private String productSignatureCardNameEN;


}
