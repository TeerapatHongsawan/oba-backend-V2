package th.co.scb.onboardingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import th.co.scb.entapi.alerts.model.FeeResponse;
import th.co.scb.entapi.ref_common.model.BusinessType;
import th.co.scb.entapi.ref_common.model.Country;
import th.co.scb.entapi.ref_common.model.CustomerSubType;
import th.co.scb.entapi.ref_common.model.Occupation;
import th.co.scb.entapi.ref_common.model.OccupationSubtype;
import th.co.scb.entapi.ref_common.model.OwnerCode;
import th.co.scb.entapi.ref_common.model.Prefix;
import th.co.scb.entapi.ref_postals.model.ThaiPostal;
import th.co.scb.entapi.referencedata_databases_reference_country.model.Item_reference_COUNTRY;
import th.co.scb.onboardingapp.exception.ApplicationException;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.service.JmxFlags;
import th.co.scb.onboardingapp.service.MasterDataService;
import th.co.scb.onboardingapp.service.ToggleService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MasterDataController {

    @Autowired
    MasterDataService masterDataService;

    @Autowired
    ToggleService toggleService;

    @Autowired(required = false)
    private JmxFlags featureFlags;

    @GetMapping("/api/masterdata/business-types")
    public List<BusinessType> businessTypes() {
        return masterDataService.businessTypes().stream().filter(it -> "1".equals(it.getStatus())).collect(Collectors.toList());
    }

    @GetMapping("/api/masterdata/countries")
    public List<Country> countries() {
        return masterDataService.countries().stream().filter(it -> "1".equals(it.getStatus())).collect(Collectors.toList());
    }

    @PostMapping("/api/masterdata/country")
    public List<Item_reference_COUNTRY> country() {
        return masterDataService.country();
    }

    @PostMapping("/api/masterdata/currencies")
    public List<Currencies> currencies() {
        return masterDataService.currencies();
    }

    @PostMapping("/api/masterdata/country-crs")
    public List<Item_reference_COUNTRY> countryCrs() {
        return masterDataService.countryCrs();
    }

    @GetMapping("/api/masterdata/customer-types")
    public List<CustomerSubType> customerTypes() {
        return masterDataService.customerTypes().stream().filter(it -> "1".equals(it.getStatus())).collect(Collectors.toList());
    }

    @GetMapping("/api/masterdata/config/toggles")
    public List<ToggleConfig> toggles() {
        if (featureFlags != null && featureFlags.has("disableToggle")) {
            throw new ApplicationException("Config Toggles Disabled");
        }
        return toggleService.toggles();
    }

    @GetMapping("/api/masterdata/sms-alert-fees/{prodtype}")
    public List<FeeResponse> smsAlertFees(@PathVariable String prodtype) {
        return masterDataService.smsAlertFees(prodtype);
    }

    @GetMapping("/api/masterdata/occodes")
    public List<OwnerCode> occodes() {
        return masterDataService.occodes().stream().filter(it -> "1".equals(it.getStatus())).collect(Collectors.toList());
    }

    @GetMapping("/api/masterdata/occupation-subtypes")
    public List<OccupationSubtype> occupationSubtypes() {
        return masterDataService.occupationSubtypes().stream().filter(it -> "1".equals(it.getStatus())).collect(Collectors.toList());
    }

    @GetMapping("/api/masterdata/occupation-types")
    public List<Occupation> occupationTypes() {
        return masterDataService.occupationTypes().stream().filter(it -> "1".equals(it.getStatus())).collect(Collectors.toList());
    }

    @GetMapping("/api/masterdata/postals")
    public List<ThaiPostal> postals() {
        return masterDataService.postals();
    }

    @GetMapping("/api/masterdata/titles")
    public List<Prefix> titles() {
        return masterDataService.titles().stream().filter(it -> "1".equals(it.getStatus())).collect(Collectors.toList());
    }

    @GetMapping("/api/masterdata/relations")
    public List<BeneficiaryRelation> relations() {
        return masterDataService.relations();
    }


    @GetMapping("/api/masterdata/occupations")
    public ResponseEntity<?> occupation(){
        try {
            List<Occupation> occupations = masterDataService.occupationTypes().stream()
                    .filter(it -> "1".equals(it.getStatus()))
                    .collect(Collectors.toList());

            return PayloadResponseT.buildResponse(occupations);
        } catch (HttpClientErrorException e) {
            return PayloadResponseErrorT.buildResponseError(e.getResponseBodyAsString());
        }
    }

    @GetMapping("/v2/api/masterdata/titles")
    public ResponseEntity<?> titlesV2() {
        try {
            return PayloadResponseT.buildResponse( masterDataService.titles().stream().filter(it -> "1".equals(it.getStatus())).collect(Collectors.toList()));
        } catch (HttpClientErrorException e) {
            return PayloadResponseErrorT.buildResponseError(e.getResponseBodyAsString());
        }

    }

    @GetMapping("/v2/api/masterdata/customer-types")
    public ResponseEntity<?> customerTypesV2() {
        try {
            return PayloadResponseT.buildResponse(masterDataService.customerTypes().stream().filter(it -> "1".equals(it.getStatus())).collect(Collectors.toList()));
        } catch (HttpClientErrorException e) {
            return PayloadResponseErrorT.buildResponseError(e.getResponseBodyAsString());
        }
    }

    @GetMapping("/v2/api/masterdata/country")
    public ResponseEntity<?> countryV2() {
        try {
            return PayloadResponseT.buildResponse(masterDataService.country());
        } catch (HttpClientErrorException e) {
            return PayloadResponseErrorT.buildResponseError(e.getResponseBodyAsString());
        }

    }

    @GetMapping("/v2/api/masterdata/postals")
    public ResponseEntity<?> postalsV2() {
        try {
            return PayloadResponseT.buildResponse(masterDataService.postals());
        } catch (HttpClientErrorException e) {
            return PayloadResponseErrorT.buildResponseError(e.getResponseBodyAsString());
        }
    }


}
