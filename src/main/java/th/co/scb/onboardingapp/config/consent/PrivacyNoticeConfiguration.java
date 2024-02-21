package th.co.scb.onboardingapp.config.consent;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class PrivacyNoticeConfiguration {
	private String link;
	private String linkEN;
	private String version;
	private String type;
}
