package th.co.scb.onboardingapp.model;

public enum Relation {
    SELF("Self"),
    SPOUSE("Spouse");

    private String value;

    Relation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
