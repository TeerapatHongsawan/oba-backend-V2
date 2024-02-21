package th.co.scb.onboardingapp.model.enums;

public enum ApprovalErrorCodes {
    CASE_NOT_START("Cannot find case id"),
    APPL_INVALID_STATUS("Invalid approval status"),
    APPL_INVALID_TYPE("Invalid approval type"),
    APPL_NO_PERMISSION("Not authorized for user"),
    ALREADY_APPROVED("This user already approved this approval"),
    APPL_IS_LOCKED("The approval is locked by other user"),
    APPL_EXPIRED("Approval is expired");

    private final String message;

    ApprovalErrorCodes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
