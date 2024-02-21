package th.co.scb.onboardingapp.exception;

/**
 * Additional attributes included into Spring MVC default 
 * error response body introduced by {@link StatusCodeException}
 * 
 * @author s89452
 *
 */
public class ExceptionBodyAttributes {

	/**
	 * code denote the condition that causes the Exception.
	 */
	public static final String CODE = "code";
	/**
	 * textual description of the condition that causes the Exception.
	 */
	public static final String DESCRIPTION = "description";
	
	/**
	 * the system that cause the Exception
	 */
	public static final String ORIGIN = "origin";
	/**
	 * the operation that cause the Exception
	 */
	public static final String ORIGIN_OP = "origin_op";
	/**
	 * the code of the system that originate the Exception
	 */
	public static final String ORIGIN_CODE = "origin_code";
	/**
	 * textual description of condition of ORIGIN_CODE
	 */
	public static final String ORIGIN_DESCRIPTION = "origin_description";
	
	/**
	 * trace level detail, use for non production only
	 */
	public static final String TRACE = "trace";
	
	/**
	 * uid that point to the request it is originated from
	 */
	public static final String REQUEST_UID = "request_uid";

	private ExceptionBodyAttributes() {
	}
}
