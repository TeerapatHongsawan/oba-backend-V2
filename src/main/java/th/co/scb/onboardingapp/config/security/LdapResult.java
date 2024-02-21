package th.co.scb.onboardingapp.config.security;

import java.util.Arrays;

public enum LdapResult {

    USER_NOT_FOUND("525","USER_NOT_FOUND"),
    INVALID_CREDENTIAL("52e","INVALID_CREDENTIAL"),
    NOT_PERMIT_LOGON("530","NOT_PERMIT_LOGON"),
    NOT_PERMIT_LOGON_WORKSTATION("531","NOT_PERMIT_LOGON_WORKSTATION"),
    PASSWORD_EXPIRED("532","PASSWORD_EXPIRED"),
    ACCOUNT_DISABLED("533","ACCOUNT_DISABLED"),
    USER_NOT_BE_GRANTED("534","USER_NOT_BE_GRANTED"),
    ACCOUNT_EXPIRED("701","ACCOUNT_EXPIRED"),
    USER_MUST_RESET("773","USER_MUST_RESET"),
    USER_LOCKED("775","USER_LOCKED");
	
	public final String code;
	public final String description;
	
	private LdapResult(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public static LdapResult findByCode(final String code){
	    return Arrays.stream(values()).filter(value -> value.code.equals(code)).findFirst().orElse(null);
	}
}
