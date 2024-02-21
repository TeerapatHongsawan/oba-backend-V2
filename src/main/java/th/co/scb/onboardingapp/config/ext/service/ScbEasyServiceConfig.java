package th.co.scb.onboardingapp.config.ext.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import th.co.scb.scbeasy.ScbEasyApi;
import th.co.scb.scbeasy.ScbEasyApiConfig;

@Configuration
public class ScbEasyServiceConfig {

	@Bean
	@ConfigurationProperties(prefix = "fasteasy")
	public ScbEasyApiConfig scbEasyApiConfig(ObjectMapper objectMapper) {
		ScbEasyApiConfig result = new ScbEasyApiConfig();
		result.setObjectMapper(objectMapper);

		return result;
	}

	@Bean
	public ScbEasyApi scbEasyApi(ScbEasyApiConfig scbEasyApiConfig) {
		return new ScbEasyApi(scbEasyApiConfig);
	}

}
