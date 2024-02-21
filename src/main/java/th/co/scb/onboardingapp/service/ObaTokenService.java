package th.co.scb.onboardingapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.StartbizUserProfilesDetail;
import th.co.scb.onboardingapp.model.TokenInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ObaTokenService {

    @Value("${security.jwt.signing-key}")
    private String signingKey;

    @Value("${security.jwt.expiration-seconds}")
    private Long expirationSeconds;

    public TokenInfo create(ObaAuthentication authentication) {
        long expiresIn = expirationSeconds * 1000;
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("scopes", authentication.getRoles())
                .claim("case", authentication.getCaseId())
                .claim("branch", authentication.getBranchId())
                .claim("app", authentication.getAppName())
                .claim("startbiz", authentication.getStb())
                .setExpiration(new Date(System.currentTimeMillis() + expiresIn))
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();

        return new TokenInfo(token, "Bearer", expiresIn);
    }

    public ObaAuthentication parse(String token) {
        Jws<Claims> jwt = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token);

        String username = jwt.getBody().getSubject();
        List<?> scopes = (List<?>) jwt.getBody().get("scopes");

        Set<String> roles = scopes == null ? Collections.emptySet() : scopes.stream().map(Object::toString).collect(Collectors.toSet());
        String caseId = (String) jwt.getBody().get("case");
        String branchId = (String) jwt.getBody().get("branch");
        String appName = (String) jwt.getBody().get("app");
        StartbizUserProfilesDetail stb;
        try{
            Map<?,?> map = (Map<?, ?>) jwt.getBody().get("startbiz");
            ObjectMapper mapper = new ObjectMapper();
            stb = mapper.convertValue(map, StartbizUserProfilesDetail.class);
        } catch (Exception e){
            stb = new StartbizUserProfilesDetail();
        }
        return new ObaAuthentication(username, roles, branchId, caseId, appName, stb);
    }

    public String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        } else {
            return request.getParameter("token");
        }
    }
}
