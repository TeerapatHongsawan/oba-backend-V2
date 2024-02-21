package th.co.scb.onboardingapp.config.consent;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ConsentServiceConfiguration {
	private PrivacyNoticeConfiguration privacyNotice = new PrivacyNoticeConfiguration();
	private PrivacyNoticeFullConfiguration privacyNoticeFull = new PrivacyNoticeFullConfiguration();
}
