package th.co.scb.onboardingapp.model.enums;

public enum CaseStatus {
    CLOSED("CLSD"),
    ONBD("ONBD"),
    OPEN("OPEN"),
    REJECTED("REJD"),
    REQSIGN("REQSIGN");

    private String value;

    CaseStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
