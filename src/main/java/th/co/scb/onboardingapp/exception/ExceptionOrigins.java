package th.co.scb.onboardingapp.exception;

public enum ExceptionOrigins {

	INDI("INDI","individual"),
	LDAP("LDAP","ldap");

	public final String code;
	public final String description;
	
	private ExceptionOrigins(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}
