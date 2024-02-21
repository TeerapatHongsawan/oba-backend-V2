package th.co.scb.onboardingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends StatusCodeException {
	private static final long serialVersionUID = 1L;

	public ConflictException(String code, String message) {
        this(code, message, null);
    }

    public ConflictException(String code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

}
