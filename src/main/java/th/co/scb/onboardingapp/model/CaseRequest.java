package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class CaseRequest {
    private CardInfo cardInfo;
    private IdentificationsItem identificationsItem;
    private ChaiyoLoanDetail chaiyoLoanDetail;
}
