package th.co.scb.onboardingapp.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.service.CaseApplicationService;

import java.util.List;

@RestController
public class CaseController {

    @Autowired
    CaseApplicationService caseApplicationService;

    @PostMapping("/api/case")
    public CaseInfo create(@RequestBody CaseRequest caseRequest, ObaAuthentication authentication, HttpServletResponse httpServletResponse) {
        return caseApplicationService.createCase(caseRequest, authentication, httpServletResponse);
    }

    @PostMapping("/api/case/continue")
    public CaseInfo caseContinue(ObaAuthentication authentication) {
        return caseApplicationService.continueCase(authentication);
    }

    /**
     * Save case info
     */
    @PutMapping("/api/case")
    public void save(@RequestBody CaseInfo caseInfo) {
        caseApplicationService.saveCase(caseInfo);
    }


    /**
     * Approve monitor code & dopa
     */
    @PostMapping("/api/case/approve")
    public CaseInfo approve(@RequestBody ApproveRequest request, ObaAuthentication authentication) {
        return caseApplicationService.approveCase(request, authentication);
    }


    /**
     * Get case for remote approval
     */
    @GetMapping("/api/case/{id}")
    public CaseInfo load(@PathVariable String id, ObaAuthentication authentication, HttpServletResponse httpServletResponse) {
        return caseApplicationService.loadCase(id, authentication, httpServletResponse);
    }


    /**
     * Close case
     */
    @DeleteMapping("/api/case")
    public void close(ObaAuthentication authentication) {
        caseApplicationService.closeCase(authentication);
    }

    /**
     * Fetch current caseInfo
     */
    @GetMapping("/api/case/refresh")
    public CaseInfo refresh(ObaAuthentication authentication) {
        return caseApplicationService.refreshCase(authentication);
    }

    @PostMapping("/api/case/approved")
    public List<CaseItem> getApprovedList(ObaAuthentication authentication) {
        return caseApplicationService.getCaseApprovedList(authentication);
    }

    @PostMapping("/api/case/validate")
    public void validate(ObaAuthentication authentication) {
        caseApplicationService.validateCase(authentication);
    }

    @GetMapping("/api/case/stampfulfilment")
    public void stampfulfilment(ObaAuthentication authentication) {
        caseApplicationService.caseStampCaseFulfilmentDate(authentication);
    }

}
