package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebitCardFeeInfo {
    private String feeCode;
    private String feeDescription;
    private int feeAmount;
    private String docReqFlag;
    private String usageType;
    private String taxidReqFlag;
    private List<String> userType;
}
