package th.co.scb.onboardingapp.service;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import th.co.scb.entapi.alerts.ServicingAlertsRegistrationApi;
import th.co.scb.entapi.alerts.model.FeeResponse;
import th.co.scb.entapi.ref_common.ReferenceDataCommonGeneralApi;
import th.co.scb.entapi.ref_common.model.BusinessType;
import th.co.scb.entapi.ref_common.model.BusinessTypes;
import th.co.scb.entapi.ref_common.model.Countries;
import th.co.scb.entapi.ref_common.model.Country;
import th.co.scb.entapi.ref_common.model.CustomerSubType;
import th.co.scb.entapi.ref_common.model.CustomerSubTypes;
import th.co.scb.entapi.ref_common.model.Occuapations;
import th.co.scb.entapi.ref_common.model.Occupation;
import th.co.scb.entapi.ref_common.model.OccupationSubTypes;
import th.co.scb.entapi.ref_common.model.OccupationSubtype;
import th.co.scb.entapi.ref_common.model.OwnerCode;
import th.co.scb.entapi.ref_common.model.OwnerCodes;
import th.co.scb.entapi.ref_common.model.Prefix;
import th.co.scb.entapi.ref_common.model.Prefixes;
import th.co.scb.entapi.ref_postals.ReferenceDataThaiPostalsApi;
import th.co.scb.entapi.ref_postals.model.ThaiPostal;
import th.co.scb.entapi.ref_postals.model.ThaiPostals;
import th.co.scb.entapi.referencedata_databases_reference_country.ReferenceDataDatabaseCommonApi;
import th.co.scb.entapi.referencedata_databases_reference_country.model.Db_reference_COUNTRY;
import th.co.scb.entapi.referencedata_databases_reference_country.model.Item_reference_COUNTRY;
import th.co.scb.entapi.referencedata_databases_reference_country.model.PagingResponse;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.BeneficiaryRelation;
import th.co.scb.onboardingapp.model.Currencies;
import th.co.scb.onboardingapp.model.entity.CurrencyProdMappingEntity;
import th.co.scb.onboardingapp.model.entity.MasterBeneficiaryRelationCodeEntity;
import th.co.scb.onboardingapp.repository.CurrencyProdMappingJpaRepository;
import th.co.scb.onboardingapp.repository.MasterBeneficiaryRelationCodeJpaRepository;
import th.co.scb.onboardingapp.service.api.ReferenceDataApiService;
import th.co.scb.onboardingapp.service.api.ReferenceDataThaiPostalsApiService;
import th.co.scb.onboardingapp.service.api.ServicingAlertsRegistrationApiService;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class MasterDataService {

    private Collator collator = Collator.getInstance(new Locale("th", "TH"));

    @Autowired
    ReferenceDataCommonGeneralApi referenceDataCommonGeneralApi;

    @Autowired
    ReferenceDataDatabaseCommonApi referenceDataDatabaseCommonApi;

    @Autowired
    CurrencyProdMappingJpaRepository currencyProdMappingJpaRepository;

    @Autowired
    ServicingAlertsRegistrationApi servicingAlertsRegistrationApi;

//    @Autowired
//    ReferenceDataThaiPostalsApi referenceDataThaiPostalsApi;

    @Autowired
    ReferenceDataThaiPostalsApiService referenceDataThaiPostalsApiService;

    @Autowired
    MasterBeneficiaryRelationCodeJpaRepository beneficiaryRelationCodeRepository;

    @Autowired
    MappingHelper mappingHelper;

    @Autowired
    ReferenceDataApiService referenceDataApiService;

    @Autowired
    ServicingAlertsRegistrationApiService servicingAlertsRegistrationApiService;

    @Cacheable(value = "countries", unless = "#result == null")
    public List<Country> countries() {
        Countries countries = referenceDataApiService.getCountries(null, null);
        return Arrays.stream(countries.getItems())
                .sorted(Comparator.comparing(Country::getCountryDescTh, collator))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "occupationTypes", unless = "#result == null")
    public List<Occupation> occupationTypes() {
        Occuapations occupations = referenceDataApiService.getOccupations(null, null);

        Comparator<Occupation> comparingPriorityList =
                Comparator.comparingInt(it -> StringUtils.isBlank(it.getOccupationPriorityList()) ? 0 : Integer.valueOf(it.getOccupationPriorityList()));

        return Arrays.stream(occupations.getItems())
                .filter(it -> StringUtils.isNotBlank(it.getOccupationParentCode()) || StringUtils.isNotBlank(it.getOccupationPriorityList()))
                .map(it -> {
                    if (StringUtils.isNotBlank(it.getOccupationPriorityList())) {
                        it.setOccupationParentCode("");
                    }
                    return it;
                })
                .sorted(comparingPriorityList.thenComparing(Occupation::getOccupationDisplayOrder))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "businessTypes", unless = "#result == null")
    public List<BusinessType> businessTypes() {
        BusinessTypes businessTypes = referenceDataApiService.getBusinessTypes(null, null);
        return Arrays.stream(businessTypes.getItems())
                .sorted(Comparator.comparing(BusinessType::getBusinessCodeOrder))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "country", unless = "#result == null")
    public List<Item_reference_COUNTRY> country() {
        List<Item_reference_COUNTRY> items = new ArrayList<>();

        Db_reference_COUNTRY response = referenceDataApiService.getDataReferenceCOUNTRY(25, 1);

        while (response != null && response.getItems() != null) {
            items.addAll(Arrays.asList(response.getItems()));
            if (response.getPagination() == null || response.getPagination().getNextPage() == null) {
                response = null;
            } else {
                PagingResponse pagingResponse = response.getPagination();
                response = referenceDataApiService.getDataReferenceCOUNTRY(25, Integer.parseInt(Objects.requireNonNull(pagingResponse.getNextPage().getPagingOffset())));
            }
        }
        List<String> excludeCountryCode = Arrays.asList("IR", "KP");

        return items.stream()
                .filter(it -> !excludeCountryCode.contains(it.getCOUNTRY_CODE()))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "currencies", unless = "#result == null")
    public List<Currencies> currencies() {
        List<CurrencyProdMappingEntity> currencyProdMappingEntityList =
                currencyProdMappingJpaRepository.findByCurrencyConfigAndCurrencyProdMappingActiveY();
        List<Currencies> currenciesList = new ArrayList<>();
        for (CurrencyProdMappingEntity currencyProdMapping : currencyProdMappingEntityList) {
            Currencies currencies = new Currencies();
            currencies.setIndi_Prod_Code(currencyProdMapping.getIndiProdCode());
            currencies.setCurrencyCode(currencyProdMapping.getCurrencyConfigEntity().getCurrencyCode());
            currencies.setCurrencyDesc(currencyProdMapping.getCurrencyConfigEntity().getCurrencyDesc());
            currencies.setCurr_Name_En(currencyProdMapping.getCurrencyConfigEntity().getCurrNameEn());
            currencies.setCurr_Name_Th(currencyProdMapping.getCurrencyConfigEntity().getCurrNameTh());
            currencies.setResident_Type(currencyProdMapping.getResidentType());
            currenciesList.add(currencies);
        }
        currenciesList = currenciesList.stream().sorted(Comparator.comparing(Currencies::getIndi_Prod_Code)
                .thenComparing(Currencies::getCurrencyCode)).collect(Collectors.toList());

        return currenciesList;
    }

    @Cacheable(value = "countryCrs", unless = "#result == null")
    public List<Item_reference_COUNTRY> countryCrs() {
        List<Item_reference_COUNTRY> items = new ArrayList<>();
        Db_reference_COUNTRY response = referenceDataApiService.getDataReferenceCOUNTRY(25, 1);
        while (response != null && response.getItems() != null) {
            items.addAll(Arrays.asList(response.getItems()));
            if (response.getPagination() == null || response.getPagination().getNextPage() == null) {
                response = null;
            } else {
                PagingResponse pagingResponse = response.getPagination();
                response = referenceDataApiService.getDataReferenceCOUNTRY(25, Integer.parseInt(Objects.requireNonNull(pagingResponse.getNextPage().getPagingOffset())));
            }
        }
        return items.stream()
                .filter(it -> "1".equals(it.getSTATUS()) && "1".equals(it.getCOUNTRY_CRS_FLAG()))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "customerTypes", unless = "#result == null")
    public List<CustomerSubType> customerTypes() {
        CustomerSubTypes customerSubTypes = referenceDataApiService.getCustomerSubTypes(null, null, null);
        return Arrays.stream(customerSubTypes.getItems()).collect(Collectors.toList());
    }

    @Cacheable(value = "smsAlertFees", unless = "#result == null")
    public List<FeeResponse> smsAlertFees(String prodType) {
        FeeResponse[] feeTypes = servicingAlertsRegistrationApiService.getFeeProfileByProductType(prodType, null);
        return Arrays.stream(feeTypes).collect(Collectors.toList());
    }

    @Cacheable(value = "occodes", unless = "#result == null")
    public List<OwnerCode> occodes() {
        OwnerCodes ownerCodes = referenceDataApiService.getOwnerCodes(null, null);
        return Arrays.stream(ownerCodes.getItems()).collect(Collectors.toList());
    }

    @Cacheable(value = "occupationSubTypes", unless = "#result == null")
    public List<OccupationSubtype> occupationSubtypes() {
        OccupationSubTypes occupationSubtypes = referenceDataApiService.getOccupationSubtypes("93", null, null);
        return Arrays.stream(occupationSubtypes.getItems()).collect(Collectors.toList());
    }

    @Cacheable(value = "postals", unless = "#result == null")
    public List<ThaiPostal> postals() {
        ThaiPostals thaiPostals = referenceDataThaiPostalsApiService.getThaiPostals(null, null);
        return Arrays.stream(thaiPostals.getItems()).collect(Collectors.toList());
    }

    @Cacheable(value = "titles", unless = "#result == null")
    public List<Prefix> titles() {
        Prefixes prefixes = referenceDataApiService.getIndividualTitles(null, null, null);
        return Arrays.stream(prefixes.getItems()).collect(Collectors.toList());
    }

    @Cacheable(value = "relations", unless = "#result == null")
    public List<BeneficiaryRelation> relations() {
        List<MasterBeneficiaryRelationCodeEntity> list = beneficiaryRelationCodeRepository.findAll();
        return mappingHelper.mapAsList(list, BeneficiaryRelation.class);
    }

}
