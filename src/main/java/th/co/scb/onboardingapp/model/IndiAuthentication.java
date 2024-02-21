package th.co.scb.onboardingapp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class IndiAuthentication implements Authentication {

    @Getter
    private String name;

    @Getter
    private String branchId;

    @Getter
    @Setter
    private String caseId;

    @Getter
    private String appName;

    @Getter(lazy = true)
    private final Collection<? extends GrantedAuthority> authorities = rolesToAuthorities();

    @Getter
    private Set<String> roles;

    public IndiAuthentication(String username, Set<String> roles, String branchId, String caseId, String appName) {
        this.name = username;
        this.roles = roles;
        this.branchId = branchId;
        this.caseId = caseId;
        this.appName = appName;
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities() {
        return roles.stream().map(it -> new SimpleGrantedAuthority("ROLE_" + it)).collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.name;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Authentication is immutable");
    }
}