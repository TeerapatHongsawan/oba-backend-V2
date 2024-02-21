package th.co.scb.onboardingapp.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConstants {
    public static final String TRANSIT_PATH = "AP2131-ReportEngine";
    public static final String CONTACT_RPT = "Please contact the RPT team for support.";

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSSSSZ";
    public static final String ACCEPTED = "accepted";
    public static final String STATUS_CODE_1000 = "1000";
    public static final String STATUS_CODE_1001 = "1001";
    public static final String STATUS_CODE_01 = "01";
    public static final String STATUS_CODE_00 = "00";
    public static final String EVENT_START = "Start";
    public static final String EVENT_START_VERIFICATION = "Start Verification";
    public static final String EVENT_VERIFICATION = "Verification";
    public static final String EVENT_RESPONSE = "Response";
    public static final String EVENT_REQUEST = "Request";
    public static final String STATUS_CODE_1899 = "1899";
    public static final String SYSTEM_API_KEY = "system/apiKey";
    public static final String SYSTEM_API_KEY_IS_NOT_CORRECT = "422: %s is not correct";
    public static final String MANDATORY_CAN_NOT_NULL = "422: %s Mandatory can not null";
    public static final String DATE_NOT_MATCH = "422: %s not match with date format";
    public static final String HEADER_INFO = "correlationId: %s, system_name: %s";
    public static final String CALCULATE_PARAM_IS_NOT_CORRECT = "422: Calculate param is not correct";
    public static final String RESPONSE_DETAIL = "{calculate_param_result = {%s}}";
    public static final String TODAY = "T";
    public static final String WORKING_DAY = "WKD";
    public static final String END_OF_WEEK = "EOW";
    public static final String END_OF_WEEK_WORKINGDAY = "EOW_WKD";
    public static final String END_OF_MONTH = "EOM";
    public static final String END_OF_MONTH_WORKINGDAY = "EOM_WKD";
    public static final String END_OF_QUARTER = "EOQ";
    public static final String END_OF_QUARTER_WORKINGDAY = "EOQ_WKD";
    public static final String END_OF_YEAR = "EOY";
    public static final String END_OF_YEAR_WORKINGDAY = "EOY_WKD";
    public static final String START_OF_WEEK = "SOW";
    public static final String START_OF_WEEK_WORKINGDAY = "SOW_WKD";
    public static final String START_OF_MONTH = "SOM";
    public static final String START_OF_MONTH_WORKINGDAY = "SOM_WKD";
    public static final String START_OF_QUARTER = "SOQ";
    public static final String START_OF_QUARTER_WORKINGDAY = "SOQ_WKD";
    public static final String START_OF_YEAR = "SOY";
    public static final String START_OF_YEAR_WORKINGDAY = "SOY_WKD";

    public static final String LAST_SATURDAY = "L_SAT";

    public static final String LAST_SUNDAY = "L_SUN";
    public static final String TOTAL_DAY_OF_PERIOD = "TOTAL_PERI";
    public static final String TOTAL_DAY_OF_PERIOD_WORKINGDAY = "TOTAL_PERI_WKD";
    public static final String TOTAL_DAY_OF_WEEK = "TOTAL_DOW";
    public static final String TOTAL_DAY_OF_WEEK_WORKINGDAY = "TOTAL_DOW_WKD";
    public static final String TOTAL_DAY_OF_MONTH = "TOTAL_DOM";
    public static final String TOTAL_DAY_OF_MONTH_WORKINGDAY = "TOTAL_DOM_WKD";
    public static final String TOTAL_DAY_OF_WEEK_ON_MONTH = "TOTAL_DOW_ON_M";
    public static final String TOTAL_DAY_OF_WEEK_ON_MONTH_WORKINGDAY = "TOTAL_DOW_ON_M_WKD";
    public static final String DATE_ON_PERIOD = "PERI";
    public static final String DATE_ON_PERIOD_WORKINGDAY = "PERI_WKD";
    public static final String DATE_ON_MONTH = "DOM";
    public static final String DATE_ON_MONTH_WORKINGDAY = "DOM_WKD";
    public static final String DATE_ON_WEEK = "DOW";
    public static final String DATE_ON_WEEK_WORKINGDAY = "DOW_WKD";
    public static final String CRON_EXPRESSION = "app.cron.expression";

}
