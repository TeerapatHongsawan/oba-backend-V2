package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import th.co.scb.onboardingapp.config.security.LdapResult;
import th.co.scb.onboardingapp.exception.ErrorCodes;
import th.co.scb.onboardingapp.exception.ExceptionOrigins;
import th.co.scb.onboardingapp.model.OriginalExceptionDetail;
import th.co.scb.onboardingapp.exception.UnauthorizedException;

import javax.naming.AuthenticationException;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringTokenizer;

@Service
@Slf4j
public class LdapValidator {

    @Value("${authentication.ldap.url}")
    private String ldapUrl;

    @Value("${authentication.ldap.domain}")
    private String ldapDomain;

    @Autowired(required = false)
    private JmxFlags featureFlags;

//    @Value("${spring.profiles.active}")
//    private String profile;

    public void validate(String username, String password) {
        if (featureFlags != null && featureFlags.has("LdapMock")) {
            return;
        }
//        if (profile.contains("local") || profile.contains("default")) {
//            return;
//        }

        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            log.debug("Invalid username : [{}] password: [{}]", username, password);
            throw new UnauthorizedException(ErrorCodes.B00013.name(), ErrorCodes.B00013.getMessage());
        }

        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
            env.put("java.naming.provider.url", this.ldapUrl);
            env.put("java.naming.security.authentication", "simple");
            env.put("java.naming.security.principal", username + "@" + this.ldapDomain);
            env.put("java.naming.security.credentials", password);
            new InitialDirContext(env);
        } catch (AuthenticationException authenticationException) {
            log.error("Error from LDAP: Username = {}", username, authenticationException);
            String code = this.getErrorCode(authenticationException.getMessage());
            throw this.getException(code, authenticationException);
        } catch (NamingException namingException) {
            log.error("Error from LDAP: Username = {}", username, namingException);
        	// should throw 5xx instead, throw 4xx is misleading.
            throw new UnauthorizedException(ErrorCodes.I00012.name(), ErrorCodes.I00012.getMessage(), namingException);
        }
    }

    private String nextTokenOrNull(StringTokenizer receiver) {
        return receiver.hasMoreTokens() ? receiver.nextToken() : null;
    }

    private String getErrorCode(String message) {
        if (message == null) {
            return null;
        }

        StringTokenizer st1 = new StringTokenizer(message, ",");
        this.nextTokenOrNull(st1);
        this.nextTokenOrNull(st1);
        String data = this.nextTokenOrNull(st1);
        if (data == null) {
            return null;
        }

        StringTokenizer st2 = new StringTokenizer(data.trim(), " ");
        st2.nextToken();
        return st2.nextToken();
    }

    static Set<String> b00013LdapCodes = new HashSet<>();
    static {
    	b00013LdapCodes.add(LdapResult.USER_NOT_FOUND.code);
    	b00013LdapCodes.add(LdapResult.INVALID_CREDENTIAL.code);
    	b00013LdapCodes.add(LdapResult.PASSWORD_EXPIRED.code);
    	b00013LdapCodes.add(LdapResult.USER_NOT_BE_GRANTED.code);
    	b00013LdapCodes.add(LdapResult.ACCOUNT_EXPIRED.code);
    	b00013LdapCodes.add(LdapResult.USER_MUST_RESET.code);
    }

    static Set<String> b00014LdapCodes = new HashSet<>();
    static {
    	b00014LdapCodes.add(LdapResult.NOT_PERMIT_LOGON.code);
    	b00014LdapCodes.add(LdapResult.NOT_PERMIT_LOGON_WORKSTATION.code);
    	b00014LdapCodes.add(LdapResult.ACCOUNT_DISABLED.code);
    }

    static Set<String> b00015LdapCodes = new HashSet<>();
    static {
    	b00015LdapCodes.add(LdapResult.USER_LOCKED.code);
    }
    
    private UnauthorizedException getException(String ldapResultCode, Throwable throwable) {
    	String code = ErrorCodes.UNKNOWN.name();
    	String message = ErrorCodes.UNKNOWN.getMessage();
        
        if (b00013LdapCodes.contains(ldapResultCode)) {
            code = ErrorCodes.B00013.name();
            message = ErrorCodes.B00013.getMessage();	
        } else if (b00014LdapCodes.contains(ldapResultCode)) {
        	code = ErrorCodes.B00014.name();
        	message = ErrorCodes.B00014.getMessage();
        } else if (b00015LdapCodes.contains(ldapResultCode)) {
        	code = ErrorCodes.B00015.name();
        	message = ErrorCodes.B00015.getMessage();	
        } 
        
        UnauthorizedException unauthorizedException = new UnauthorizedException(code, message, throwable);
        
        LdapResult ldapResult = LdapResult.findByCode(ldapResultCode);
        
    	OriginalExceptionDetail originalExceptionDetail = OriginalExceptionDetail.builder()
    			.originateFrom(ExceptionOrigins.LDAP.code)
    				.onOperation("BIND")
    					.resultInCode("ldapResult.code")
    						.withDescription(ldapResult.description)
    							.build();
    	unauthorizedException.setOriginalExceptionDetail(originalExceptionDetail);
        return unauthorizedException;
    }
}
