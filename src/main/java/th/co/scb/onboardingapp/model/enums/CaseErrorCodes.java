package th.co.scb.onboardingapp.model.enums;

public enum CaseErrorCodes {
    CASE_NOT_START("Cannot find case id"),
    CASE_INVALID_BRANCH("Invalid branchId"),
    CASE_INVALID_STATUS("Invalid case status"),
    CASE_INVALID_APPRL_STATUS("Invalid approval status"),
    CASE_NEW_CUSTOMER("Not allow new customer to access"),
    CASE_IDCARD_EXPIRE("ID card is expired"),
    CASE_IDCARD_EXPIRE_IPROFILE("ID card is expired"),
    CASE_IDCARD_ISSUEDDATE_BEFORE_VCHANNEL("ID card issued date is before vhannel's issued date"),
    CASE_APPROVAL_REQUIRED("Required approval"),
    EC_ACCOUNT_BALANCE("Account balance more than zero"),
    CASE_DATA_INVALID("Data is invalid"),
    INFORMATION_INVALID("Invalid information"),
    CHAIYO_CONVERT_CARD_NO_FAILED("Card red convert to card No. for chaiyo failed");

    private String message;

    CaseErrorCodes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
