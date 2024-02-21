package th.co.scb.onboardingapp.model;

public enum RunType {
    FRONT_END("FRONT_END"),
    BACK_END("BACK_END"),
    E_API("E_API"),
    WEB_EXCP("WEB_EXCP"), // temporarily placed here, should moved later.
    BATCH("BATCH"),
    CRON_JOB("CRON_JOB");

    private final String code;

    public String getCode() {
        return this.code;
    }

    RunType(String code) {
        this.code = code;
    }
}
