package th.co.scb.onboardingapp.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import th.co.scb.entapi.external_agency_dopa.model.CardStatusOutManual;
import th.co.scb.entapi.otp.model.GenerationResponseDataModel;
import th.co.scb.entapi.ref_common.model.BusinessType;
import th.co.scb.entapi.ref_common.model.Country;
import th.co.scb.entapi.ref_common.model.Occupation;
import th.co.scb.onboardingapp.helper.EmailHelper;
import th.co.scb.onboardingapp.model.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@PropertySource(value = "classpath:tjlog.properties", encoding = "UTF-8")
public class TJLogProcessor {

    @Autowired
    Environment environment;

    @Autowired
    TJLogService tjLogService;

    @Autowired
    EmailHelper emailHelper;

    @Autowired
    MasterDataService masterDataService;

    @Autowired
    private CaseLibraryService caseService;
    private static final String EMAIL = "email";
    private static final String TXN038 = "TXN038";
    private static final String MONITORING = "monitoring";
    private static final String VILLAGE = " Village";
    private static final String CONDITION_PROVINCE = "กรุงเทพ";
    private static final String CONDITION_PROVINCE_EN = "BANGKOK";
    private static final String PREFIX_SUB_DISTRICT = "แขวง ";
    private static final String PREFIX_VILLAGE_1 = "หมู่บ้าน ";
    private static final String PREFIX_VILLAGE_3 = "อาคาร ";
    private static final String PREFIX_FLOOR = "ชั้น ";
    private static final String PREFIX_UNIT = "ห้อง ";
    private static final String PREFIX_IPROFILE = "IPRO";
    private static final String[] foreignerDocType = {"P7", "P8"};
    private static final String CONSENT = "consent";
    private static final String TXN018 = "TXN018";
    private static final String MOO_TH = "หมู่ ";
    private static final String TROK = " Trok";
    private static final String STREET = " Street";
    private static final String DISTRICT = " District";
    private static final String AMPHUR = " Amphur";
    private static final String PROVINCE = " Province";
    private static final String STATE = " State";
    private static final String FLOOR = " Floor";
    private static final String UNIT = " Unit";
    private static final String ZIPCODE = " Zipcode";
    private static final String OFFICE_NAME = " Office Name";

    public void getTxnDopa(CaseInfo cse, Collection<TJLogActivity> tjlogList, CardStatusOutManual cardStatusOut) {
        TJLogActivity tjLogActivity;
        if (cardStatusOut == null) {
            return;
        }
        if (cardStatusOut.getIsError()) {
            tjLogActivity = populateCaseInfo(cse, "TXN051");
            if ("4".equals(cardStatusOut.getCode()) && cardStatusOut.getDesc().contains("สถานะไม่ปกติ =>")) {
                tjLogActivity.setDopaDesc(cardStatusOut.getDesc().replace("สถานะไม่ปกติ =>", ""));
            } else {
                tjLogActivity.setDopaDesc(cardStatusOut.getDesc());
            }
        } else {
            tjLogActivity = populateCaseInfo(cse, "TXN050");
            tjLogActivity.setDopaDesc(cardStatusOut.getDesc());
        }
        tjLogActivity.setReasonCode(cardStatusOut.getCode());
        tjlogList.add(tjLogActivity);
    }

    public void logTxnPrivacy(CaseInfo cse, IdentificationsItem item) {
        if (item == null) {
            return;
        }
        ConsentData data = item.getIdenDetail().getData().get(0);
        TJLogActivity tjLogActivity = populateCaseInfo(cse, "TXN087");
        tjLogActivity.setConsentType(data.getConsentType());
        Scopes scopes = data.getScopes().get(0);
        tjLogActivity.setConsentStatus("privacy_notice".equalsIgnoreCase(scopes.getKey()) ? scopes.getDisplay().getValue() : "");
        tjLogActivity.setConsentUpdateDate(data.getConsentUpdateDate());

        tjLogService.addActivity(tjLogActivity);
    }

    private TJLogActivity populateCaseInfo(CaseInfo caseInfo, String txnCode) {
        TJLogActivity tjLogActivity = new TJLogActivity();
        tjLogActivity.setTxnCode(txnCode);
        tjLogActivity.setCaseId(caseInfo.getCaseId());
        tjLogActivity.setBranchId(caseInfo.getBranchId());
        if (environment.getProperty("TXN_ESUBMISSION").contains(txnCode)) {
            if (caseInfo.getBookingBranchId() != null) {
                tjLogActivity.setBookbr(caseInfo.getBookingBranchId());
            } else {
                tjLogActivity.setBookbr("");
            }
        }
        tjLogActivity.setEmployeeId(caseInfo.getEmployeeId());
        tjLogActivity.setReferenceId(caseInfo.getReferenceId());
        tjLogActivity.setAppFormNo(caseInfo.getAppFormNo());
        tjLogActivity.setDocNo(caseInfo.getCustomerInfo().getDocNo());
        tjLogActivity.setThaiTitle(caseInfo.getCustomerInfo().getThaiTitle());
        tjLogActivity.setThaiFirstName(caseInfo.getCustomerInfo().getThaiFirstName());
        tjLogActivity.setThaiMiddleName(caseInfo.getCustomerInfo().getThaiMiddleName());
        tjLogActivity.setThaiLastName(caseInfo.getCustomerInfo().getThaiLastName());
        tjLogActivity.setTimestampCreated(new Timestamp(System.currentTimeMillis()));

        tjLogActivity.setForeigner(isForeigner(caseInfo));
        if (tjLogActivity.isForeigner()) {
            CardInfo cardInfo = caseInfo.getCustomerInfo().getCardInfo();
            tjLogActivity.setEngTitle(cardInfo.getEngTitle());
            tjLogActivity.setEngFirstName(cardInfo.getEngFirstName());
            tjLogActivity.setEngMiddleName(cardInfo.getEngMiddleName());
            tjLogActivity.setEngLastName(cardInfo.getEngLastName());
            tjLogActivity.setDocNo(cardInfo.getDocNo());
        }

        return tjLogActivity;
    }

    public boolean isForeigner(CaseInfo caseInfo) {
        if (caseInfo == null) {
            return false;
        }
        if (ObjectUtils.isEmpty(caseInfo.getCustomerInfo().getDocType())) {
            return false;
        }
        return Arrays.stream(foreignerDocType).anyMatch(s -> s.equalsIgnoreCase(caseInfo.getCustomerInfo().getDocType().toUpperCase()));
    }

    public void getTxnDopaFail(CaseInfo caseInfo, Collection<TJLogActivity> tjlogList) {
        TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN096");
        tjlogList.add(tjLogActivity);
    }

    @Async
    public void logCaseCreateTxn(CaseInfo caseInfo, Collection<TJLogActivity> tjLogList) {
        if (caseInfo.getCustomerInfo().getCardInfo().isManualKeyIn()) {
            logTxnWithDescType(caseInfo, tjLogList, "TXN024", "id");//Manual Key-in
        }
        TJLogActivity dopaReject = tjLogList.stream()
                .filter(it -> "TXN051".equalsIgnoreCase(it.getTxnCode()))
                .findAny().orElse(null);
        IdentificationsItem vip = getIdentificationsItem(caseInfo.getCustomerInfo(), "vip");
        IdentificationsItem monitor = getIdentificationsItem(caseInfo.getCustomerInfo(), MONITORING);
        IdentificationsItem cdd = getIdentificationsItem(caseInfo.getCustomerInfo(), "cdd");

        if ("N".equalsIgnoreCase(caseInfo.getCustomerInfo().getIsExistingCustomer())) {
            logTxnWithDescType(caseInfo, tjLogList, "TXN001", "id");//New Customer
            if (dopaReject == null) {
                logTxnWithDescType(caseInfo, tjLogList, "TXN019", "id");//CDD enquiry
                if (cdd != null) {
                    String cddStatus = cdd.getIdenStatus();
                    if ("nohit".equalsIgnoreCase(cddStatus)) {
                        logTxnWithDescType(caseInfo, tjLogList, "TXN020", "id");//CDD Blank
                    }
                    if ("hit".equalsIgnoreCase(cddStatus)) {
                        logTxnWithDescType(caseInfo, tjLogList, "TXN021", "id");//CDD Detica
                    }
                }
            }
        } else {
            if (vip != null && "fail".equalsIgnoreCase(vip.getIdenStatus())) {
                logTxnWithDescType(caseInfo, tjLogList, "TXN047", "id");//VIP customer
            } else {
                logTxnWithDescType(caseInfo, tjLogList, "TXN046", "id");//Existing Customer
            }

            if (dopaReject == null) {
                logTxnWithDescType(caseInfo, tjLogList, "TXN002", "id");//RM monitor enquiry
                if (monitor != null) {
                    String monitoringCode = monitor.getIdenDetail().getMonitoring().getCode();
                    if ("".equals(monitoringCode)) {
                        logTxnWithDescType(caseInfo, tjLogList, "TXN003", "id");
                    }

                    if (!caseInfo.getCaseTypeID().equalsIgnoreCase(PREFIX_IPROFILE)) {
                        if ("101".equals(monitoringCode)) {
                            logTxnWithDescType(caseInfo, tjLogList, "TXN004", "id");
                        }
                        if ("102".equals(monitoringCode)) {
                            logTxnWithDescType(caseInfo, tjLogList, "TXN005", "id");
                        }
                        if ("103".equals(monitoringCode)) {
                            logTxnWithDescType(caseInfo, tjLogList, "TXN006", "id");
                        }
                    }
                    if ("104".equals(monitoringCode)) {
                        logTxnWithDescType(caseInfo, tjLogList, "TXN007", "id");
                    }
                    if ("105".equals(monitoringCode)) {
                        logTxnWithDescType(caseInfo, tjLogList, "TXN008", "id");
                    }
                }
            }
        }
        tjLogService.addActivityList(tjLogList);
    }

    private void logTxnWithDescType(CaseInfo caseInfo, Collection<TJLogActivity> tjLogList, String txnCode, String descType) {
        TJLogActivity tjLogActivity;
        tjLogActivity = populateCaseInfo(caseInfo, txnCode);
        if ("id".equalsIgnoreCase(descType)) {
            tjLogActivity.setIdNo(caseInfo.getCustomerInfo().getDocNo());
        }
        if (EMAIL.equalsIgnoreCase(descType)) {
            tjLogActivity.setEmailId(emailHelper.getBranchEmail(caseInfo.getBranchId()));
        }
        tjLogList.add(tjLogActivity);
    }

    private IdentificationsItem getIdentificationsItem(CustomerInfo customerInfo, String idenType) {
        return customerInfo.getIdentifications().stream()
                .filter(it -> idenType.equalsIgnoreCase(it.getIdenType()))
                .findAny().orElse(null);
    }

    public void getTxnWithName(CaseInfo caseInfo, String txnCode, Collection<TJLogActivity> tjlogList) {
        TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, txnCode);
        tjLogActivity.setESubRank(Integer.parseInt(environment.getProperty("RANK_" + txnCode)));
        tjlogList.add(tjLogActivity);
    }

    public void getTxnAddCustInfo(CaseInfo caseInfo, Collection<TJLogActivity> tjlogList) {
        Collection<TJLogActivity> tjLogActivities = new ArrayList<>();
        int rank = Integer.parseInt(environment.getProperty("RANK_TXN018"));
        CustomerInfo customerInfo = caseInfo.getCustomerInfo();
        Boolean isForeignerCustomer = isForeigner(caseInfo);
        addCustInfo(tjLogActivities, caseInfo, "Title", customerInfo.getThaiTitle(), rank);
        addCustInfo(tjLogActivities, caseInfo, "Name", customerInfo.getThaiFirstName(), rank);
        addCustInfo(tjLogActivities, caseInfo, "Middle Name", customerInfo.getThaiMiddleName(), rank);
        addCustInfo(tjLogActivities, caseInfo, "Surname", customerInfo.getThaiLastName(), rank);
        if (isForeignerCustomer) {
            addCustInfo(tjLogActivities, caseInfo, "English Title", customerInfo.getEngTitle(), rank);
            addCustInfo(tjLogActivities, caseInfo, "English Name", customerInfo.getEngFirstName(), rank);
            addCustInfo(tjLogActivities, caseInfo, "English Middle Name", customerInfo.getEngMiddleName(), rank);
            addCustInfo(tjLogActivities, caseInfo, "English Surname", customerInfo.getEngLastName(), rank);
        }
        addCustInfo(tjLogActivities, caseInfo, "National ID", customerInfo.getDocNo(), rank);
        addCustInfo(tjLogActivities, caseInfo, "Date of Birth", customerInfo.getDob(), rank);
        addCustInfo(tjLogActivities, caseInfo, "Nationality", getCountryNameByCode(customerInfo.getNationalityCode()), rank);
        addCustInfo(tjLogActivities, caseInfo, "Customer Type", customerInfo.getCustomerTypeCode(), rank);
        addCustInfo(tjLogActivities, caseInfo, "Tax ID No", customerInfo.getTaxId(), rank);
        addCustInfo(tjLogActivities, caseInfo, "Source of Income", customerInfo.getSourceOfIncome(), rank);
        addCustInfo(tjLogActivities, caseInfo, "ActualIncome", customerInfo.getActualIncome() != null ? customerInfo.getActualIncome().toString() : null, rank);

        addMobileNumInfo(tjLogActivities, caseInfo, "Mobile Phone");
        addOccupationInfo(tjLogActivities, caseInfo, "Occupation");
        addAddressInfo(tjLogActivities, caseInfo, "Current Address", "C");
        if (isForeignerCustomer) {
            addNationalityAddressInfo(tjLogActivities, caseInfo, "Nationality Address");
        }
        tjlogList.addAll(tjLogActivities);
    }

    private void addNationalityAddressInfo(Collection<TJLogActivity> tjLogActivities, CaseInfo caseInfo, String fieldName) {
        int rank = Integer.parseInt(environment.getProperty("RANK_TXN018_A"));
        CustomerAddress customerAddress = caseInfo.getCustomerInfo().getAddresses().stream()
                .filter(it -> "N".equalsIgnoreCase(it.getUsageCode()))
                .findAny().orElse(null);
        if (customerAddress != null) {
            addAddressCustInfo(tjLogActivities, caseInfo, fieldName + ZIPCODE, "", customerAddress.getZipCode(), rank);
            addAddressCustInfo(tjLogActivities, caseInfo, fieldName + FLOOR, PREFIX_FLOOR, customerAddress.getFloorNumber(), rank);
            addAddressCustInfo(tjLogActivities, caseInfo, fieldName + UNIT, PREFIX_UNIT, customerAddress.getUnitNumber(), rank);

            if ("1".equals(customerAddress.getFormatCode())) {
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + VILLAGE, PREFIX_VILLAGE_1, customerAddress.getEngAddressVillage(), rank);
            } else if ("3".equals(customerAddress.getFormatCode())) {
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + VILLAGE, PREFIX_VILLAGE_3, customerAddress.getEngAddressVillage(), rank);
            }
            addAddressCustInfo(tjLogActivities, caseInfo, fieldName + " No", "", customerAddress.getEngAddressNumber(), rank);
            addAddressCustInfo(tjLogActivities, caseInfo, fieldName + " City", "", customerAddress.getEngAddressAmphur(), rank);
            addAddressCustInfo(tjLogActivities, caseInfo, fieldName + STATE, "", customerAddress.getEngAddressState(), rank);
            addAddressCustInfo(tjLogActivities, caseInfo, fieldName + " Country", "", getCountryNameENGByCode(customerAddress.getCountryCode()), rank);
        }
    }

    private void addOccupationInfo(Collection<TJLogActivity> tjLogActivities, CaseInfo caseInfo, String fieldName) {
        int rank = Integer.parseInt(environment.getProperty("RANK_TXN018_O"));
        TJLogOccupation tjLogOccupation = new TJLogOccupation();
        getOccupationData(caseInfo.getCustomerInfo(), tjLogOccupation, false);
        getBusinessData(caseInfo.getCustomerInfo(), tjLogOccupation, false);
        addCustInfo(tjLogActivities, caseInfo, fieldName + " Occupation", tjLogOccupation.getNewOccupationDescTh(), rank);
        addCustInfo(tjLogActivities, caseInfo, fieldName + " Sub-type", tjLogOccupation.getNewSubOccupationDescTh(), rank);
        addCustInfo(tjLogActivities, caseInfo, fieldName + " รายละเอียดที่ระบุ", caseInfo.getCustomerInfo().getOccupationDescription(), rank);
        addCustInfo(tjLogActivities, caseInfo, fieldName + " Major", tjLogOccupation.getNewMajorBusinessDescTh(), rank);
        addCustInfo(tjLogActivities, caseInfo, fieldName + " Minor", tjLogOccupation.getNewMinorBusinessDescTh(), rank);
    }

    private void getBusinessData(IndividualInfo custInfo, TJLogOccupation tjLogOccupation, boolean isLegacy) {
        List<BusinessType> businessTypes = masterDataService.businessTypes();
        String parentBusinessTypeDesc = null;
        String businessTypeDesc = null;
        if (custInfo.getOccupationIsicCode() != null && StringUtils.isNotEmpty(custInfo.getOccupationIsicCode())) {
            BusinessType businessType = businessTypes.stream()
                    .filter(it -> custInfo.getOccupationIsicCode().equalsIgnoreCase(it.getBusinessCode()))
                    .findAny().orElse(null);
            if (businessType != null && hasValue(businessType.getBusinessParentCode())) {
                BusinessType parentBusinessType = businessTypes.stream()
                        .filter(it -> businessType.getBusinessParentCode().equalsIgnoreCase(it.getBusinessCode()))
                        .findAny().orElse(null);
                if (parentBusinessType != null) {
                    parentBusinessTypeDesc = parentBusinessType.getBusinessDescTh();
                    businessTypeDesc = businessType.getBusinessDescTh();
                }
            }
        }
        if (isLegacy) {
            // Add only case of change/new occupation Isic Code.. If occupation Isic Code is null, it represent customer no change anything
            if (custInfo.getOccupationIsicCode() != null) {
                tjLogOccupation.setOldMajorBusinessDescTh(parentBusinessTypeDesc == null ? "" : parentBusinessTypeDesc);
                tjLogOccupation.setOldMinorBusinessDescTh(businessTypeDesc == null ? "" : businessTypeDesc);
            }
        } else {
            tjLogOccupation.setNewMajorBusinessDescTh(parentBusinessTypeDesc == null ? "" : parentBusinessTypeDesc);
            tjLogOccupation.setNewMinorBusinessDescTh(businessTypeDesc == null ? "" : businessTypeDesc);
        }
    }

    private String getCountryNameENGByCode(String code) {
        if (code != null) {
            Country country = masterDataService.countries().stream()
                    .filter(it -> code.equals(it.getCountryCode()))
                    .findAny().orElse(null);
            return country != null ? country.getCountryDescEn() : "";
        } else {
            return "";
        }
    }

    private void addAddressCustInfo(Collection<TJLogActivity> tjLogActivities, CaseInfo caseInfo, String fieldName, String valuePrefix, String value, int rank) {
        if (hasValue(value)) {
            TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, TXN018);
            tjLogActivity.setFieldValues(fieldName, "", valuePrefix + value);
            tjLogActivity.setESubRank(rank);
            tjLogActivities.add(tjLogActivity);
        }
    }

    private void getOccupationData(IndividualInfo custInfo, TJLogOccupation tjLogOccupation, boolean isLegacy) {
        List<Occupation> occupations = masterDataService.occupationTypes();
        String occupationDesc = null;
        String subOccupationDesc = null;
        if (custInfo.getOccupationCode() != null && StringUtils.isNotEmpty(custInfo.getOccupationCode())) {
            Occupation occupation = occupations.stream()
                    .filter(it -> custInfo.getOccupationCode().equalsIgnoreCase(it.getOccupationCode()))
                    .findAny().orElse(null);
            if (occupation != null) {
                if (hasValue(occupation.getOccupationParentCode())) {
                    Occupation parentOccupation = occupations.stream()
                            .filter(it -> occupation.getOccupationParentCode().equalsIgnoreCase(it.getOccupationCode()))
                            .findAny().orElse(null);

                    if (parentOccupation != null) {
                        occupationDesc = (parentOccupation.getOccupationShortDescTh() == null ? "" : parentOccupation.getOccupationShortDescTh());
                        subOccupationDesc = (occupation.getOccupationShortDescTh() == null ? "" : occupation.getOccupationShortDescTh());
                    }
                } else {
                    occupationDesc = occupation.getOccupationShortDescTh();
                    subOccupationDesc = "";
                }
            }
        }
        if (isLegacy) {
            // Add only case of change/new occupation.. If occupation Code is null, it represent customer no change anything
            if (custInfo.getOccupationCode() != null) {
                tjLogOccupation.setOldOccupationDescTh(occupationDesc);
                tjLogOccupation.setOldSubOccupationDescTh(subOccupationDesc);
            }
        } else {
            tjLogOccupation.setNewOccupationDescTh(occupationDesc);
            tjLogOccupation.setNewSubOccupationDescTh(subOccupationDesc);
        }
    }

    public void addMobileNumInfo(Collection<TJLogActivity> tjLogActivities, CaseInfo caseInfo, String fieldName) {
        int rank = Integer.parseInt(environment.getProperty("RANK_TXN018"));
        List<CustomerContactChannel> customerContactChannels = caseInfo.getCustomerInfo().getContactChannels().stream()
                .filter(it -> hasValue(it.getTxnType()) && "002".equals(it.getContactTypeCode()))
                .filter(it -> !"D".equalsIgnoreCase(it.getTxnType()))
                .collect(Collectors.toList());
        for (CustomerContactChannel contact : customerContactChannels) {
            TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, TXN018);
            tjLogActivity.setFieldValues(fieldName, "", contact.getContactNumber());
            tjLogActivity.setESubRank(rank);
            tjLogActivities.add(tjLogActivity);
        }
    }

    private void addAddressInfo(Collection<TJLogActivity> tjLogActivities, CaseInfo caseInfo, String fieldName, String addressCode) {
        int rank = Integer.parseInt(environment.getProperty("RANK_TXN018_A"));
        CustomerAddress customerAddress = caseInfo.getCustomerInfo().getAddresses().stream()
                .filter(it -> addressCode.equalsIgnoreCase(it.getInternalTypeCode()))
                .findAny().orElse(null);
        if (customerAddress != null) {
            String amphur;
            String subDis;
            String province;
            if (isThaiCapitalProvince(customerAddress)) {
                amphur = "เขต ";
                subDis = PREFIX_SUB_DISTRICT;
                province = "";
            } else {
                amphur = "อ.";
                subDis = "ต.";
                province = "จ.";
            }
            if ("1".equals(customerAddress.getFormatCode())) {
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + VILLAGE, PREFIX_VILLAGE_1, getAddressVillageLang(customerAddress), rank);
            } else if ("3".equals(customerAddress.getFormatCode())) {
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + VILLAGE, PREFIX_VILLAGE_3, getAddressVillageLang(customerAddress), rank);
            }
            if ("TH".equalsIgnoreCase(customerAddress.getAddrLang())) {
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + " No", "", customerAddress.getThaiAddressNumber(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + " Moo", MOO_TH, customerAddress.getThaiAddressMoo(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + TROK, "ตรอก", customerAddress.getThaiAddressTrok(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + " Soi", "ซ.", customerAddress.getThaiAddressSoi(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + STREET, "ถ.", customerAddress.getThaiAddressThanon(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + DISTRICT, subDis, customerAddress.getThaiAddressDistrict(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + AMPHUR, amphur, customerAddress.getThaiAddressAmphur(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + PROVINCE, province, customerAddress.getThaiAddressProvince(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + STATE, "", customerAddress.getThaiAddressState(), rank);
            }
            if ("EN".equalsIgnoreCase(customerAddress.getAddrLang())) {
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + " No", "", customerAddress.getEngAddressNumber(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + " Moo", MOO_TH, customerAddress.getEngAddressMoo(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + TROK, "ตรอก", customerAddress.getEngAddressTrok(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + " Soi", "ซ.", customerAddress.getEngAddressSoi(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + STREET, "ถ.", customerAddress.getEngAddressThanon(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + DISTRICT, subDis, customerAddress.getEngAddressDistrict(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + AMPHUR, amphur, customerAddress.getEngAddressAmphur(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + PROVINCE, province, customerAddress.getEngAddressProvince(), rank);
                addAddressCustInfo(tjLogActivities, caseInfo, fieldName + STATE, "", customerAddress.getEngAddressState(), rank);
            }
            addAddressCustInfo(tjLogActivities, caseInfo, fieldName + FLOOR, PREFIX_FLOOR, customerAddress.getFloorNumber(), rank);
            addAddressCustInfo(tjLogActivities, caseInfo, fieldName + UNIT, PREFIX_UNIT, customerAddress.getUnitNumber(), rank);
            addAddressCustInfo(tjLogActivities, caseInfo, fieldName + ZIPCODE, "", customerAddress.getZipCode(), rank);
        }
    }

    public String getAddressVillageLang(CustomerAddress customerAddress) {
        if (customerAddress.getAddrLang() != null && "TH".equalsIgnoreCase(customerAddress.getAddrLang())) {
            return customerAddress.getThaiAddressVillage();
        } else if (StringUtils.isNotEmpty(customerAddress.getThaiAddressVillage())) {
            return customerAddress.getThaiAddressVillage();
        } else if (customerAddress.getAddrLang() != null && "EN".equalsIgnoreCase(customerAddress.getAddrLang())) {
            return customerAddress.getEngAddressVillage();
        } else if (StringUtils.isNotEmpty(customerAddress.getEngAddressVillage())) {
            return customerAddress.getEngAddressVillage();
        }
        return "";
    }

    private void addCustInfo(Collection<TJLogActivity> tjLogActivities, CaseInfo caseInfo, String fieldName, String value, int rank) {
        if (hasValue(value)) {
            TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, TXN018);
            tjLogActivity.setFieldValues(fieldName, "", value);
            tjLogActivity.setESubRank(rank);
            tjLogActivities.add(tjLogActivity);
        }
    }

    private boolean hasValue(String input) {
        return !(input == null || input.isEmpty());
    }

    public boolean isThaiCapitalProvince(CustomerAddress customerAddress) {
        if (customerAddress == null) {
            return false;
        }
        String provinceEN = StringUtils.isNotEmpty(customerAddress.getEngAddressProvince()) ? customerAddress.getEngAddressProvince() : "";
        String provinceTH = StringUtils.isNotEmpty(customerAddress.getThaiAddressProvince()) ? customerAddress.getThaiAddressProvince() : "";
        return provinceEN.contains(CONDITION_PROVINCE_EN) || provinceTH.contains(CONDITION_PROVINCE);
    }

    private String getCountryNameByCode(String code) {
        if (code != null) {
            Country country = masterDataService.countries().stream()
                    .filter(it -> code.equals(it.getCountryCode()))
                    .findAny().orElse(null);
            return country != null ? country.getCountryDescTh() : "";
        } else {
            return "";
        }
    }

    @Async
    public void logTxnDetica(CaseInfo caseInfo, String txncode) {

        TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, txncode);
        if ("TXN077".equalsIgnoreCase(txncode)) {
            tjLogActivity.setDeticaResult("Pass");
        }
        tjLogService.addActivity(tjLogActivity);
    }

    @Async
    public void logApprovalTxn(CaseInfo caseInfo, TJLogApproval approval) {
        String txnCode = "";
        if ("M_101".equalsIgnoreCase(approval.getFunctionCode())) {
            txnCode = "TXN011";
        }
        if ("M_102".equalsIgnoreCase(approval.getFunctionCode())) {
            txnCode = "TXN012";
        }
        if ("VIP".equalsIgnoreCase(approval.getFunctionCode())) {
            txnCode = "TXN013";
        }
        if ("CAL".equalsIgnoreCase(approval.getFunctionCode())) {
            txnCode = "TXN023";
        }
        if ("E_SUBMIT".equalsIgnoreCase(approval.getFunctionCode())) {
            txnCode = "TXN028";
        }
        if ("FATCA".equalsIgnoreCase(approval.getFunctionCode())) {
            txnCode = "TXN035";
        }
        if ("DOPA".equalsIgnoreCase(approval.getFunctionCode())) {
            txnCode = "TXN052";
        }
        if ("BOT48".equalsIgnoreCase(approval.getFunctionCode())) {
            logBOT48Approval(caseInfo, approval);
            return;
        }
        if ("BKL".equalsIgnoreCase(approval.getFunctionCode())) {
            logBKLApproval(caseInfo, approval);
            return;
        }
        if ("BOT48_BKL".equalsIgnoreCase(approval.getFunctionCode())) {
            logBOT48Approval(caseInfo, approval);
            logBKLApproval(caseInfo, approval);
            return;
        }
        if ("FN_EDIT_PP".equalsIgnoreCase(approval.getFunctionCode())) {
            logRequestApprovePassportChange(caseInfo, approval);
            return;
        }
        if ("SKIP_REDLIST_FAIL".equalsIgnoreCase(approval.getFunctionCode())) {
            logApproveRequestOrSkipRedListFail(caseInfo);
            return;
        }
        if ("SKIP_VERIFYEMAIL".equalsIgnoreCase(approval.getFunctionCode())) {
            logApproveRequestOrSkipVerifyEmail(caseInfo);
            return;
        }
        if ("SKIP_VERIFYEMAIL_FAIL".equalsIgnoreCase(approval.getFunctionCode())) {
            logApproveRequestOrSkipVerifyEmailFail(caseInfo);
            return;
        }

        if (!"A".equals(approval.getApprovalStatus()) || "".equals(txnCode)) {
            return;
        }
        TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, txnCode);
        tjLogActivity.setApprovalInfo(caseInfo.getApprovalInfo());
        tjLogActivity.setApprove(true);
        tjLogActivity.setApprovalState(approval);
        tjLogService.addActivity(tjLogActivity);
    }

    public void logRequestApprovePassportChange(CaseInfo caseInfo, TJLogApproval approval) {
        TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN104");
        tjLogActivity.setApprovalInfo(caseInfo.getApprovalInfo());
        tjLogActivity.setApprove(true);
        tjLogActivity.setApprovalState(approval);
        tjLogService.addActivity(tjLogActivity);
    }

    @Async
    public void logBOT48Approval(CaseInfo caseInfo, TJLogApproval approval) {
        TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN084");
        tjLogActivity.setWatchlistData("Hit");
        tjLogActivity.setIdNo(caseInfo.getCustomerInfo().getDocNo());
        tjLogActivity.setApprovalInfo(caseInfo.getApprovalInfo());
        tjLogActivity.setApprove(true);
        tjLogActivity.setApprovalState(approval);
        tjLogService.addActivity(tjLogActivity);
    }

    @Async
    public void logBKLApproval(CaseInfo caseInfo, TJLogApproval approval) {
        IdentificationsItem identificationsItem = caseInfo.getCustomerInfo().getIdentifications().stream()
                .filter(it -> "watchlist".equalsIgnoreCase(it.getIdenType()))
                .findFirst().orElse(null);

        if (identificationsItem != null) {
            for (BklAns bklAns : identificationsItem.getIdenDetail().getBklAns()) {
                TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN085");
                String watchlistType = bklAns.getWatchlistTypes().stream().map(WatchlistTypes::getTypeCode).collect(Collectors.joining(","));
                tjLogActivity.setWatchlistData("Hit, WL ID: " + bklAns.getWlID() + ", Type: " + watchlistType + ", Result: " + bklAns.getAnswer());
                tjLogActivity.setIdNo(caseInfo.getCustomerInfo().getDocNo());
                tjLogActivity.setApprovalInfo(caseInfo.getApprovalInfo());
                tjLogActivity.setApprove(true);
                tjLogActivity.setApprovalState(approval);
                tjLogService.addActivity(tjLogActivity);
            }
        }
    }

    public void logApproveRequestOrSkipRedListFail(CaseInfo caseInfo) {
        ApprovalInfo approvalInfo = caseInfo.getApprovalInfo().stream()
                .filter(i -> "SKIP_REDLIST_FAIL".equals(i.getFunctionCode()))
                .findFirst().orElse(null);
        if (approvalInfo != null) {
            TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN117");
            tjLogActivity.setOtpEmailContactEmail(approvalInfo.getCustomerEmail());
            tjLogService.addActivity(tjLogActivity);
        }
    }

    public void logApproveRequestOrSkipVerifyEmail(CaseInfo caseInfo) {
        ApprovalInfo approvalInfo = caseInfo.getApprovalInfo().stream()
                .filter(i -> "SKIP_VERIFYEMAIL".equals(i.getFunctionCode())).findFirst().orElse(null);
        if (approvalInfo != null) {
            TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN118");
            tjLogActivity.setOtpEmailContactEmail(approvalInfo.getCustomerEmail());
            tjLogService.addActivity(tjLogActivity);
        }
    }

    public void logApproveRequestOrSkipVerifyEmailFail(CaseInfo caseInfo) {
        ApprovalInfo approvalInfo = caseInfo.getApprovalInfo().stream()
                .filter(i -> "SKIP_VERIFYEMAIL_FAIL".equals(i.getFunctionCode()))
                .findFirst().orElse(null);
        if (approvalInfo != null) {
            TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN119");
            tjLogActivity.setOtpEmailContactEmail(approvalInfo.getCustomerEmail());
            tjLogService.addActivity(tjLogActivity);
        }
    }

    public void getTxnKYC(CaseInfo caseInfo, Collection<TJLogActivity> tjlogList) {
        IdentificationsItem kyc = getIdentificationsItem(caseInfo.getCustomerInfo(), "kyc");
        if (kyc != null && kyc.getIdenDetail().getKyc().getKycFormType() != null) {
            TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN044");
            tjLogActivity.setESubRank(Integer.parseInt(environment.getProperty("RANK_TXN044")));
            tjLogActivity.setKycLevel(kyc.getIdenDetail().getKyc().getKycFormType().toUpperCase());
            tjlogList.add(tjLogActivity);
        }
    }

    public void logEsubmissionTxn(Collection<TJLogActivity> tjlogList) {
        Collection<TJLogActivity> sortedList = tjlogList.stream()
                .sorted(Comparator.comparing(TJLogActivity::getESubRank))
                .collect(Collectors.toList());
        tjLogService.addActivityList(sortedList);
    }

    @Async
    public void logApprovalNotRequired(CaseInfo caseInfo, TJLogApproval approval) {
        TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN028");
        tjLogActivity.setApprovalInfo(caseInfo.getApprovalInfo());
        tjLogActivity.setApprove(true);
        tjLogActivity.setApprovalState(approval);
        tjLogService.addActivity(tjLogActivity);
    }

    public void logUITxn(TJLogUIActivity tjLogUIActivity, String caseId) {
        CaseInfo caseInfo = caseService.getCase(caseId);
        TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, tjLogUIActivity.getTxnCode());
        if (hasValue(tjLogUIActivity.getTc())) {
            tjLogActivity.setTc(tjLogUIActivity.getTc());
        }
        if (hasValue(caseInfo.getCustomerInfo().getAddingMobileNoVerification())) {
            tjLogActivity.setCondition_message(caseInfo.getCustomerInfo().getAddingMobileNoVerification());
        }
        if ("TXN086".equalsIgnoreCase(tjLogActivity.getTxnCode())) {
            IdentificationsItem identificationsItem = caseInfo.getCustomerInfo().getIdentifications().stream()
                    .filter(it -> CONSENT.equalsIgnoreCase(it.getIdenType()))
                    .findFirst()
                    .orElse(null);
            if (identificationsItem != null) {
                identificationsItem.getIdenDetail().getData().stream()
                        .filter(it -> "005".equalsIgnoreCase(it.getConsentType()))
                        .findFirst().ifPresent(consentData -> logConsent(tjLogActivity, consentData));
            }
        }
        if ("TXN090".equalsIgnoreCase(tjLogActivity.getTxnCode())) {
            IdentificationsItem item = caseInfo.getCustomerInfo().getIdentifications().stream()
                    .filter(it -> "securitiesFlag".equalsIgnoreCase(it.getIdenType())).findFirst().orElse(null);
            if (item != null) {
                ConsentData securities = item.getIdenDetail().getData().get(0);
                tjLogActivity.setConsentStatus(securities.getConsentStatus());
                tjLogActivity.setConsentVersion(securities.getConsentVersion());
            }
        }

        if ("TXN100".equalsIgnoreCase(tjLogActivity.getTxnCode())) {
            tjLogActivity.setDOB(caseInfo.getCustomerInfo().getCardInfo().getDob());
            tjLogActivity.setNationalityCode(caseInfo.getCustomerInfo().getCardInfo().getNationalityCode());
        }

        if ("TXN106".equalsIgnoreCase(tjLogActivity.getTxnCode())) {
            IdentificationsItem identificationsItem = caseInfo.getCustomerInfo().getIdentifications().stream()
                    .filter(it -> CONSENT.equalsIgnoreCase(it.getIdenType()))
                    .findFirst()
                    .orElse(null);
            if (identificationsItem != null) {
                identificationsItem.getIdenDetail().getData().stream()
                        .filter(it -> "006".equalsIgnoreCase(it.getConsentType()) || "006-F".equalsIgnoreCase(it.getConsentType()))
                        .findAny().ifPresent(consentData -> logConsent(tjLogActivity, consentData));
            }
        }

        tjLogService.addActivity(tjLogActivity);
    }

    private void logConsent(TJLogActivity tjLogActivity, ConsentData consentData) {
        tjLogActivity.setConsentType(consentData.getConsentType());
        tjLogActivity.setConsentVersion(consentData.getConsentVersion());
        tjLogActivity.setConsentStatus(consentData.getScopes().get(0).getDisplay().getValue());
        tjLogActivity.setConsentUpdateDate(consentData.getConsentUpdateDate());
    }

    @Async
    public void logFaceCapturing(String caseId) {
        CaseInfo caseInfo = caseService.getCase(caseId);
        TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN101");
        tjLogActivity.setFaceCaptureStatus("Success");
        tjLogService.addActivity(tjLogActivity);
    }
    @Async
    public void logFaceRecognitionTxn(CaseInfo caseInfo, String scoreText, Boolean isPass) {
        if (StringUtils.isNotEmpty(scoreText)) {
            String txnCode = isPass ? "TXN056" : "TXN057";
            TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, txnCode);
            tjLogActivity.setFaceScore(scoreText);
            tjLogService.addActivity(tjLogActivity);
        }
    }
    @Async
    public void logTxnPep(String caseId, String pep) {
        CaseInfo caseInfo = tjLogService.getCase(caseId);

        TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN081");
        tjLogActivity.setDeticaResult(pep);
        tjLogService.addActivity(tjLogActivity);

    }
    @Async
    public void logTxnSendOTP(String caseId, GenerationResponseDataModel response, String mobileNumber) {
        CaseInfo caseInfo = tjLogService.getCase(caseId);
        if (hasValue(response.getTokenUUID())) {
            TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN026");
            tjLogActivity.setMobileNo(mobileNumber);
            tjLogActivity.setRefNo(response.getTokenUUID() + ", " + response.getReference());
            tjLogService.addActivity(tjLogActivity);
        }
    }
    @Async
    public void logTxnEnterOTP(String caseId, String tokenUUID) {
        CaseInfo caseInfo = tjLogService.getCase(caseId);
        if (hasValue(tokenUUID)) {
            TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN027");
            tjLogActivity.setRefNo(tokenUUID);
            tjLogService.addActivity(tjLogActivity);
        }
    }

    public void logCheckRedList(String caseId, String emailAddress, String internalRedlistFlag) {
        CaseInfo caseInfo = caseService.getCase(caseId);
        TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN111");
        tjLogActivity.setOtpEmailContactEmail(emailAddress);
        String redListResult = "true".equalsIgnoreCase(internalRedlistFlag) ? "N" : "Y";
        tjLogActivity.setInternalRedlistFlag(redListResult);
        tjLogService.addActivity(tjLogActivity);
    }

    public void logCheckRedListFail(String caseId, String emailAddress) {
        CaseInfo caseInfo = caseService.getCase(caseId);
        TJLogActivity tjLogActivity = populateCaseInfo(caseInfo, "TXN112");
        tjLogActivity.setOtpEmailContactEmail(emailAddress);
        tjLogService.addActivity(tjLogActivity);
    }

}
