package th.co.scb.onboardingapp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitCardFeeKey implements Serializable {

    private String productId;
    private String feeCode;
    private String taxidReqFlag;
}
