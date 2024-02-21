package th.co.scb.onboardingapp.model;

public class LoggingConstant {
    public static final  String ENVIRONMENT = "environment";
    public static final  String HEADER_REQUEST_UID = "RequestUID";
    public static final  String HEADER_HOST = "host";
    public static final  String EVENT_NAME = "eventName";
    public static final  String USER_ID = "userId";
    public static final  String CASE_ID = "caseId";
    public static final  String BRANCH_ID = "branchId";
    public static final  String RUN_TYPE = "runType";
    public static final  String FE_TIMESTAMP = "feTime";
    
    public static final  String MDC_REQ_UID = "ob_client_reqUID"; // use for all type = web, cron, batch

    private LoggingConstant() {
    }
}
