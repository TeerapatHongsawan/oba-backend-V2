package th.co.scb.onboardingapp.service.common;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.helper.ForeignerHelper;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.entity.OtpEntity;
import th.co.scb.onboardingapp.repository.OtpJpaRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class OtpDateService {

    @Autowired
    private OtpJpaRepository otpRepository;

    public String getFormattedOtpDate(String caseId, String otpPhoneNumber) {
        LocalDateTime otpDateTime = getOtpDateTime(caseId, otpPhoneNumber);
        if (otpDateTime == null) {
            return "";
        }
        return DateTimeFormatter.ofPattern("dd/MM/yy เวลา HH:mm:ss น.").format(otpDateTime);
    }

    public String getFormattedOtpDateForENTemplate(String caseId, String otpPhoneNumber, CaseInfo caseInfo) {
        LocalDateTime otpDateTime = getOtpDateTime(caseId, otpPhoneNumber);
        if (otpDateTime == null) {
            return "";
        }
        String dateFormat = DateTimeFormatter.ofPattern("dd/MM/yy เวลา HH:mm:ss น.").format(otpDateTime);

        return ForeignerHelper.isForeigner(caseInfo) ? dateFormat.replace("เวลา", "Time").replace("น.", "") : dateFormat;
    }

    public String getFormattedIsoOtpDate(String caseId, String otpPhoneNumber) {
        LocalDateTime otpDateTime = getOtpDateTime(caseId, otpPhoneNumber);
        if (otpDateTime == null) {
            return "";
        }
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(otpDateTime);
    }

    public LocalDateTime getOtpDateTime(String caseId, String otpPhoneNumber) {
        OtpEntity otpInfo = null;

        try {
            if (otpPhoneNumber != null) {
                otpInfo = otpRepository.findById(caseId).orElse(null);
                if (otpInfo != null) {
                    if (otpInfo.getCreatedDate() != null) {
                        return otpInfo.getCreatedDate().toLocalDateTime();
                    }
                } else {
                    log.info("AppForm : OTP NOT FOUND - {}", caseId);
                }
            }
        } catch (Exception ex) {
            log.error("AppForm Error : {} ", ex.getMessage(), ex);
        }

        return null;
    }

    public String getMobileNumber(String caseId) {
        OtpEntity otpInfo = null;

        try {
            otpInfo = otpRepository.findById(caseId).orElse(null);
            if (otpInfo != null) {
                return StringUtils.defaultString(otpInfo.getMobileNo());
            } else {
                log.info("AppForm : OTP MOBILE NUMBER NOT FOUND - {}", caseId);
            }
        } catch (Exception ex) {
            log.error("AppForm Error : {} ", ex.getMessage(), ex);
        }

        return "";
    }
}