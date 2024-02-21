package th.co.scb.onboardingapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import th.co.scb.onboardingapp.exception.ExceptionBodyAttributes;
import th.co.scb.onboardingapp.exception.StatusCodeException;
import th.co.scb.onboardingapp.model.LoggingConstant;
import th.co.scb.onboardingapp.model.OriginalExceptionDetail;
import th.co.scb.onboardingapp.model.RunType;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.kv;


@Slf4j
@Configuration
public class MvcErrorAttributesWithCustomExceptionDetailsConfig {

    @Value("${oba.mvc.error.result.includeRequestUID:true}")
    private boolean includeRequestUID;

    private void includeRequestUIDToErrorAttributesIfEnabled(Map<String, Object> errorAttributes) {
        if (this.includeRequestUID) {
            this.includeRequestUIDToErrorAttributes(errorAttributes);
        }
    }

    private void includeRequestUIDToErrorAttributes(Map<String, Object> errorAttributes) {
        String requestUID = this.getRequestUID();
        if (requestUID == null) {
            requestUID = "undefined";
        }
        errorAttributes.put(ExceptionBodyAttributes.REQUEST_UID, requestUID);
    }

//    @Autowired
//    private HttpServletRequest request;

    private String getRequestUID() {
        String result = null;
        try {
//			result = this.request.getHeader(LoggingConstant.HEADER_REQUEST_UID);
        } catch (Exception e) {
            log.info("MvcErrorAttributesWithCustomExceptionDetailsConfig : {}", e.getMessage());
        }
        return result;
    }


    private static String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    @Value("${oba.mvc.error.result.includeOriginalExceptionDetail:true}")
    private boolean includeOriginalExceptionDetailToErrorAttributes;

    private void includeOriginalExceptionDetailToErrorAttributesIfEnabled(Map<String, Object> errorAttributes,
                                                                          OriginalExceptionDetail originalExceptionDetail) {
        if (this.includeOriginalExceptionDetailToErrorAttributes) {
            this.includeOriginalExceptionDetailToErrorAttributes(errorAttributes, originalExceptionDetail);
        }
    }

    @Value("${oba.mvc.error.result.printStackTrace:true}")
    private boolean printStackTrace;

    private void printStackTraceIfEnabled(Throwable throwable) {
        if (this.printStackTrace) {
            String stackTrace = getStackTrace(throwable);
            log.error("Following Exception Reach AppServer Boundary: \n{}", stackTrace);
        }
    }

    @Value("${oba.mvc.error.result.printErrorAttributes:true}")
    private boolean printErrorAttributes;

    private void printErrorAttributesIfEnabled(Map<String, Object> errorAttributes) {
        if (this.printErrorAttributes) {
			log.error("{}", kv("errorAttributes", errorAttributes), kv(LoggingConstant.RUN_TYPE, RunType.WEB_EXCP));
        }
    }

    private void includeOriginalExceptionDetailToErrorAttributes(Map<String, Object> errorAttributes,
                                                                 OriginalExceptionDetail originalExceptionDetail) {
        errorAttributes.put(ExceptionBodyAttributes.ORIGIN, originalExceptionDetail.getOriginateFrom());
        errorAttributes.put(ExceptionBodyAttributes.ORIGIN_OP, originalExceptionDetail.getOnOperation());
        errorAttributes.put(ExceptionBodyAttributes.ORIGIN_CODE, originalExceptionDetail.getResultInCode());
        errorAttributes.put(ExceptionBodyAttributes.ORIGIN_DESCRIPTION, originalExceptionDetail.getWithDescription());
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest,
                                                          ErrorAttributeOptions includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);

                Throwable throwable = getError(webRequest);
                // generic throwable
                // - print stack trace
                printStackTraceIfEnabled(throwable);

                // request_uid
                includeRequestUIDToErrorAttributesIfEnabled(errorAttributes);

                // statusCodeException
                // - extract originalExceptionDetail
                if (throwable instanceof StatusCodeException) {
                    StatusCodeException statusCodeException = (StatusCodeException) throwable;
                    String code = statusCodeException.getCode();
                    String message = statusCodeException.getMessage();
                    if (code != null) {
                        errorAttributes.put(ExceptionBodyAttributes.CODE, code);
                        errorAttributes.put("message", message);
                    }
                    OriginalExceptionDetail originalExceptionDetail = statusCodeException.getOriginalExceptionDetail();
                    if (originalExceptionDetail != null) {
                        includeOriginalExceptionDetailToErrorAttributesIfEnabled(errorAttributes, originalExceptionDetail);
                    }
                }else if (throwable instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
                    StringBuilder errorMessage = new StringBuilder();
                    for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
                        errorMessage.append(fieldError.getDefaultMessage()).append(". ");
                    }
                    errorAttributes.put("message", errorMessage.toString());
                }

                printErrorAttributesIfEnabled(errorAttributes);

                return errorAttributes;
            }

        };
    }

}
