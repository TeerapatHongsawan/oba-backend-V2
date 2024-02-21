package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TJLogApproval {

    private String functionCode;
    private int approvalRequired;
    private String approvalStatus;
    private int approvalCount;

}
