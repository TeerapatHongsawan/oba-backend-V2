package th.co.scb.onboardingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.scb.onboardingapp.model.GeneralParam;
import th.co.scb.onboardingapp.service.GeneralParamService;

@RestController
public class GeneralParamsController {

    @Autowired
    private GeneralParamService generalParamService;

    @GetMapping("/api/param/general-params/frontend")
    public GeneralParam getFrontendGeneralParams() {
        return generalParamService.getFrontEndGeneralParam();
    }

//    @GetMapping("/api/param/general-params/convertepass")
//    public GeneralParam getConvertEpassFlagGeneralParams() { return generalParamService.getConvertEpassbookFlag(); }

    @GetMapping("/api/param/general-params/allow-foreigner-roles")
    public GeneralParam getAllowForeignerRoles() { return generalParamService.getAllowForeignerRoles(); }

    @GetMapping("/api/param/general-params/foreigner-allow-products")
    public GeneralParam getForeignerAllowProducts() { return generalParamService.getForeignerAllowProducts(); }

    @GetMapping("/api/param/general-params/popup-redlist-email")
    public GeneralParam getPopupEmvRedlistEmailGeneralParams() {
        return generalParamService.getPopupEmvRedlistEmail();

    }
}
