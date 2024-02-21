package th.co.scb.onboardingapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.scb.onboardingapp.model.MasterJointType;
import th.co.scb.onboardingapp.service.CustommerJointInfoService;

import java.util.List;


@RestController
public class CustommerJointInfoController {


    @Autowired
    CustommerJointInfoService custommerJointInfoService;

    @GetMapping("/api/jointInfo/jointType")
    public List<MasterJointType> jointType() {
        return  custommerJointInfoService.JointInfo();
    }
}
