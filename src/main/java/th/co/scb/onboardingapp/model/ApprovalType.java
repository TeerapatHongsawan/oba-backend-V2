package th.co.scb.onboardingapp.model;

public enum ApprovalType {
    L("Local"),
    R("Remote");

    private final String value;

    ApprovalType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
