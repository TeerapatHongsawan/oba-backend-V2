package th.co.scb.onboardingapp.model;

import lombok.Data;
import th.co.scb.entapi.bpo.model.SetupRequest;

@Data
public class PaymentInfo extends SetupRequest {
    public static final String PAYMENT_FUND_ACC_TRANSFER = "04";
    public static final String PAYMENT_FUND_CASH = "01";
    public static final String PAYMENT_FUND_ATM = "06";

    private String paymentOrderID;
}
