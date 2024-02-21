package th.co.scb.onboardingapp.service;

import jakarta.persistence.LockModeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import th.co.scb.entapi.custinfo.model.CustInfo;
import th.co.scb.entapi.custinfo.model.Custinfos;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.NotFoundException;
import th.co.scb.onboardingapp.helper.CaseHelper;
import th.co.scb.onboardingapp.helper.DateHelper;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.mapper.CaseInfoMapper;
import th.co.scb.onboardingapp.mapper.CaseMapper;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.entity.*;
import th.co.scb.onboardingapp.model.enums.CaseStatus;
import th.co.scb.onboardingapp.repository.CaseJpaRepository;
import th.co.scb.onboardingapp.service.api.CustProfileApiService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class CaseService {

    public static final String LANG_TH = "TH";
    public static final String LANG_EN = "EN";

    @Autowired
    CaseJpaRepository caseRepository;

    @Autowired
    MappingHelper mappingHelper;

    //@Autowired
    //CustProfileCustinfoApi custProfileCustinfoApi;

    @Autowired
    CustProfileApiService custProfileApiService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    AccessMappingService accessMappingService;

    public void createCase(CaseInfo caseInfo) {
        log.info("Create Case");
        //TODO: change to use  CaseMapper instead
        //CaseEntity caseEntity = mappingHelper.map(caseInfo, CaseEntity.class);
        CaseEntity caseEntity = CaseMapper.INSTANCE.map(caseInfo);
        caseRepository.save(caseEntity);
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public CaseInfo updateCase(String caseId, Consumer<CaseInfo> action) {
        log.info("start updateCase: {}", caseId);
        CaseEntity caseEntity = caseRepository.findByCaseId(caseId)
                .orElseThrow(() -> new NotFoundException("Case"));

        CaseInfo caseInfo = CaseMapper.INSTANCE.convertToCaseInfo(caseEntity);
        action.accept(caseInfo);
        CaseEntity caseEntityToSave = CaseMapper.INSTANCE.map(caseInfo);
        caseEntityToSave.setId(caseEntity.getId());
        caseEntityToSave.setSavedDate(LocalDateTime.now());

        caseRepository.save(caseEntityToSave);
        log.info("end updateCase: {}", caseId);
        return caseInfo;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public CaseInfo updateCaseWithOutUpdateDate(String caseId, Consumer<CaseInfo> action) {
        log.info("start updateCase: {}", caseId);
        CaseEntity caseEntity = caseRepository.findByCaseId(caseId)
                .orElseThrow(() -> new NotFoundException("Case"));

        CaseInfo caseInfo = mappingHelper.map(caseEntity, CaseInfo.class);
        action.accept(caseInfo);

        CaseEntity caseEntityToSave = mappingHelper.copy(caseInfo, caseEntity);

        caseRepository.save(caseEntityToSave);
        log.info("end updateCase: {}", caseId);
        return caseInfo;
    }

    public CaseInfo getCase(String caseId) {
        CaseEntity caseEntity = caseRepository.findByCaseId(caseId)
                .orElseThrow(() -> new NotFoundException("Case"));

        //TODO: change to use  CaseInfoMapper instead
        //return mappingHelper.map(caseEntity, CaseInfo.class);
        return CaseInfoMapper.INSTANCE.map(caseEntity);
    }

    public CaseInfo buildCase(CardInfo cardInfo, IdentificationsItem identificationsItem, ObaAuthentication authentication, String caseId) {

        log.info("Build Case DocNo : {}, caseId: {}", cardInfo.getDocNo(), caseId);

        Custinfos customerInfosFromApi = new Custinfos();
        if (CaseHelper.isForeigner(cardInfo.getDocType()) && !StringUtils.isEmpty(cardInfo.getOldDocNo())) {
            customerInfosFromApi = custProfileApiService.listCustinfoByID(cardInfo.getOldDocNo(), cardInfo.getDocType());
        } else {
            customerInfosFromApi = custProfileApiService.listCustinfoByID(cardInfo.getDocNo(), cardInfo.getDocType());
        }

        CustInfo[] customerInfoItems = customerInfosFromApi.getItems();
        CustInfo apiCustomerInfo = (customerInfoItems != null && customerInfoItems.length > 0) ? customerInfoItems[0] : null;

        CaseInfo caseInfo = CaseInfo.builder()
                .caseId(caseId)
                .employeeId(authentication.getName())
                .referenceId(apiCustomerInfo != null ? apiCustomerInfo.getPartnerID() : null)
                .branchId(authentication.getBranchId())
                .caseTypeID(authentication.getAppName())
                .createdDate(LocalDateTime.now())
                .savedDate(LocalDateTime.now())
                .caseStatus(CaseStatus.OPEN.getValue())
                .customerInfo(new CustomerInfo())
                .productInfo(new ArrayList<>())
                .paymentInfo(new ArrayList<>())
                .documentInfo(new ArrayList<>())
                .approvalInfo(new ArrayList<>())
                .additionalInfo(new AdditionalCaseInfo())
                .portfolioInfo(new PortfolioInfo())
                .build();

        CustomerInfo customerInfo = caseInfo.getCustomerInfo();
        customerInfo.setCaseId(caseId);
        customerInfo.setIsExistingCustomer(apiCustomerInfo != null ? "Y" : "N");
        customerInfo.setIdentifications(new ArrayList<>());
        customerInfo.setCardInfo(mappingHelper.map(cardInfo, CardInfo.class));
        customerInfo.getCardInfo().setPhoto(null);

        if (apiCustomerInfo != null) {
            IdentificationsItem identificationItem = new IdentificationsItem();
            identificationItem.setIdenType("vip");

            if (isEmpty(apiCustomerInfo.getVipCode())) {
                log.info("Customer is not VIP");
                identificationItem.setIdenStatus("pass");
            } else {
                identificationItem.setIdenStatus("fail");
                log.info("Customer is VIP");
                ApprovalInfo approvalInfo = new ApprovalInfo();
                approvalInfo.setFunctionCode("VIP");
                approvalInfo.setApprovalStatus("P");
                caseInfo.getApprovalInfo().add(approvalInfo);
            }
            customerInfo.getIdentifications().add(identificationItem);
        }

        if (identificationsItem != null && "consent".equalsIgnoreCase(identificationsItem.getIdenType())) {
            IdentificationsItem consentIdentificationItem = new IdentificationsItem();
            consentIdentificationItem.setIdenType("consent");
            consentIdentificationItem.setIdenDetail(new IdenDetail());
            consentIdentificationItem.getIdenDetail().setData(new ArrayList<>());

            ConsentData privacyConsentData = identificationsItem.getIdenDetail().getData().stream()
                    .filter(it -> "004".equalsIgnoreCase(it.getConsentType()))
                    .findAny().orElse(null);


            if (privacyConsentData != null) {
                Display privacyConsentDisplay = new Display();
                privacyConsentDisplay.setVisible(true);
                privacyConsentDisplay.setValue("Y");

                Scopes privacyConsentScopes = new Scopes();
                privacyConsentScopes.setKey("privacy_notice");
                privacyConsentScopes.setDisplay(privacyConsentDisplay);

                ConsentData pricacyConsentData = new ConsentData();
                pricacyConsentData.setScopes(new ArrayList<>());
                pricacyConsentData.getScopes().add(privacyConsentScopes);
                pricacyConsentData.setConsentType(privacyConsentData.getConsentType());
                pricacyConsentData.setVendor(privacyConsentData.getVendor());
                pricacyConsentData.setConsentUpdateDate(privacyConsentData.getConsentUpdateDate());

                consentIdentificationItem.getIdenDetail().getData().add(pricacyConsentData);
            }

            ConsentData dopaConsentData = identificationsItem.getIdenDetail().getData().stream()
                    .filter(it -> "006".equalsIgnoreCase(it.getConsentType()))
                    .findAny().orElse(null);

            if (dopaConsentData != null) {
                Display dopaConsentDisplay = new Display();
                dopaConsentDisplay.setVisible(true);
                dopaConsentDisplay.setValue("Y");

                Scopes dopaConsentScopes = new Scopes();
                dopaConsentScopes.setKey("Dopa consent");
                dopaConsentScopes.setDisplay(dopaConsentDisplay);

                ConsentData consentData = new ConsentData();
                consentData.setScopes(new ArrayList<>());
                consentData.getScopes().add(dopaConsentScopes);
                consentData.setConsentType(dopaConsentData.getConsentType());
                consentData.setVendor(dopaConsentData.getVendor());
                consentData.setConsentUpdateDate(dopaConsentData.getConsentUpdateDate());
                consentData.setConsentVersion(dopaConsentData.getConsentVersion());


                consentIdentificationItem.getIdenDetail().getData().add(consentData);
            }

            customerInfo.getIdentifications().add(consentIdentificationItem);

        }

        AdditionalCaseInfo additionalInfo = caseInfo.getAdditionalInfo();
        EmployeeEntity employeeEntity = employeeService.getEmployee(authentication.getName()).orElse(null);
        if (employeeEntity != null) {
            EmployeeProfile employeeProfile = new EmployeeProfile();
            employeeProfile.setEmpId(employeeEntity.getEmployeeId());
            employeeProfile.setEmpFirstNameEN(employeeEntity.getFirstNameEn());
            employeeProfile.setEmpLastNameEN(employeeEntity.getLastNameEn());
            employeeProfile.setEmpFirstNameTH(employeeEntity.getFirstNameThai());
            employeeProfile.setEmpLastNameTH(employeeEntity.getLastNameThai());
            employeeProfile.setPositionName(employeeEntity.getPositionName());
            employeeProfile.setScbEmail(employeeEntity.getEmail());
            employeeProfile.setSamEmail(employeeEntity.getSamEmail());
            String samEmployeeId = employeeEntity.getSamEmployeeId();
            employeeProfile.setSamId(isNotBlank(samEmployeeId) ? samEmployeeId.replaceAll("^s", "") : samEmployeeId);
            additionalInfo.setEmpProfile(employeeProfile);

            employeeProfile.setIsPersonalBanker(!authentication.getRoles().contains("branch"));

            Map<String, AccessMappingTypeEntity> userTypeMap = accessMappingService.getUserTypeMap();
            authentication.getRoles().stream().flatMap(
                            n -> userTypeMap.keySet().stream().filter(n::equals))
                    .findFirst().ifPresent(employeeProfile::setType);
        }

        BranchEntity branchEntity = employeeService.getBranch(authentication.getBranchId()).orElse(null);
        BranchProfile branchProfile = new BranchProfile();
        if (branchEntity != null) {
            branchProfile.setBranchId(branchEntity.getBranchId());
            branchProfile.setBranchNameEN(branchEntity.getNameEn());
            branchProfile.setBranchNameTH(branchEntity.getNameThai());
        }

        if (employeeEntity != null) {
            branchProfile.setSamOcCode(employeeEntity.getSamOcCode());
            branchProfile.setScbOcCode(employeeEntity.getOcCode());
            OrganizationEntity organization = employeeService.getOrganization(caseInfo.getBranchId());
            if (organization != null) {
                branchProfile.setScbOcNameEN(organization.getOcNameEn());
                branchProfile.setScbOcNameTH(organization.getOcNameTh());
            }
        }

        additionalInfo.setBranchProfile(branchProfile);

        return caseInfo;
    }

    public void postContinue(CaseInfo caseInfo) {
        // set address name from mktInfo to office address
        if (caseInfo.getCustomerInfo().getAddresses() != null) {
            for (CustomerAddress address : caseInfo.getCustomerInfo().getAddresses()) {
                if ("O".equalsIgnoreCase(address.getInternalTypeCode())) {
                    String workplace = caseInfo.getCustomerInfo().getMarketingInfo().getWorkplace();
                    if (!"-".equals(workplace)) {
                        if (StringUtils.isBlank(address.getThaiAddressProvince())) {
                            address.setEngAddressName(workplace);
                        } else {
                            address.setThaiAddressName(workplace);
                        }
                        log.info("Set address name from marketing info : " + workplace);
                    }
                }
            }
        }

        // set flag to portfolio
        if (caseInfo.getPortfolioInfo().getAccounts() != null) {
            for (DepositAccount account : caseInfo.getCustomerInfo().getCustomerAccounts()) {
                PortfolioItem item = caseInfo.getPortfolioInfo().getAccounts().stream()
                        .filter(it -> "IM".equals(it.getApplicationId()) || "ST".equals(it.getApplicationId()))
                        .filter(it -> Objects.equals(it.getAccountNumber(), account.getAccountNumber()))
                        .findFirst().orElse(null);
                if (item == null) {
                    continue;
                }
                item.setOnlineOpenFlag(account.getOnlineOpenFlag());
                item.setMultiTermFlag(account.getMultiTermFlag());
            }
        }

        // filter available customer account
        if (caseInfo.getCustomerInfo().getCustomerAccounts() != null) {
            List<DepositAccount> depositAccounts = caseInfo.getCustomerInfo().getCustomerAccounts().stream()
                    .filter(item -> "ACT".equalsIgnoreCase(item.getAccountStatusCode()) ||
                            "PD1".equalsIgnoreCase(item.getAccountStatusCode()) ||
                            "DOR".equalsIgnoreCase(item.getAccountStatusCode()) ||
                            "FZM".equalsIgnoreCase(item.getAccountStatusCode()) ||
                            (CaseHelper.isForeigner(caseInfo) && "FZA".equalsIgnoreCase(item.getAccountStatusCode())))
                    .collect(Collectors.toList());
            caseInfo.getCustomerInfo().setCustomerAccounts(depositAccounts);
        }
    }

    static Pattern dashRegex = Pattern.compile("-");

    public CaseInfo setChaiYoCaseLoanDetails(CaseInfo caseInfo, ChaiyoLoanDetail chaiyoLoanDetail) {
        AdditionalCaseInfo additionalInfo = caseInfo.getAdditionalInfo();
        additionalInfo.setChaiyoLoanDetail(chaiyoLoanDetail);
        caseInfo.setAdditionalInfo(additionalInfo);
        return caseInfo;
    }

    public void applyCardInfo(CaseInfo caseInfo, CardInfo card) {
        String dob = card.getDob();
        String dobFormatted = dashRegex.splitAsStream(dob)
                .map(x -> "00".equals(x) ? "01" : x)
                .collect(Collectors.joining("-"));

        CustomerInfo customerInfo = caseInfo.getCustomerInfo();
        mappingHelper.copy(card, customerInfo);

        customerInfo.setDob(dobFormatted);
        if ("P1".equals(customerInfo.getDocType())) {
            customerInfo.setNationalityCode("TH");
        } else {
            customerInfo.setNationalityCode(card.getNationalityCode());
        }

        LocalDate birthdate = LocalDate.parse(dobFormatted);
        Period age = Period.between(birthdate, LocalDate.now());
        customerInfo.setAge(age.getYears());
    }

    public void updateApproval(CaseInfo caseInfo, ApprovalDto approval) {
        ApprovalInfo info = caseInfo.getApprovalInfo().stream()
                .filter(it -> it.getFunctionCode().equals(approval.getFunctionCode()) && it.getApprovalStatus().equals("P"))
                .findAny()
                .orElse(null);

        if (info == null) {
            info = new ApprovalInfo();
            info.setFunctionCode(approval.getFunctionCode());
            caseInfo.getApprovalInfo().add(info);
        }

        info.setApprovalId(approval.getApprovalId());
        info.setApprovalStatus(approval.getApprovalStatus().name());
        info.setApprovalType(approval.getType().name());
        info.setReferenceNo(approval.getReferenceNo());
        info.setReferenceType(approval.getReferenceType());
        info.setRequestReason(approval.getRequestReason());

        Map<String, Object> data = approval.getData();
        if (data != null) {
            info.setAccountNameThaiOld((String) data.get("accountNameThaiOld"));
            info.setAccountNameThaiNew((String) data.get("accountNameThaiNew"));
            info.setAccountNameEngOld((String) data.get("accountNameEngOld"));
            info.setAccountNameEngNew((String) data.get("accountNameEngNew"));
            info.setCustomerEmail((String) data.get("customerEmail"));
            info.setCustomerNationality((String) data.get("customerNationality"));
        }

        List<ApprovalDetailsItem> list = new ArrayList<>();
        ApprovalDetailsItem item1 = new ApprovalDetailsItem();
        item1.setChecker(approval.getApproval1());
        item1.setCreatedTime(DateHelper.toISO8601(approval.getApproval1Date()));
        item1.setUpdatedTime(DateHelper.toISO8601(approval.getApproval1Date()));
        item1.setStatus(approval.getApproval1Status().name());
        list.add(item1);

        if (approval.getApproval2() != null) {
            ApprovalDetailsItem item2 = new ApprovalDetailsItem();
            item2.setChecker(approval.getApproval2());
            item2.setCreatedTime(DateHelper.toISO8601(approval.getApproval2Date()));
            item2.setUpdatedTime(DateHelper.toISO8601(approval.getApproval2Date()));
            item2.setStatus(approval.getApproval2Status().name());
            list.add(item2);
        }
        info.setApprovalDetails(list);
    }

    public String getUserAffiliateName(CaseInfo caseInfo, String lang) {
        AdditionalCaseInfo additionalCaseInfo = caseInfo.getAdditionalInfo();
        EmployeeProfile empProfile = additionalCaseInfo.getEmpProfile();

        BranchProfile branchProfile = additionalCaseInfo.getBranchProfile();
        if (branchProfile == null) {
            return "";
        }

        if (Boolean.TRUE.equals(empProfile.getIsPersonalBanker())) {
            if (LANG_EN.equalsIgnoreCase(lang)) {
                return branchProfile.getScbOcNameEN();
            } else {
                return branchProfile.getScbOcNameTH();
            }
        } else {
            if (LANG_EN.equalsIgnoreCase(lang)) {
                return branchProfile.getBranchNameEN();
            } else {
                return branchProfile.getBranchNameTH();
            }
        }
    }

    public void addForeignerApproval(CaseInfo caseInfo) {
        String nationalityCode = caseInfo.getCustomerInfo().getCardInfo().getNationalityCode();
        ApprovalInfo approvalInfo = new ApprovalInfo();
        if (("IR".equals(nationalityCode) || "KP".equals(nationalityCode)) &&
                ("P8".equals(caseInfo.getCustomerInfo().getCardInfo().getDocType()) && caseInfo.getCustomerInfo().getCardInfo().getOldDocNo() != null)) {
            approvalInfo.setFunctionCode("FN_IRKP_EDIT_PP");
            approvalInfo.setApprovalStatus("P");
            caseInfo.getApprovalInfo().add(approvalInfo);
        } else if ("P8".equals(caseInfo.getCustomerInfo().getCardInfo().getDocType()) && caseInfo.getCustomerInfo().getCardInfo().getOldDocNo() != null) {
            approvalInfo.setFunctionCode("FN_EDIT_PP");
            approvalInfo.setApprovalStatus("P");
            caseInfo.getApprovalInfo().add(approvalInfo);
        } else if ("IR".equals(nationalityCode) || "KP".equals(nationalityCode)) {
            approvalInfo.setFunctionCode("FN_IRKP");
            approvalInfo.setApprovalStatus("P");
            caseInfo.getApprovalInfo().add(approvalInfo);
        }
    }
}
