package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalCaseInfo {
    private BranchProfile branchProfile;
    private EmployeeProfile empProfile;
    /**
     * Chaiyo Loan Detail
     */
    private ChaiyoLoanDetail chaiyoLoanDetail;


    private Boolean isSendedSMS;

    private Boolean isFCD;
}
