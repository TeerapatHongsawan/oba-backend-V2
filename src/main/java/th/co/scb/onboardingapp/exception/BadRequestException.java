package th.co.scb.onboardingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends StatusCodeException {
	private static final long serialVersionUID = 1L;

	public BadRequestException(String code, String message) {
        this(code, message, null);
    }

    public BadRequestException(String code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

}
