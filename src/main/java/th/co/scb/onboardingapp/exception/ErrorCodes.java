package th.co.scb.onboardingapp.exception;

public enum ErrorCodes {
    INVALID_TOKEN("Token is invalid"),
    NOT_FOUND(" not found"),
    UPDATE_CONFLICT("Other user already update the record"),
    UNKNOWN("Unknown exception"),
    BLOCKING_MULTI_LOGIN("Blocking multi login"),

    B00012("This anonymous user can not access."),
    B00013("The username or password you entered is incorrect. Please try again."),
    B00014("The account disabled"),
    B00015("The user account locked"),
    B00016("This user has no permission to open the account"),
    INCORRECT_VERSION("Version is incorrect"),

    I00012("Interface to LDAP Service Error (Timeout/IO/Socket)."),
    S00001("System Error."),
    NOT_ALLOWED_DEVICES("The system does not support iOnboard and iProfile on desktop web browser.");


    private String message;

    ErrorCodes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
