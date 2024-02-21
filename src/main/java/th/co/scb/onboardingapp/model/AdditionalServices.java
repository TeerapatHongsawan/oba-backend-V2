package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.List;

@Data
public class AdditionalServices {
    private List<DebitCard> cards;
    private SmsAlert sms;
    private List<PromptPay> promptpay;
    private NSSEmail nssEmail;
    private Affiliate affiliate;
    private StandingOrder standingOrder;
    private RegisterFundTransfer registerFundTransfer;
    private StatementFrequency statementFrequency;
}