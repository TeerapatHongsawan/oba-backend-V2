package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DebitCard {
    public static final String USAGE_TYPE_EMBOSSED = "Embossed";
    public static final String USAGE_TYPE_PREISSUE = "Pre-issued";

    private String productId;
    private String cardType;
    private String cardNoWithMask;
    private String cardReferenceNo;
    private String cardName;
    private String cardNameEn;
    private String cardDesc;
    private String stockCode;
    private BigDecimal annualFee;
    private BigDecimal issueFee;
    private String feeCode;
    private String usageType;   //'Cash Only', 'Cash / Purchase', 'Pre-issued', 'Embossed'
    private String service_fee_type;
    // private String expiryDate; //YYYYMM

    @JsonProperty("isDebitCardMRegister")
    private boolean isDebitCardMRegister;

    private String registerStatus;
    private Boolean accountDepositMessageSuccess;
    private String failedReason;
    private String companyTaxId;
    private String feePaidFlag;
    private String taxidReqFlag;
    private String license;
}
