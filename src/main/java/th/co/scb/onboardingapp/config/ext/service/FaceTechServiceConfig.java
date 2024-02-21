package th.co.scb.onboardingapp.config.ext.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import th.co.scb.facetech.FaceTechApi;
import th.co.scb.facetech.FaceTechApiConfig;

@Configuration
public class FaceTechServiceConfig {

	@Bean
	@ConfigurationProperties(prefix = "ent-api")
	public FaceTechApiConfig faceTechApiConfig(ObjectMapper objectMapper) {
		FaceTechApiConfig result = new FaceTechApiConfig();
		result.setObjectMapper(objectMapper);
		return result;
	}

	@Bean
	public FaceTechApi faceTechApi(FaceTechApiConfig faceTechApiConfig) {
		return new FaceTechApi(faceTechApiConfig);
	}

}
