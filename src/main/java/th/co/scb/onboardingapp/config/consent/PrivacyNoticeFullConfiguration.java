package th.co.scb.onboardingapp.config.consent;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class PrivacyNoticeFullConfiguration {
	private String link;
	private String linkEN;
	private String version;
	private String type;
}
