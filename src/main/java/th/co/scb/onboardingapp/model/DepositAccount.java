package th.co.scb.onboardingapp.model;

import lombok.Data;
import th.co.scb.entapi.custinfo.model.CustomerAccountInfo;
import th.co.scb.entapi.deposits.model.RecordDetails;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DepositAccount extends CustomerAccountInfo {
    private String productCode;
    private BigDecimal balance;
    private String applicationId;
    private List<RecordDetails> debitCards;
}
