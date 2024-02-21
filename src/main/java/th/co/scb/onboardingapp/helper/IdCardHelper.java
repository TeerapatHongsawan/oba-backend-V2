package th.co.scb.onboardingapp.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.model.CardInfo;
import th.co.scb.onboardingapp.model.CaseInfo;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Slf4j
@Component
public class IdCardHelper {

    public boolean isIdCardExpired(CardInfo cardInfo) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            log.info("Today : {}", LocalDate.now());
            log.info("ExpDate : {}", LocalDate.parse(cardInfo.getExpDate()));

            return LocalDate.parse(cardInfo.getExpDate()).isBefore(LocalDate.now());
//            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
//            Date expireDate = simpleDateFormat.parse(cardInfo.getExpDate());
//            return expireDate.before(today);
        } catch (Exception e) {
            log.error("Invalid Format expireDate(yyyy-MM-dd) : " + cardInfo.getExpDate());
            return true;
        }
    }

    public boolean isCardIssuedDateBeforeVChannelIssuedDate(CaseInfo caseInfo, CardInfo cardInfo) {
        String issueDate = caseInfo.getCustomerInfo().getIssueDate();
        if (issueDate == null || "".equals(issueDate)) {
            return false;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date vchannelIssuedDate = simpleDateFormat.parse(issueDate);
            Date cardIssuedDate = simpleDateFormat.parse(cardInfo.getIssueDate());
            return cardIssuedDate.before(vchannelIssuedDate);
        } catch (Exception e) {
            log.error("Invalid Format cardIssuedDate : " + cardInfo.getIssueDate() + " OR " + "Invalid Format vChannelIssuedDate : " + issueDate);
            return true;
        }
    }
}
