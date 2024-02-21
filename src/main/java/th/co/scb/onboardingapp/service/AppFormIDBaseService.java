package th.co.scb.onboardingapp.service;

import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.CaseInfo;

@Repository
public interface AppFormIDBaseService {
    String getAppFormNo(CaseInfo caseInfo);
}

