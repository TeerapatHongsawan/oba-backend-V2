package th.co.scb.onboardingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.exception.NotFoundException;
import th.co.scb.onboardingapp.model.ConsentContentInfo;
import th.co.scb.onboardingapp.model.ConsentContentRequest;
import th.co.scb.onboardingapp.model.entity.ConsentContentEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InitConsentContentCommandHandlerTest {

    @InjectMocks
    private InitConsentContentCommandHandler initConsentContentCommandHandler;

    @Mock
    private ConsentService consentService;

    @Test
    public void testGetConsentCrossSellAndMarketing() throws IOException {
        List<ConsentContentInfo> mockResponse = getMockConsentCrossSellAndMarketingRequest();

        when(consentService.getConsentCrossSellAndMarketing(any(), any())).thenReturn(getMockConsentCrossSellAndMarketing());

        List<ConsentContentRequest> consentContentRequests = new ArrayList<>();
        ConsentContentRequest consentCrossSellAndMarketing = new ConsentContentRequest();
        consentCrossSellAndMarketing.setConsentType(ConsentContentEntity.CROSS_SELL_CONSENT_TYPE);
        consentContentRequests.add(consentCrossSellAndMarketing);

        List<ConsentContentInfo> response = initConsentContentCommandHandler.initConsentContent(consentContentRequests);
        assertEquals(mockResponse, response);
    }

    @Test
    public void testGetConsentNotice() throws IOException {
        List<ConsentContentInfo> mockResponse = getMockConsentNoticeRequest();

        when(consentService.getConsentNotice(any())).thenReturn(getMockConsentNotice());

        List<ConsentContentRequest> consentContentRequests = new ArrayList<>();
        ConsentContentRequest consentNotice = new ConsentContentRequest();
        consentNotice.setConsentType(ConsentContentEntity.PRIVACY_NOTICE_SHORT_TYPE);
        consentContentRequests.add(consentNotice);

        List<ConsentContentInfo> response = initConsentContentCommandHandler.initConsentContent(consentContentRequests);
        assertEquals(mockResponse, response);
    }

    @Test
    public void testGetConsentCrossSellPartner() throws IOException {
        List<ConsentContentInfo> mockResponse = getMockConsentCrossSellPartnerRequest();

        when(consentService.getConsentCrossSellPartner(any(), any())).thenReturn(getMockConsentCrossSellPartner());

        List<ConsentContentRequest> consentContentRequests = new ArrayList<>();
        ConsentContentRequest consentCrossSellPartner = new ConsentContentRequest();
        consentCrossSellPartner.setConsentType(ConsentContentEntity.CROSS_SELL_PARTNER_CONSENT_TYPE);
        consentContentRequests.add(consentCrossSellPartner);

        List<ConsentContentInfo> response = initConsentContentCommandHandler.initConsentContent(consentContentRequests);
        assertEquals(mockResponse, response);
    }

    @Test
    public void testGetConsentByTypeAndVendor() throws IOException {
        List<ConsentContentInfo> mockResponse = getMockConsentByTypeAndVendorRequest();

        when(consentService.getConsentByTypeAndVendor(any(), any())).thenReturn(getMockConsentByTypeAndVendor());

        List<ConsentContentRequest> consentContentRequests = new ArrayList<>();
        ConsentContentRequest consentByTypeAndVendor = new ConsentContentRequest();
        consentByTypeAndVendor.setConsentType(ConsentContentEntity.SENSITIVE_CONSENT_TYPE);
        consentContentRequests.add(consentByTypeAndVendor);

        List<ConsentContentInfo> response = initConsentContentCommandHandler.initConsentContent(consentContentRequests);
        assertEquals(mockResponse, response);
    }

    @Test
    public void testGetConsentByTypeAndVendorDopa() throws IOException {
        List<ConsentContentInfo> mockResponse = getMockConsentByTypeAndVendorRequest();

        when(consentService.getConsentByTypeAndVendor(any(), any())).thenReturn(getMockConsentByTypeAndVendor());

        List<ConsentContentRequest> consentContentRequests = new ArrayList<>();
        ConsentContentRequest consentByTypeAndVendor = new ConsentContentRequest();
        consentByTypeAndVendor.setConsentType(ConsentContentEntity.DOPA_CONSENT_TYPE);
        consentContentRequests.add(consentByTypeAndVendor);

        List<ConsentContentInfo> response = initConsentContentCommandHandler.initConsentContent(consentContentRequests);
        assertEquals(mockResponse, response);
    }

    @Test
    public void testGetConsentIsNull() throws IOException {
        List<ConsentContentInfo> mockResponse = getMockConsentByTypeAndVendorRequest();

        when(consentService.getConsentByTypeAndVendor(any(), any())).thenReturn(getMockConsentIsNull());

        List<ConsentContentRequest> consentContentRequests = new ArrayList<>();
        ConsentContentRequest consentByTypeAndVendor = new ConsentContentRequest();
        consentByTypeAndVendor.setConsentType(ConsentContentEntity.SENSITIVE_CONSENT_TYPE);
        consentContentRequests.add(consentByTypeAndVendor);

        assertThrows(NotFoundException.class, () -> initConsentContentCommandHandler.initConsentContent(consentContentRequests));
    }

    @Test
    public void testGetConsentTypeNotMatch() throws IOException {
        List<ConsentContentInfo> mockResponse = getMockConsentTypeNotMatchRequest();

        List<ConsentContentRequest> consentContentRequests = new ArrayList<>();
        ConsentContentRequest consentByTypeAndVendor = new ConsentContentRequest();
        consentByTypeAndVendor.setConsentType("");
        consentContentRequests.add(consentByTypeAndVendor);

        assertThrows(RuntimeException.class, () ->  initConsentContentCommandHandler.initConsentContent(consentContentRequests));
    }

    private List<ConsentContentInfo> getMockConsentCrossSellAndMarketingRequest() {
        List<ConsentContentInfo> consentContentInfos = new ArrayList<>();
        ConsentContentInfo consentContentInfo002 = new ConsentContentInfo();
        consentContentInfo002.setContent("AAAAA");
        consentContentInfo002.setConsentType(ConsentContentEntity.CROSS_SELL_CONSENT_TYPE);
        consentContentInfo002.setContentName("");
        consentContentInfo002.setContentTitle("");
        consentContentInfo002.setConsentVersion("");
        consentContentInfo002.setConsentUpdateDate(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));
        consentContentInfos.add(consentContentInfo002);

        return consentContentInfos;
    }


    private List<ConsentContentInfo> getMockConsentNoticeRequest() {
        List<ConsentContentInfo> consentContentInfos = new ArrayList<>();
        ConsentContentInfo consentContentInfo004 = new ConsentContentInfo();
        consentContentInfo004.setContent("AAAAA");
        consentContentInfo004.setConsentType(ConsentContentEntity.PRIVACY_NOTICE_SHORT_TYPE);
        consentContentInfo004.setContentName("");
        consentContentInfo004.setContentTitle("");
        consentContentInfo004.setConsentVersion("");
        consentContentInfo004.setConsentUpdateDate(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));
        consentContentInfos.add(consentContentInfo004);

        return consentContentInfos;
    }

    private List<ConsentContentInfo> getMockConsentCrossSellPartnerRequest() {
        List<ConsentContentInfo> consentContentInfos = new ArrayList<>();
        ConsentContentInfo consentContentInfo002C = new ConsentContentInfo();
        consentContentInfo002C.setContent("AAAAA");
        consentContentInfo002C.setConsentType(ConsentContentEntity.CROSS_SELL_PARTNER_CONSENT_TYPE);
        consentContentInfo002C.setContentName("");
        consentContentInfo002C.setContentTitle("");
        consentContentInfo002C.setConsentVersion("");
        consentContentInfo002C.setConsentUpdateDate(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));
        consentContentInfos.add(consentContentInfo002C);

        return consentContentInfos;
    }

    private List<ConsentContentInfo> getMockConsentByTypeAndVendorRequest() {
        List<ConsentContentInfo> consentContentInfos = new ArrayList<>();
        ConsentContentInfo consentContentInfo005 = new ConsentContentInfo();
        consentContentInfo005.setContent("AAAAA");
        consentContentInfo005.setConsentType(ConsentContentEntity.SENSITIVE_CONSENT_TYPE);
        consentContentInfo005.setContentName("");
        consentContentInfo005.setContentTitle("");
        consentContentInfo005.setConsentVersion("");
        consentContentInfo005.setConsentUpdateDate(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));
        consentContentInfos.add(consentContentInfo005);

        return consentContentInfos;
    }

    private List<ConsentContentInfo> getMockConsentTypeNotMatchRequest() {
        List<ConsentContentInfo> consentContentInfos = new ArrayList<>();
        ConsentContentInfo consentContentInfo005 = new ConsentContentInfo();
        consentContentInfo005.setContent("s");
        consentContentInfo005.setConsentType("");
        consentContentInfo005.setContentName("");
        consentContentInfo005.setContentTitle("");
        consentContentInfo005.setConsentVersion("");
        consentContentInfo005.setConsentUpdateDate(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));
        consentContentInfos.add(consentContentInfo005);

        return consentContentInfos;
    }

    private ConsentContentInfo getMockConsentCrossSellAndMarketing() {
        ConsentContentInfo consentContentInfo = new ConsentContentInfo();
        consentContentInfo.setContent("AAAAA");
        consentContentInfo.setConsentType(ConsentContentEntity.CROSS_SELL_CONSENT_TYPE);
        consentContentInfo.setContentName("");
        consentContentInfo.setContentTitle("");
        consentContentInfo.setConsentVersion("");
        consentContentInfo.setConsentUpdateDate(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));

        return consentContentInfo;

    }

    private ConsentContentInfo getMockConsentNotice() {
        ConsentContentInfo consentContentInfo = new ConsentContentInfo();
        consentContentInfo.setContent("AAAAA");
        consentContentInfo.setConsentType(ConsentContentEntity.PRIVACY_NOTICE_SHORT_TYPE);
        consentContentInfo.setContentName("");
        consentContentInfo.setContentTitle("");
        consentContentInfo.setConsentVersion("");
        consentContentInfo.setConsentUpdateDate(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));

        return consentContentInfo;

    }


    private ConsentContentInfo getMockConsentCrossSellPartner() {
        ConsentContentInfo consentContentInfo = new ConsentContentInfo();
        consentContentInfo.setContent("AAAAA");
        consentContentInfo.setConsentType(ConsentContentEntity.CROSS_SELL_PARTNER_CONSENT_TYPE);
        consentContentInfo.setContentName("");
        consentContentInfo.setContentTitle("");
        consentContentInfo.setConsentVersion("");
        consentContentInfo.setConsentUpdateDate(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));

        return consentContentInfo;

    }

    private ConsentContentInfo getMockConsentByTypeAndVendor() {
        ConsentContentInfo consentContentInfo = new ConsentContentInfo();
        consentContentInfo.setContent("AAAAA");
        consentContentInfo.setConsentType(ConsentContentEntity.SENSITIVE_CONSENT_TYPE);
        consentContentInfo.setContentName("");
        consentContentInfo.setContentTitle("");
        consentContentInfo.setConsentVersion("");
        consentContentInfo.setConsentUpdateDate(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));

        return consentContentInfo;

    }

    private ConsentContentInfo getMockConsentByTypeAndVendorDopa() {
        ConsentContentInfo consentContentInfo = new ConsentContentInfo();
        consentContentInfo.setContent("AAAAA");
        consentContentInfo.setConsentType(ConsentContentEntity.DOPA_CONSENT_TYPE);
        consentContentInfo.setContentName("");
        consentContentInfo.setContentTitle("");
        consentContentInfo.setConsentVersion("");
        consentContentInfo.setConsentUpdateDate(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));

        return consentContentInfo;

    }

    private ConsentContentInfo getMockConsentIsNull() {
        ConsentContentInfo consentContentInfo = new ConsentContentInfo();
        consentContentInfo.setContent("");
        consentContentInfo.setConsentType(ConsentContentEntity.SENSITIVE_CONSENT_TYPE);
        consentContentInfo.setContentName("");
        consentContentInfo.setContentTitle("");
        consentContentInfo.setConsentVersion("");
        consentContentInfo.setConsentUpdateDate(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));

        return consentContentInfo;

    }
    private ConsentContentInfo getMockConsentTypeNotMatch() {
        ConsentContentInfo consentContentInfo = new ConsentContentInfo();
        consentContentInfo.setContent("AAA");
        consentContentInfo.setConsentType("");
        consentContentInfo.setContentName("");
        consentContentInfo.setContentTitle("");
        consentContentInfo.setConsentVersion("");
        consentContentInfo.setConsentUpdateDate(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));

        return consentContentInfo;

    }
}