package th.co.scb.onboardingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends StatusCodeException {
	private static final long serialVersionUID = 1L;

	public UnauthorizedException(String code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

    public UnauthorizedException(String code, String message) {
        this(code, message, null);
    }
    
}
