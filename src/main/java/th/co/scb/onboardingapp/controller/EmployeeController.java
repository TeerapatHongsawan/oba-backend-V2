package th.co.scb.onboardingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.EmployeeInfo;
import th.co.scb.onboardingapp.model.EmployeeMe;
import th.co.scb.onboardingapp.model.ValidateRequest;
import th.co.scb.onboardingapp.service.EmployeeApplicationService;


import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeApplicationService employeeApplicationService;

    @GetMapping("/api/employee")
    public List<EmployeeInfo> browse(ObaAuthentication authentication) {
        return employeeApplicationService.getEmployeesInfo(authentication);
    }

    @GetMapping("/api/employee/me")
    public EmployeeMe getMe(ObaAuthentication authentication) {
        return employeeApplicationService.getEmployeeMe(authentication);
    }

//    @GetMapping("/api/employee/license/{type}/{number}")
//    public EmployeeInfo getLicense(@PathVariable String type, @PathVariable String number) {
//        return employeeApplicationService.getEmployeeLicense(type, number);
//    }
//
//    @PostMapping("/api/employee/validate")
//    public void validate(@RequestBody ValidateRequest validateRequest) {
//        employeeApplicationService.validateEmployee(validateRequest);
//    }
}

