package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseInfo {
    private String caseId;
    private String branchId;
    private String bookingBranchId;
    private String ocCode;
    private String employeeId;

    /**
     * RM id
     */
    private String referenceId;
    private LocalDateTime createdDate;
    private LocalDateTime savedDate;

    /**
     * Timestamp when customer start filling info
     */
    private LocalDateTime fulfilmentDate;
    private LocalDateTime completedDate;

    /**
     * Not use
     */
    private String caseTypeID;

    /**
     * OPEN - Case started
     * ONBD - Case completed
     * CLSD - Case rejected
     */
    private String caseStatus;

    /**
     * Not use
     */
    private String reasonCode;

    /**
     * Reference for account opening
     */
    private String appFormNo;

    /**
     * All info about customers + current saving accounts + investment products + services + payment
     */
    @Valid
    private CustomerInfo customerInfo;

    /**
     * All saving products
     */
    private List<ProductInfo> productInfo;

    /**
     * All about documents
     */
    private List<DmsInfo> documentInfo;

    /**
     * Payments
     */
    private List<PaymentInfo> paymentInfo;

    /**
     * Approval history
     */
    private List<ApprovalInfo> approvalInfo;

    /**
     * Current products (IPRO only)
     */
    private PortfolioInfo portfolioInfo;

    /**
     * Current branch & employee info
     */
    private AdditionalCaseInfo additionalInfo;

}

