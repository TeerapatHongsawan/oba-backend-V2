package th.co.scb.onboardingapp.service.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.entapi.ref_common.ReferenceDataCommonGeneralApi;
import th.co.scb.entapi.ref_common.model.*;
import th.co.scb.entapi.referencedata_databases_reference_country.ReferenceDataDatabaseCommonApi;
import th.co.scb.entapi.referencedata_databases_reference_country.model.Db_reference_COUNTRY;
import th.co.scb.onboardingapp.helper.ObaHelper;

@Service
public class ReferenceDataApiService {

    @Autowired
    ReferenceDataCommonGeneralApi referenceDataCommonGeneralApi;

    @Autowired
    ReferenceDataDatabaseCommonApi referenceDataDatabaseCommonApi;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EntApiConfig entApiConfig;

    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;

    public BusinessTypes getBusinessTypes(@Nullable Integer pagingOffset, @Nullable Integer pagingLimit) {
        if(isEntApiMode) {
            return referenceDataCommonGeneralApi.getBusinessTypes(pagingOffset, pagingLimit);
        }else{
            ResponseEntity<BusinessTypes> entity =  restTemplate.exchange(
                    entApiConfig.getBasePath() + "/v1/referenceData/common/businessTypes",
                    HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null)),
                    BusinessTypes.class);
            return entity.getBody();
        }

    }

    public Countries getCountries(@Nullable Integer pagingOffset, @Nullable Integer pagingLimit) {
        if(isEntApiMode) {
            return referenceDataCommonGeneralApi.getCountries(pagingOffset, pagingLimit);
        }else{
            ResponseEntity<Countries> entity =  restTemplate.exchange(
                    entApiConfig.getBasePath() + "/v1/referenceData/common/countries",
                    HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null)),
                    Countries.class);
            return entity.getBody();
        }

    }

    public Db_reference_COUNTRY getDataReferenceCOUNTRY(@Nullable Integer pagingLimit, @Nullable Integer pagingOffset) {
        if(isEntApiMode) {
            return referenceDataDatabaseCommonApi.getDataReferenceCOUNTRY(pagingOffset, pagingLimit);
        }else{
            ResponseEntity<Db_reference_COUNTRY> entity =  restTemplate.exchange(
                    entApiConfig.getBasePath() + "/v1/referenceData/databases/reference/COUNTRY?pagingLimit="+pagingLimit+"&pagingOffset="+pagingOffset,
                    HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null)),
                    Db_reference_COUNTRY.class);
            return entity.getBody();
        }
    }

    public CustomerSubTypes getCustomerSubTypes(@Nullable String customerTypeCode, @Nullable Integer pagingOffset, @Nullable Integer pagingLimit) {
        if(isEntApiMode) {
            return referenceDataCommonGeneralApi.getCustomerSubTypes(customerTypeCode, pagingOffset, pagingLimit);
        }else{
            ResponseEntity<CustomerSubTypes> entity =  restTemplate.exchange(
                    entApiConfig.getBasePath() + "/v1/referenceData/common/customerSubTypes",
                    HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null)),
                    CustomerSubTypes.class);
            return entity.getBody();
        }
    }

    public OwnerCodes getOwnerCodes(@Nullable Integer pagingOffset, @Nullable Integer pagingLimit) {
        if(isEntApiMode) {
            return referenceDataCommonGeneralApi.getOwnerCodes(pagingOffset, pagingLimit);
        }else{
            ResponseEntity<OwnerCodes> entity =  restTemplate.exchange(
                    entApiConfig.getBasePath() + "/v1/referenceData/common/ownerCodes",
                    HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null)),
                    OwnerCodes.class);
            return entity.getBody();
        }
    }

    public OccupationSubTypes getOccupationSubtypes(@NotNull String parentOccupationCode, @Nullable Integer pagingOffset, @Nullable Integer pagingLimit) {
        if(isEntApiMode) {
            return referenceDataCommonGeneralApi.getOccupationSubtypes(parentOccupationCode, pagingOffset, pagingLimit);
        }else{
            ResponseEntity<OccupationSubTypes> entity =  restTemplate.exchange(
                    entApiConfig.getBasePath() + "/v1/referenceData/common/occupationSubTypes?parentOccupationCode=" + parentOccupationCode,
                    HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null)),
                    OccupationSubTypes.class);
            return entity.getBody();
        }
    }

    public Occuapations getOccupations(@Nullable Integer pagingOffset, @Nullable Integer pagingLimit) {
        if(isEntApiMode) {
            return referenceDataCommonGeneralApi.getOccupations(pagingOffset, pagingLimit);
        }else{
            ResponseEntity<Occuapations> entity =  restTemplate.exchange(
                    entApiConfig.getBasePath() + "/v1/referenceData/common/occupations",
                    HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null)),
                    Occuapations.class);
            return entity.getBody();
        }
    }

    public Prefixes getIndividualTitles(@Nullable String prefixGender, @Nullable Integer pagingOffset, @Nullable Integer pagingLimit) {
        if(isEntApiMode) {
            return referenceDataCommonGeneralApi.getIndividualTitles(prefixGender, pagingOffset, pagingLimit);
        }else{
            ResponseEntity<Prefixes> entity =  restTemplate.exchange(
                    entApiConfig.getBasePath() + "/v1/referenceData/common/individualTitles",
                    HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(entApiConfig.getApisecret(), entApiConfig.getSourceSystem(), entApiConfig.getApikey(), null,null)),
                    Prefixes.class);
            return entity.getBody();
        }
    }

}

