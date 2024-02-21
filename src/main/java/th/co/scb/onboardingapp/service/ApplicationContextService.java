package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContextService {

    @Autowired
    private ApplicationContext applicationContext;

    public boolean isNonProduction() {
        return !applicationContext.getEnvironment().getActiveProfiles()[0].contains("prod");
    }
}
