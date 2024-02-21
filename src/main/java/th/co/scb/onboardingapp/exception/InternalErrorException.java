package th.co.scb.onboardingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalErrorException extends StatusCodeException {
	private static final long serialVersionUID = 1L;

	public InternalErrorException(String code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

    public InternalErrorException(String code, String message) {
        this(code, message, null);
    }
    
    
}
