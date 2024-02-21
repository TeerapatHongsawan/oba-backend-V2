package th.co.scb.onboardingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends StatusCodeException {
    private static final long serialVersionUID = 1L;

    public NotFoundException(String type) {
        this(type, null);
    }

    public NotFoundException(String type, Throwable throwable) {
        super(ErrorCodes.NOT_FOUND.name(), type + ErrorCodes.NOT_FOUND.getMessage(), throwable);
    }

}