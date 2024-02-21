package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "PRODUCT")
public class DebitCardProductEntity {

    @Id
    @Column(name = "PRODUCT_ID", nullable = false, columnDefinition = "CHAR", length = 2)
    private String productId;

    @Column(name = "PRODUCT_TYPE", length = 32)
    private String productType;

    @Column(name = "PRODUCT_CARD_TYPE", length = 64)
    private String productCardType;

    @Column(name = "PRODUCT_NAME_TH", length = 64)
    private String productNameTH;

    @Column(name = "PRODUCT_NAME_EN", length = 64)
    private String productNameEN;

    @Column(name = "PRODUCT_DESC", length = 1028)
    private String productDescription;

    @Column(name = "PRODUCT_DESC_EN", length = 1028)
    private String productDescriptionEN;

    @Column(name = "PRODUCT_DESC_DETAIL", length = 1028)
    private String productDescriptionDetail;

    @Column(name = "PRODUCT_DESC_DETAIL_EN", length = 1028)
    private String productDescriptionDetailEN;

    @Column(name = "ACTIVE_DATE", length = 10)
    private String activeDate;

    @Column(name = "TC_TYPE", columnDefinition = "CHAR", length = 1)
    private String tcType;

    @Column(name = "STOCK_CODE", columnDefinition = "CHAR", length = 4)
    private String stockCode;

    @Column(name = "STOCK_RB_DESC_TYPE1", length = 64)
    private String stockRbDescriptionType1;

    @Column(name = "STOCK_RB_DESC_TYPE2", length = 64)
    private String stockRbDescriptionType2;

    @Column(name = "PRODUCT_IMG", columnDefinition = "MEDIUMTEXT")
    private String productImage;

    @Column(name = "TIMESTAMP_CREATED", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdTimestamp;

    @Column(name = "CHUB_PROD_CODE", length = 45)
    private String chubProductCode;

    @Column(name = "CHUB_RUNNING_NO", length = 6)
    private String chubRunningNumber;

    @Column(name = "CHUB_PROD_FEE", length = 10)
    private String chubProdFee;

    @Column(name = "ACCT_CODE", length = 4)
    private String accountCode;

    @Column(name = "PRODUCT_FEE_INFO", length = 1000)
    private String productFeeInfo;

    @Column(name = "PRODUCT_FEE_INFO_EN", length = 1000)
    private String productFeeInfoEN;

    @Column(name = "SEND_CHUBB",columnDefinition = "tinyint(1)")
    private boolean sendChubb;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productId")
    @Where(clause = "ACTIVE = 'Y'")
    private List<DebitCardFeeEntity> feeCodes;

    @Column(name = "DEFAULT_ORDER", length = 11)
    private String defaultOrder;

    @Column(name = "SALE_SHEET_LINK")
    private String salesheetLink;

    @Column(name = "SALE_SHEET_LINK_EN")
    private String salesheetLinkEN;
}
