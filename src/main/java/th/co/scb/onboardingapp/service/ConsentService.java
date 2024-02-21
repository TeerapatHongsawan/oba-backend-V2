package th.co.scb.onboardingapp.service;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import th.co.scb.facetech.model.ConsentData;
import th.co.scb.facetech.model.ConsentValidateInfo;
import th.co.scb.facetech.model.ConsentValidationResponse;
import th.co.scb.onboardingapp.config.consent.ConsentServiceConfiguration;
import th.co.scb.onboardingapp.config.consent.PrivacyNoticeConfiguration;
import th.co.scb.onboardingapp.config.consent.PrivacyNoticeFullConfiguration;
import th.co.scb.onboardingapp.exception.ApplicationException;
import th.co.scb.onboardingapp.model.ConsentContentInfo;
import th.co.scb.onboardingapp.model.entity.ConsentContentEntity;
import th.co.scb.onboardingapp.repository.ConsentContentJpaRepository;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.Objects.nonNull;
import static th.co.scb.onboardingapp.model.entity.ConsentContentEntity.*;

@Slf4j
@Service
public class ConsentService {

    @Autowired(required = false)
    private JmxFlags featureFlags;

    @Value("${consent.link}")
    private String consentLink;

    @Autowired
    FaceTechConsentValidateService faceTechConsentValidateService;

    @Autowired
    ConsentContentJpaRepository consentContentRepository;

    @Autowired
    ConsentServiceConfiguration consentServiceConfiguration;

    List<ConsentContentEntity> consentContents;

    public ConsentContentInfo getConsentCrossSellAndMarketing(String type, String version) {
        this.consentContents = getConsentContents();
        ConsentContentEntity consentContent = filterContentByTypeAndVersion(consentContents, type, version).stream().findFirst().orElse(null);
        return buildData(consentContent);
    }

    public ConsentContentInfo getConsentNotice(String type) {
        this.consentContents = getConsentContents();
        ConsentContentEntity consentContent = getExistingContentNotice(this.consentContents, type);
        return buildData(consentContent);
    }

    public ConsentContentInfo getConsentCrossSellPartner(String type, String link) {
        this.consentContents = getConsentContents();
        link = addLinkConsent(link);
        ConsentContentEntity consentContent = getExistingContentPartner(this.consentContents, type, link);
        return buildData(consentContent);
    }
    public ConsentContentInfo getConsentByTypeAndVendor(String type, String vendor) {
        this.consentContents = getConsentContents();
        ConsentContentEntity consentContent = consentContents.stream()
                .filter(i -> type.equalsIgnoreCase(i.getConsentType()) && vendor.equalsIgnoreCase(i.getVendor()))
                .sorted(Comparator.comparing(ConsentContentEntity::getConsentVersion).reversed())
                .findFirst()
                .orElse(null);
        return buildData(consentContent);
    }

    public ConsentContentInfo getConsentCrossMatch(String type, String referenceId) {
        ConsentValidationResponse consentValidationResponse = faceTechConsentValidateService.getConsentValidate(referenceId);
        ConsentData consentContentData = consentValidationResponse.getData();
        ConsentValidateInfo[] consentValidateInfos = nonNull(consentContentData) ? consentContentData.getConsentInfo() : null;
        ConsentValidateInfo consentContent = (consentValidateInfos != null && consentValidateInfos.length > 0) ? consentValidateInfos[0] : null;
        ConsentContentInfo consentContentInfo = new ConsentContentInfo();
        if (nonNull(consentContent)) {
            consentContentInfo.setConsentType(consentContent.getConsentType());
            consentContentInfo.setConsentVersion(consentContent.getConsentVersion());
            consentContentInfo.setConsentStatus(consentContent.getConsentStatus());
            consentContentInfo.setContentName(consentContent.getConsentName());
            consentContentInfo.setConsentUpdateDate(LocalDateTime.now().withNano(0));
            if (nonNull(consentContent.getContent())) {
                consentContentInfo.setContent(validateConsentContent(consentContent.getContent().getBody()));
                consentContentInfo.setContentTitle(consentContent.getContent().getTitle());
            }
            this.consentContents = getConsentContents();
            ConsentContentEntity consentContentEntity = filterContentByType(consentContents, type).stream().findFirst().orElse(null);
            if (nonNull(consentContentEntity)) {
                consentContentInfo.setContent(consentContentInfo.getContent() + consentContentEntity.getContent());
            }
        }
        return consentContentInfo;
    }

    public List<ConsentContentEntity> getConsentContents() {
        return consentContentRepository.findAll();
    }

    private List<ConsentContentEntity> filterContentByTypeAndVersion(List<ConsentContentEntity> contents, String type, String version) {

        return contents.stream().filter(i -> type.equalsIgnoreCase(i.getConsentType()) && version.equalsIgnoreCase(i.getConsentVersion()))
                .sorted(Comparator.comparing(ConsentContentEntity::getUpdateDate).reversed()).collect(Collectors.toList());
    }

    private ConsentContentInfo buildData(ConsentContentEntity consentContent) {
        ConsentContentInfo consentContentInfo = new ConsentContentInfo();
        if (consentContent != null) {
            consentContentInfo.setConsentType(consentContent.getConsentType());
            consentContentInfo.setConsentVersion(consentContent.getConsentVersion());
            consentContentInfo.setConsentUpdateDate(consentContent.getUpdateDate());
            consentContentInfo.setContent(consentContent.getContent());
            consentContentInfo.setContentEN(consentContent.getContentEN());
            consentContentInfo.setContentName(consentContent.getConsentName());
            consentContentInfo.setContentTitle(consentContent.getContentTitle());
        }
        return consentContentInfo;
    }

    private List<ConsentContentEntity> filterContentByType(List<ConsentContentEntity> contents, String type) {

        return contents.stream().filter(i -> type.equalsIgnoreCase(i.getConsentType()))
                .sorted(Comparator.comparing(ConsentContentEntity::getUpdateDate).reversed()).collect(Collectors.toList());
    }

    private ConsentContentEntity filterContentToDate(List<ConsentContentEntity> contents) {

        return contents.stream()
                .filter(i -> !i.getUpdateDate().toLocalDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(ConsentContentEntity::getUpdateDate).reversed())
                .findFirst()
                .orElse(null);
    }

    public PrivacyNoticeFullConfiguration getPrivacyNoticeFull() {
        return this.consentServiceConfiguration.getPrivacyNoticeFull();
    }

    public PrivacyNoticeConfiguration getPrivacyNotice() {
        return this.consentServiceConfiguration.getPrivacyNotice();
    }

    public ConsentContentEntity getExistingContentNotice(List<ConsentContentEntity> contents, String type) {

        List<ConsentContentEntity> consentType = filterContentByType(contents, type);
        ConsentContentEntity consentContent = consentType.stream().findFirst().orElse(null);
        ConsentContentEntity consentfilter = filterContentToDate(consentType);
        String link = "";
        String linkEN = "";
        String version = "";

        if (consentfilter == null) {
            if (type.equalsIgnoreCase(PRIVACY_NOTICE_FULL_TYPE)) {
                PrivacyNoticeFullConfiguration privacyNotice = getPrivacyNoticeFull();
                link = privacyNotice.getLink();
                linkEN =privacyNotice.getLinkEN();
                version = privacyNotice.getVersion();
            } else {
                PrivacyNoticeConfiguration privacyNotice = getPrivacyNotice();
                link = privacyNotice.getLink();
                linkEN =privacyNotice.getLinkEN();
                version = privacyNotice.getVersion();
            }

            log.info("Update Version ContentNotice");
            try {
                String gethtml = "";
                String gethtmlEN = "";
                if (consentContent == null) {

                    gethtml = gethtml(link);
                    gethtmlEN = gethtml(linkEN);
                    consentContent = new ConsentContentEntity();
                    consentContent.setConsentType(type);
                    consentContent.setConsentVersion(version);
                    consentContent.setLink(link);
                    consentContent.setLinkEN(linkEN);
                    consentContent.setCreateDate(LocalDateTime.now());

                } else {
                    if (Strings.isNullOrEmpty(consentContent.getLink())) {
                        consentContent.setLink(link);
                    }
                    if (Strings.isNullOrEmpty(consentContent.getLinkEN())) {
                        consentContent.setLinkEN(linkEN);
                    }
                    if (Strings.isNullOrEmpty(consentContent.getConsentVersion())) {
                        consentContent.setConsentVersion(version);
                    }
                    if (Strings.isNullOrEmpty(consentContent.getConsentType())) {
                        consentContent.setConsentType(type);
                    }
                    gethtml = gethtml(consentContent.getLink());
                    gethtmlEN = gethtml(consentContent.getLinkEN());
                }

                this.consentContents.remove(consentContent);
                consentContent.setContent(gethtml);
                consentContent.setContentEN(gethtmlEN);
                consentContent.setUpdateDate(LocalDateTime.now());
                consentContent.setVendor("SCB");
                consentContent.setConsentName("Privacy notices");
                this.consentContents.add(consentContent);
                saveConsent(consentContent);
            } catch (Exception ex) {
                log.error("cannot get consent html", ex);
            }
            return consentContent;
        } else {
            return consentfilter;
        }
    }

    public String gethtml(String url) throws IOException {
        log.info("get html:" + url);
        if (featureFlags != null && featureFlags.has("DisableS3")) {
            throw new ApplicationException("S3 Link Disabled");
        }
        StringBuilder result = new StringBuilder();

        URL weburl = new URL(url);
        BufferedReader rd;
        if (weburl.getProtocol().equalsIgnoreCase("http")) {

            HttpURLConnection webProxyConnection = (HttpURLConnection) weburl.openConnection();
            rd = new BufferedReader(new InputStreamReader(webProxyConnection.getInputStream()));
        } else {
            HttpsURLConnection webProxyConnection = (HttpsURLConnection) weburl.openConnection();
            rd = new BufferedReader(new InputStreamReader(webProxyConnection.getInputStream()));
        }

        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        if (result.toString().trim().equalsIgnoreCase("")) {
            throw new ApplicationException("get Html Empty");
        }
        log.info("get html:" + url + " Successful");
        return result.toString();
    }

    private void saveConsent(ConsentContentEntity c) {
        ConsentContentEntity consentContent = consentContentRepository.findByConsentTypeAndConsentVersionAndLinkAndVendorAndLinkEN(c.getConsentType(), c.getConsentVersion(), c.getLink(), c.getVendor(), c.getLinkEN()).stream().findFirst().orElse(null);

        if (consentContent == null) {
            consentContentRepository.save(c);
        } else {
            consentContent.setContentTitle(c.getContentTitle());
            consentContent.setContent(c.getContent());
            consentContent.setContentEN(c.getContentEN());
            consentContent.setConsentName(c.getConsentName());
            consentContent.setUpdateDate(LocalDateTime.now());
            consentContentRepository.save(consentContent);
        }
    }

    private String addLinkConsent(String link) {
        consentServiceConfiguration.getPrivacyNotice();
        return consentLink + link;
    }

    public ConsentContentEntity getExistingContentPartner(List<ConsentContentEntity> contents, String type, String link) {

        List<ConsentContentEntity> consentType = filterContentByTypeAndLink(contents, type, link);
        ConsentContentEntity consentContent = consentType.stream().findFirst().orElse(null);

        ConsentContentEntity consentfilter = filterContentToDate(consentType);

        if (consentfilter == null) {
            log.info("Update Version ContentPartner");
            try {
                String gethtml = "";
                if (consentContent == null) {

                    gethtml = gethtml(link);
                    consentContent = new ConsentContentEntity();
                    consentContent.setConsentType(type);

                    consentContent.setLink(link);
                    consentContent.setCreateDate(LocalDateTime.now());

                } else {
                    if (Strings.isNullOrEmpty(consentContent.getLink())) {
                        consentContent.setLink(link);
                    }
                    if (Strings.isNullOrEmpty(consentContent.getConsentType())) {
                        consentContent.setConsentType(type);
                    }
                    gethtml = gethtml(consentContent.getLink());
                }

                this.consentContents.remove(consentContent);
                consentContent.setContent(gethtml);
                consentContent.setUpdateDate(LocalDateTime.now());
                consentContent.setVendor("SCB");
                consentContent.setConsentName("Cross sell Business Partner");
                this.consentContents.add(consentContent);

                saveConsent(consentContent);
            } catch (Exception ex) {
                log.error("cannot get consent html", ex);
            }
            return consentContent;
        } else {
            return consentfilter;
        }

    }

    private List<ConsentContentEntity> filterContentByTypeAndLink(List<ConsentContentEntity> contents, String type, String link) {

        return contents.stream().filter(i -> type.equalsIgnoreCase(i.getConsentType()) && link.equalsIgnoreCase(i.getLink()))
                .sorted(Comparator.comparing(ConsentContentEntity::getUpdateDate).reversed()).collect(Collectors.toList());
    }

    private String validateConsentContent(String content) {
        if (nonNull(content)) {
            String[] contents = content.split("</h3> ", 2);
            return (contents.length > 0) ? contents[1] : content;
        }
        return null;
    }

    public ConsentContentInfo saveConsentCrossSellAndMarketing(String type, String version, String link, String vendor, String title, String body) {
        this.consentContents = getConsentContents();
        ConsentContentEntity consentContent = saveExistingCrossSellAndMarketing(this.consentContents, type, version, link, vendor, title, body);
        return buildData(consentContent);
    }

    public ConsentContentEntity saveExistingCrossSellAndMarketing(List<ConsentContentEntity> contents, String type, String version, String link, String vendor, String title, String body) {

        List<ConsentContentEntity> collect = contents.stream()
                .filter(i -> type.equalsIgnoreCase(i.getConsentType()) &&
                        version.equalsIgnoreCase(i.getConsentVersion()) &&
                        link.equalsIgnoreCase(i.getLink()) &&
                        vendor.equalsIgnoreCase(i.getVendor())).collect(Collectors.toList());
        ConsentContentEntity consentfilter = collect.stream().filter(i -> !i.getUpdateDate().toLocalDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(ConsentContentEntity::getUpdateDate).reversed())
                .findFirst()
                .orElse(null);

        if (consentfilter == null) {
            log.info("Insert Version CrossSellAndMarketing");
            ConsentContentEntity consentContent = new ConsentContentEntity();

            consentContent.setContent(body);
            consentContent.setConsentVersion(version);
            consentContent.setConsentType(type);
            consentContent.setVendor(vendor);
            consentContent.setLink(link);
            consentContent.setCreateDate(LocalDateTime.now());
            consentContent.setUpdateDate(LocalDateTime.now());
            consentContent.setContentTitle(title);

            if (type.equalsIgnoreCase(MARKETING_CONSENT_TYPE)) {
                consentContent.setConsentName("Marketing consent");
            } else if (type.equalsIgnoreCase(CROSS_SELL_CONSENT_TYPE)) {
                consentContent.setConsentName("Cross sell consent");
            } else if (type.equalsIgnoreCase(MODEL_CONSENT_TYPE)) {
                consentContent.setConsentName("Model consent");
            }
            contents.add(consentContent);
            saveConsent(consentContent);
            this.consentContents.add(consentContent);
            return consentContent;
        } else {
            return consentfilter;
        }
    }


}

