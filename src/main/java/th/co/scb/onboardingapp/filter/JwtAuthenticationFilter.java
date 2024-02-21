package th.co.scb.onboardingapp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.service.ObaTokenService;
import th.co.scb.onboardingapp.exception.ErrorCodes;
import th.co.scb.onboardingapp.exception.ErrorResponse;

import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {

    @Autowired
    private ObaTokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;
    
    public ObjectMapper getObjectMapper() {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper();
        }
        return this.objectMapper;
    }

    public JwtAuthenticationFilter(ObaTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = tokenService.getToken((HttpServletRequest) request);
        // no token
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        ObaAuthentication authentication = null;
        try {
            // good token
            authentication = tokenService.parse(token);
        } catch (Exception e) {
            // bad token
            // no-op
        }

        // handle bad token
        // stop proceeding - return http error 401
        if (authentication == null) {
            ErrorResponse error = ErrorResponse.builder().code(ErrorCodes.INVALID_TOKEN.name())
                    .message(ErrorCodes.INVALID_TOKEN.getMessage()).build();
            ObjectMapper objectMapper = this.getObjectMapper();
            String data = objectMapper.writeValueAsString(error);
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.getWriter().write(data);
            return;
        }

        try {
            MDC.put("userId", authentication.getName());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } finally {
            MDC.remove("userId");
            MDC.remove("caseId");
            MDC.remove("branchId");
        }
    }
}
