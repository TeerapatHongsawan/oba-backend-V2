package th.co.scb.onboardingapp.model.common;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class DocTypeCode {
    public static final String SIGNATURE = "001";
    public static final String SIGNATURE_CONSENT = "001-1";
    public static final String SIGNATURE_CI_EDITED = "001-2";
    public static final String SIGNATURE_FOREIGNER = "001-3";
    public static final String ID_CARD = "002";
    public static final String ID_CARD_MANUAL_KEYIN = "002-1";
    public static final String PASSPORT = "201";
    public static final String ALIEN_ID_CARD = "202";

    public static final String APPFORM = "003";
    public static final String APPFORM_SET002 = "S002";
    public static final String APPFORM_SET003 = "S003";
    public static final String APPFORM_SET005 = "S005";
    public static final String APPFORM_SET006 = "S006";
    public static final String APPFORM_SET008 = "S008";
    public static final String APPFORM_SET009 = "S009";
    public static final String APPFORM_SET010 = "S010";
    public static final String APPFORM_SET012 = "S012";
    public static final String APPFORM_SET013 = "S013";
    public static final String APPFORM_SET014 = "S014";
    public static final String APPFORM_SET016 = "S016";
    public static final String APPFORM_SET301 = "S301";
    public static final String APPFORM_SET302 = "S302";
    public static final String APPFORM_SET303 = "S303";
    public static final String APPFORM_SET303_1 = "S303-1";
    public static final String APPFORM_SET304 = "S304";
    public static final String APPFORM_SET305 = "S305";
    public static final String APPFORM_SET306 = "S306";
    public static final String APPFORM_SETIP004 = "IP004";
    public static final String APPFORM_SETIP005 = "IP005";
    public static final String APPFORM_SETIP006 = "IP006";
    public static final String APPFORM_SETIP007 = "IP007";
    public static final String APPFORM_MUTUALFUND_RISK_AND_BAA = "P012";

    public static final String APPFORM_PREVIEW = "003-2";
    public static final String APPFORM_CI = "100";
    public static final String APPFORM_CI_EDITED = "101";
    public static final String APPFORM_CI_FATCA_QUES = "102";
    public static final String APPFORM_PRODUCT = "200";
    public static final String APPFORM_TC_ALL = "300";
    public static final String APPFORM_TC_JAKJAI = "301";
    public static final String APPFORM_TC_JAKJAI_2 = "301-2";
    public static final String APPFORM_TC_JAD_TEM = "302";
    public static final String APPFORM_TC_E_PASSBOOK = "303";
    public static final String APPFORM_TC_DEBIT = "304";
    public static final String APPFORM_TC_SIGN = "305";
    public static final String APPFORM_TC_FEEL_FREE = "306";
    public static final String APPFORM_TC_EZ = "307";
    public static final String APPFORM_TC_MUTUAL_FUND = "308";
    public static final String APPFORM_TC_OMNIBUS = "309";
    public static final String CUSTOMER_PHOTO = "004";
    public static final String CUST_PHOTO_ON_SMART_CARD = "004-1";
    public static final String CERTIFICATE = "005";
    public static final String SPECIAL_FEE_CODE = "006";
    public static final String COVERPAGE_EPASSBOOK = "007";
    public static final String SIGN_CARD_EPASSBOOK = "008";
    public static final String SIGN_CARD_PASSBOOK = "009";
    public static final String SIGN_CARD_MUTUALFUND = "010";
    public static final String CHAIYO_CONSENT = "P306";

    public static final String FATCA_W8 = "401";
    public static final String FATCA_W9 = "402";
    public static final String JAKJAI_CONSENT = "403";
    public static final String MARKETING_CONSENT = "404";
    public static final String KYC = "012";
    public static final String CHART_IMAGE = "015";

    public static final String APPFORM_DEPOSIT_CUSTOMER_INFO = "S001";
    public static final String APPFORM_THAI_ID_CARD = "P024";
    public static final String APPFORM_DEPOSIT_PRODUCT = "P041";
    public static final String APPFORM_EASYAPP_PRODUCT = "P044";
    public static final String APPFORM_MUTUAL_FUND_EMAIL = "E001";
    public static final String SIGNATURE_CARD_EPASSBOOK = "P025";
    public static final String SIGNATURE_CARD_PASSBOOK = "P026";
    public static final String SIGNATURE_CARD_MUTUALFUND = "P027";
    public static final String APPFORM_OMNIBUS_CUSTOMER_INFO = "P301";
    public static final String APPFORM_OMNIBUS_PRODUCT = "P302";
    public static final String APPFORM_OMNIBUS_CONTRACT = "P303";
    public static final String APPFORM_OMNIBUS_RISK_AND_BAA = "P304";
    public static final String APPFORM_DEBITCARD_PRODUCTO = "P305";

    public static final Map<String, String> DOC_TYPE_CODE_MAPPER = ImmutableMap.<String, String>builder()
            .put(DocTypeCode.APPFORM_SET002, "003")
            .put(DocTypeCode.APPFORM_SET003, "003")
            .put(DocTypeCode.APPFORM_SET005, "013")
            .put(DocTypeCode.APPFORM_SET006, "013") // For Securities
            .put(DocTypeCode.APPFORM_SET008, "003")
            .put(DocTypeCode.APPFORM_SET009, "013") // For Securities
            .put(DocTypeCode.APPFORM_SET010, "003")
            .put(DocTypeCode.APPFORM_SET012, "016") // For Service Only
            .put(DocTypeCode.APPFORM_SET013, "003")
            .put(DocTypeCode.APPFORM_SET014, "003")
            .put(DocTypeCode.APPFORM_SET303_1, "017") //For Omnibus
            .put(DocTypeCode.APPFORM_SET303, "017")
            .put(DocTypeCode.APPFORM_SET304, "016") // Service Only (Debit Card + Easy)
            .put(DocTypeCode.APPFORM_SET306, "017") // Omnibus + Service
            .put(DocTypeCode.APPFORM_SETIP004, "003")
            .put(DocTypeCode.APPFORM_SETIP005, "003")
            .put(DocTypeCode.APPFORM_SETIP006, "003")
            .put(DocTypeCode.APPFORM_SETIP007, "003")
            .put(DocTypeCode.SIGNATURE_CARD_EPASSBOOK, "008")
            .put(DocTypeCode.SIGNATURE_CARD_PASSBOOK, "009")
            .put(DocTypeCode.SIGNATURE_CARD_MUTUALFUND, "010")
            .put(DocTypeCode.APPFORM_MUTUALFUND_RISK_AND_BAA, "014")
            .put(DocTypeCode.APPFORM_OMNIBUS_RISK_AND_BAA, "014")
            .put(DocTypeCode.APPFORM_SET016, "003")
            .put(DocTypeCode.CHAIYO_CONSENT, "306")
            .build();

    private DocTypeCode() {
    }
}