package th.co.scb.onboardingapp.config.ext.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import th.co.scb.captiva.CaptivaApiConfig;
import th.co.scb.facetech.FaceTechApiConfig;
import th.co.scb.vaultFaceTech.VaultFaceTechApi;
import th.co.scb.vaultFaceTech.VaultFaceTechApiConfig;
import th.co.scb.vaultFaceTech.model.LoginResponse;

@Configuration
@Data
public class VaultFaceTechApiServiceConfig {

    @Value("${vault-facetech.server}")
    String vaultFacetechServer;

    @Value("${vault-facetech.loginUrl}")
    String vaultFacetechLoginUrl;

    @Value("${vault-facetech.secretUrl}")
    String vaultFacetechSecretUrl;

    @Value("${vault-facetech.transitUrl}")
    String vaultFacetechTransitUrl;

    @Value("${vault-facetech.roleId}")
    String vaultFacetechRoleId;

    @Value("${vault-facetech.secretId}")
    String vaultFacetechSecretId;

    @Bean
    @ConfigurationProperties(prefix = "vault-facetech")
    public VaultFaceTechApiConfig vaultFaceTechApiConfig(ObjectMapper objectMapper) {
        VaultFaceTechApiConfig result = new VaultFaceTechApiConfig();

        result.setLoginUrl(vaultFacetechLoginUrl);
        result.setRoleId(vaultFacetechRoleId);
        result.setTransitUrl(vaultFacetechTransitUrl);
        result.setSecretId(vaultFacetechSecretId);
        result.setSecretUrl(vaultFacetechSecretUrl);
        result.setServer(vaultFacetechServer);
        return result;
    }
    @Bean
    public VaultFaceTechApi vaultFaceTechApi(VaultFaceTechApiConfig vaultApiConfig) {

        return new VaultFaceTechApi(vaultApiConfig);
    }
}
