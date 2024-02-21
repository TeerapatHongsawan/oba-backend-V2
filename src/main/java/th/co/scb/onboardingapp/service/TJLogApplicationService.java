package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.TJLogUIActivity;

@Service
public class TJLogApplicationService {

    @Autowired
    private TJLogProcessor tjLogProcessor;

    public void addTJLog(TJLogUIActivity tjLogUIActivity, ObaAuthentication auth) {
        tjLogProcessor.logUITxn(tjLogUIActivity, auth.getCaseId());
    }
}
