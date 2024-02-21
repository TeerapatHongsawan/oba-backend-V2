package th.co.scb.onboardingapp.model.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "PRODUCT_FEE")
@IdClass(DebitCardFeeKey.class)
public class DebitCardFeeEntity {

    @Id
    @Column(name = "PRODUCT_ID", nullable = false, columnDefinition = "CHAR", length = 2)
    private String productId;

    @Id
    @Column(name = "FEE_CODE", nullable = false, columnDefinition = "CHAR", length = 2)
    private String feeCode;

    @Column(name = "FEE_AMT", columnDefinition = "BIGINT")
    private Long feeAmount;

    @Column(name = "SEQ_RB_FRONT", columnDefinition = "CHAR", length = 2)
    private String seqRbFront;

    @Column(name = "FEE_DESCRIPTION", length = 64)
    private String feeDescription;

    @Column(name = "DOC_REQ_FLAG", columnDefinition = "CHAR", length = 1)
    private String docReqFlag;

    @Column(name = "ACTIVE", columnDefinition = "CHAR", length = 1)
    private String active;

    @Column(name = "TIMESTAMP_CREATED", columnDefinition = "TimeStamp")
    private LocalDateTime createdTimestamp;

    @Column(name = "USAGE_TYPE", length = 45)
    private String usageType;

    @Column(name = "USER_TYPE", columnDefinition = "JSON")
    private String userType;

    @Column(name = "TAXID_REQ_FLAG", columnDefinition = "CHAR", length = 1)
    private String taxidReqFlag;
}
