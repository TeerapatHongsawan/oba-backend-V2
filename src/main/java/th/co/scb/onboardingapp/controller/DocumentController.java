package th.co.scb.onboardingapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ApplicationException;
import th.co.scb.onboardingapp.model.CaptureResponse;
import th.co.scb.onboardingapp.model.UploadRequest;
import th.co.scb.onboardingapp.service.DocumentApplicationService;
import th.co.scb.onboardingapp.service.JmxFlags;


import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class DocumentController {

    @Autowired(required = false)
    private JmxFlags featureFlags;

    @Autowired
    private DocumentApplicationService documentApplicationService;

    @PostMapping("/api/document/upload")
    public CaptureResponse upload(@RequestBody UploadRequest data, ObaAuthentication auth) {
        return documentApplicationService.uploadDocument(data, auth);
    }

}

