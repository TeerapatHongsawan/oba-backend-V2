package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CSE")
public class CaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "CSE_ID", length = 32, nullable = false)
    private String caseId;

    @Column(name = "CSE_TP_CD", length = 16, nullable = false)
    private String caseTypeID;

    @Column(name = "OC_ID", length = 6)
    private String ocCode;

    @Column(name = "CRT_DT", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "SV_DT", nullable = false)
    private LocalDateTime savedDate;

    @Column(name = "FF_DT")
    private LocalDateTime fulfilmentDate;

    @Column(name = "COMPL_DT")
    private LocalDateTime completedDate;

    @Column(name = "CSE_ST_CD", length = 16, nullable = false)
    private String caseStatus;

    @Column(name = "RSN_CD", length = 45)
    private String reasonCode;

    @Column(name = "EMP_ID", length = 16)
    private String employeeId;

    @Column(name = "REFR_ID", length = 40)
    private String referenceId;

    @Column(name = "BRANCH_ID", length = 4)
    private String branchId;

    @Column(name = "BOOKING_BRANCH_ID", length = 4)
    private String bookingBranchId;

    @Column(name = "APPFORM_NO", length = 22)
    private String appFormNo;

    @Column(name = "JS_DMS_INFO", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String documentInfo;

    @Column(name = "JS_CUST_INFO", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String customerInfo;

    @Column(name = "JS_PROD_INFO", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String productInfo;

    @Column(name = "JS_PAY_INFO", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String paymentInfo;

    @Column(name = "JS_APPR_INFO", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String approvalInfo;

    @Column(name = "JS_PORTFOLIO_INFO", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String portfolioInfo;

    @Column(name = "JS_ADDITIONAL_INFO", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String additionalInfo;
}
