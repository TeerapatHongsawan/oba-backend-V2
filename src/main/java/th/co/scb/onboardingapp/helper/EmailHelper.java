package th.co.scb.onboardingapp.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("!emailHack")
@Service
public class EmailHelper {
    @Autowired
    private ApplicationContext applicationContext;

    @Value("${mail.branch.domain}")
    private String domain;

    @Value("${hack.branch.email}")
    private String hackEmail;

    public String getBranchEmail(String branchCode) {
        String branchMail = "";
        if ("sit".equalsIgnoreCase(applicationContext.getEnvironment().getActiveProfiles()[0]) || "dev".equalsIgnoreCase(applicationContext.getEnvironment().getActiveProfiles()[0]) || "uat".equalsIgnoreCase(applicationContext.getEnvironment().getActiveProfiles()[0])){
            return hackEmail;
        }else {
            if (null != branchCode && !"".equals(branchCode)) {
                String firstLetter = branchCode.substring(0, 1);
                if ("0".equals(firstLetter) && 4 == branchCode.length()) {
                    branchMail = branchCode.substring(1);
                } else {
                    branchMail = branchCode;
                }
                return String.format("mail%s@%s", branchMail, domain);
            } else {
                return "";
            }
        }
    }
}

@Profile("emailHack")
@Service
class EmailHelperHack extends EmailHelper {

    @Value("${hack.branch.email}")
    private String branchEmail;

    @Override
    public String getBranchEmail(String branchCode) {
        return branchEmail;
    }
}

