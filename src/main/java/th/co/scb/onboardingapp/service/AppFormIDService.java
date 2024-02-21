package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.CustomerInfo;
import th.co.scb.onboardingapp.model.Investment;
import th.co.scb.onboardingapp.model.ProductInfo;
import th.co.scb.onboardingapp.repository.AppformIdJpaRepository;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Service
public class AppFormIDService implements AppFormIDBaseService {
    @Autowired
    AppformIdJpaRepository appformIDRepository;

    @Override
    public String getAppFormNo(CaseInfo caseInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date today = Calendar.getInstance().getTime();
        String formattedDate = sdf.format(today);

        return "OBIN" + caseInfo.getBranchId() + formattedDate + getRunningNo(caseInfo, caseInfo.getBranchId());
    }

    private String getRunningNo(CaseInfo caseInfo, String branchId) {
        Boolean mutualFund = false;
        Boolean security = false;
        String prefix = "";
        String running;

        List<ProductInfo> productInfo = caseInfo.getProductInfo();
        Boolean deposit = isNotEmpty(productInfo);

        CustomerInfo customerInfo = caseInfo.getCustomerInfo();
        if (customerInfo.getInvestment() != null) {
            Investment investment = customerInfo.getInvestment();
            mutualFund = investment.getMutualFund() != null || investment.getOmnibus() != null;
            security = investment.getSecurities() != null;
        }

        if (deposit && mutualFund && security) prefix = "08";
        else if (deposit && mutualFund) prefix = "03";
        else if (deposit && security) prefix = "07";
        else if (mutualFund && security) prefix = "09";
        else if (deposit) prefix = "01";
        else if (mutualFund) prefix = "02";
        else if (security) prefix = "10";
        else prefix = "S1";

        running = "S1".equalsIgnoreCase(prefix) ? appformIDRepository.nextServiceAppFormID(branchId) :
                appformIDRepository.nextProductAppFormID(branchId);
        running = String.format("%04d", Integer.parseInt(running));

        return prefix + running;
    }
}
