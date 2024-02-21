package th.co.scb.onboardingapp.exception;

import org.springframework.core.NestedRuntimeException;
import th.co.scb.onboardingapp.model.OriginalExceptionDetail;

public abstract class StatusCodeException extends NestedRuntimeException {
    private static final long serialVersionUID = 1L;

    private final String code;
    private final String message;

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    private OriginalExceptionDetail originalExceptionDetail;

    public OriginalExceptionDetail getOriginalExceptionDetail() {
        return originalExceptionDetail;
    }

    public void setOriginalExceptionDetail(OriginalExceptionDetail originalExceptionDetail) {
        this.originalExceptionDetail = originalExceptionDetail;
    }

    public StatusCodeException(String code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
        this.message = message;
    }

    public StatusCodeException(String code, String message) {
        this(code, message, null);
    }

}
