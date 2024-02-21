package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.util.List;


@Data
@Entity
@Table(name = "ACCT_MAPPING")
public class AccountEntity {

    @Id
    @Column(name = "ACCT_CODE", nullable = false, length = 4)
    private String accountCode;

    @Column(name = "ACCT_NAME", length = 100)
    private String accountName;

    @Column(name = "ACCT_NAME_EN", length = 100)
    private String accountNameEn;

    @Column(name = "ACCT_TYPE_CODE", nullable = false, length = 10)
    private String accountTypeCode;

    @Column(name = "ACCT_DETAILS", length = 250)
    private String accountDetails;

    @Column(name = "ACCT_DETAILS_EN", length = 250)
    private String accountDetailsEn;

    @Column(name = "ADDITIONAL_DETAILS", columnDefinition = "JSON")
    private String additionalDetails;

    @Column(name = "ADDITIONAL_DETAILS_FCD", columnDefinition = "JSON")
    private String additionalDetailsFCD;

    @Column(name = "DEFAULT_ORDER", length = 11)
    private String defaultOrder;

    @Column(name = "SINGLE_SELECT", length = 1)
    private String singleSelect;

    @Column(name = "ACTIVE", length = 1)
    private String active;

    @Column(name = "SALE_SHEET_LINK", columnDefinition = "TEXT")
    private String saleSheetLink;

    @Column(name = "SALE_SHEET_LINK_FCD", columnDefinition = "TEXT")
    private String saleSheetLinkFCD;

    @Column(name = "SALE_SHEET_LINK_EN", columnDefinition = "TEXT")
    private String saleSheetLinkEn;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "accountCode")
    @Where(clause = "ACTIVE = 'Y'")
    @OrderBy("DEFAULT_ORDER")
    private List<AccountProductEntity> products;

}
