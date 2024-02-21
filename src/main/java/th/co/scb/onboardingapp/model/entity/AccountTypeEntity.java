package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.util.List;


@Data
@Entity
@Table(name = "ACCT_TYPE")
public class AccountTypeEntity {

    @Id
    @Column(name = "ACCT_TYPE_CODE", nullable = false, length = 10)
    private String code;

    @Column(name = "ACCT_TYPE_NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "ACCT_TYPE_NAME_EN", nullable = false, length = 100)
    private String nameEn;

    @Column(name = "SHORT_DESC")
    private String shortDescription;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DESCRIPTION_FCD")
    private String descriptionFCD;

    @Column(name = "IMAGE", columnDefinition = "MEDIUMTEXT")
    private String image;

    @Column(name = "SALE_SHEET_LINK")
    private String salesheetLink;

    @Column(name = "SALE_SHEET_LINK_EN")
    private String salesheetLinkEn;

    @Column(name = "DEFAULT_ORDER")
    private String defaultOrder;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "accountTypeCode")
    @Where(clause = "ACTIVE = 'Y'")
    @OrderBy("DEFAULT_ORDER")
    private List<AccountEntity> accounts;
}
