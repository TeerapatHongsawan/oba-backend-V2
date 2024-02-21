package th.co.scb.onboardingapp.config.ext.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import th.co.scb.captiva.CaptivaApi;
import th.co.scb.captiva.CaptivaApiConfig;

@Configuration
public class CaptivaApiServiceConfig {

	@Bean
	@ConfigurationProperties(prefix = "captiva-api")
	public CaptivaApiConfig captivaApiConfig(ObjectMapper objectMapper) {
		CaptivaApiConfig result = new CaptivaApiConfig();
		result.setObjectMapper(objectMapper);
		return result;
	}

	@Bean
	public CaptivaApi captivaApi(CaptivaApiConfig captivaApiConfig) {
		return new CaptivaApi(captivaApiConfig);
	}

}
