package th.co.scb.onboardingapp.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.facetech.model.ConsentValidationResponse;
import th.co.scb.onboardingapp.config.consent.ConsentServiceConfiguration;
import th.co.scb.onboardingapp.model.ConsentContentInfo;
import th.co.scb.onboardingapp.repository.ConsentContentJpaRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static th.co.scb.onboardingapp.model.entity.ConsentContentEntity.CROSSMATCH_CONSENT_TYPE;
import static th.co.scb.onboardingapp.model.entity.ConsentContentEntity.MARKETING_CONSENT_TYPE;

@ExtendWith(MockitoExtension.class)
public class ConsentServiceTest {

    @InjectMocks
    private ConsentService consentService;

    @Mock
    private ConsentContentJpaRepository consentContentRepository;

    @Mock
    private FaceTechConsentValidateService faceTechConsentValidateService;

    @Mock
    private ConsentServiceConfiguration consentServiceConfiguration;

    @Mock
    private JmxFlags featureFlags;

    @Test
    public void getConsentCrossSellAndMarketingTest() {
        ConsentContentInfo consentContentInfo = consentService.getConsentCrossSellAndMarketing(MARKETING_CONSENT_TYPE, "1.0");

        assertThat(consentContentInfo).isNotNull();
    }

    @Test
    public void getConsentCrossMatchTest() {
        when(faceTechConsentValidateService.getConsentValidate(any())).thenReturn(getConsentValidationResponse());

        ConsentContentInfo consentContentInfo = consentService.getConsentCrossMatch(CROSSMATCH_CONSENT_TYPE, "0000000012345");

        assertThat(consentContentInfo).isNotNull();
    }

    @Test
    public void getConsentByTypeAndVendorTest() {
        ConsentContentInfo consentContentInfo = consentService.getConsentByTypeAndVendor(MARKETING_CONSENT_TYPE, "SCB");

        assertThat(consentContentInfo.getContent()).isNull();
    }

    @SneakyThrows
    private ConsentValidationResponse getConsentValidationResponse() {
        return new ConsentValidationResponse();
    }

}