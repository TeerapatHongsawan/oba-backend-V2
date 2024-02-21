package th.co.scb.onboardingapp.model;

public enum ApprovalStatus {
    P("Pending"),
    C("Cancelled"),
    R("Rejected"),
    A("Approved");

    private final String value;

    ApprovalStatus(String value) {
        this.value = value;
    }
}
