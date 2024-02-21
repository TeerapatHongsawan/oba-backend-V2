package th.co.scb.onboardingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.TJLogUIActivity;
import th.co.scb.onboardingapp.service.TJLogApplicationService;


@RestController
public class TJLogController {

    @Autowired
    private TJLogApplicationService tjLogApplicationService;

    @PostMapping("/api/activity/add")
    public void add(@RequestBody TJLogUIActivity tjLogUIActivity, ObaAuthentication auth) {
        tjLogApplicationService.addTJLog(tjLogUIActivity, auth);
    }
}
