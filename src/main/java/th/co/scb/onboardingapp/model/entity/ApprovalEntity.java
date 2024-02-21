package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import th.co.scb.onboardingapp.model.ApprovalStatus;
import th.co.scb.onboardingapp.model.ApprovalType;

import java.util.Date;

@Data
@Entity
@Table(name = "APPROVAL")
public class ApprovalEntity {

    @Id
    @Column(name = "APPL_ID")
    private String approvalId;

    @Column(name = "FUNC_CODE", nullable = false)
    private String functionCode;

    @Column(name = "CSE_ID", nullable = false)
    private String caseId;

    @Column(name = "SELLER_ID", nullable = false)
    private String sellerId;

    @Column(name = "BRANCH_ID", nullable = false)
    private String branchId;

    @Column(name = "APPL_BRANCH_ID", nullable = false, length = 4)
    private String approvalBranchId;

    @Column(name = "APPL_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.P;

    @Column(name = "APPL_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalType type;

    @Column(name = "APPL_REQUIRED", nullable = false)
    private int approvalRequired;

    @Column(name = "SSC_REQUIRED", nullable = false)
    private int sscRequired;

    @Column(name = "CREATED_DATE", nullable = false)
    private Date createdDate;

    @Column(name = "UPDATED_DATE", nullable = false)
    private Date updatedDate;

    @Column(name = "APPL_COUNT", nullable = false)
    private int approvalCount;

    @Column(name = "CUST_FULLNAME_EN")
    private String customerFullnameEN;

    @Column(name = "CUST_FULLNAME_TH")
    private String customerFullnameTH;

    @Column(name = "REQ_REASON")
    private String requestReason;

    @Column(name = "REFERENCE_NO")
    private String referenceNo;

    @Column(name = "REFERENCE_TYPE")
    private String referenceType;

    @Column(name = "SELLER_FULLNAME_EN")
    private String sellerFullnameEN;

    @Column(name = "SELLER_FULLNAME_TH")
    private String sellerFullnameTH;

    @Column(name = "LOCK_BY")
    private String lockBy;

    @Column(name = "LOCK_DATE")
    private Date lockDate;

    @Column(name = "APPL1")
    private String approval1;

    @Column(name = "APPL1_STATUS")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approval1Status;

    @Column(name = "APPL1_DATE")
    private Date approval1Date;

    @Column(name = "APPL1_REJECT_REASON")
    private String approval1RejectReason;

    @Column(name = "APPL2")
    private String approval2;

    @Column(name = "APPL2_STATUS")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approval2Status;

    @Column(name = "APPL2_DATE")
    private Date approval2Date;

    @Column(name = "APPL2_REJECT_STATUS")
    private String approval2RejectReason;

    @Column(name = "DATA", columnDefinition = "JSON")
    private String data;

    @Version
    @Column(name = "VERSION")
    private Long version = 1L;

}
