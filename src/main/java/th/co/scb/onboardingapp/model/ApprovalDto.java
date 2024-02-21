package th.co.scb.onboardingapp.model;

import lombok.Data;
import java.util.Date;
import java.util.Map;

@Data
public class ApprovalDto {
    private String approvalId;
    private String functionCode;
    private String caseId;
    private String sellerId;
    private String branchId;
    private String approvalBranchId;
    private ApprovalStatus approvalStatus;
    private ApprovalType type;
    private int approvalRequired;
    private int sscRequired;
    private Date createdDate;
    private Date updatedDate;
    private int approvalCount;
    private String customerFullnameEN;
    private String customerFullnameTH;
    private String requestReason;
    private String referenceNo;
    private String referenceType;
    private String sellerFullnameEN;
    private String sellerFullnameTH;
    private String lockBy;
    private Date lockDate;
    private String approval1;
    private ApprovalStatus approval1Status;
    private Date approval1Date;
    private String approval1RejectReason;
    private String approval2;
    private ApprovalStatus approval2Status;
    private Date approval2Date;
    private String approval2RejectReason;
    private Map<String, Object> data;
    private Long version;
}
