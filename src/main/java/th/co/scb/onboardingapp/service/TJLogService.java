package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.exception.NotFoundException;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.mapper.ActivityMapper;
import th.co.scb.onboardingapp.mapper.CaseMapper;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.entity.ActivityEntity;
import th.co.scb.onboardingapp.model.entity.CaseEntity;
import th.co.scb.onboardingapp.repository.ActivityJpaRepository;
import th.co.scb.onboardingapp.repository.CaseJpaRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@PropertySource(value = "classpath:tjlog.properties", encoding = "UTF-8")
public class TJLogService {

    @Autowired
    CaseJpaRepository caseRepository;

    @Autowired
    Environment env;

    @Autowired
    MappingHelper mappingHelper;

    @Autowired
    ActivityJpaRepository activityRepository;

    private static final String TXN094 = "TXN094";

    public void addActivityList(Collection<TJLogActivity> tjLogActivities) {
        List<TJLogActivityDto> tjLogActivityDtoList = new ArrayList<>();
        for (TJLogActivity tjLogActivity : tjLogActivities) {
            tjLogActivityDtoList.add(buildActivityDto(tjLogActivity));
        }

        //TODO: change to use  ActivityMapper instead
        //List<ActivityEntity> activityList = mappingHelper.mapAsList(tjLogActivityDtoList, ActivityEntity.class);
        List<ActivityEntity> activityList = ActivityMapper.INSTANCE.mapList(tjLogActivityDtoList);


        activityRepository.saveAll(activityList);
    }

    public void addActivity(TJLogActivity tjLogActivity) {
        TJLogActivityDto tjLogActivityDto = buildActivityDto(tjLogActivity);
        // TODO : original code use mappingHelper
        // ActivityEntity activity = mappingHelper.map(tjLogActivityDto, ActivityEntity.class);
        ActivityEntity activity = ActivityMapper.INSTANCE.map(tjLogActivityDto);
        activityRepository.save(activity);
    }

    public CaseInfo getCase(String caseId) {
        CaseEntity cse = caseRepository.findByCaseId(caseId)
                .orElseThrow(() -> new NotFoundException("Case"));
        return CaseMapper.INSTANCE.convertToCaseInfo(cse);
    }

    private TJLogActivityDto buildActivityDto(TJLogActivity tjLogActivity) {
        TJLogActivityDesc tjLogActivityDesc = new TJLogActivityDesc();
        tjLogActivityDesc.setCaseID(checkNullData(tjLogActivity.getCaseId()));
        tjLogActivityDesc.setAppNum(checkNullData(tjLogActivity.getAppFormNo()));
        tjLogActivityDesc.setRefNum(checkNullData(tjLogActivity.getDocNo()));
        if (tjLogActivity.isApprove()) {
            setAuthIds(tjLogActivity.getApprovalInfo(), tjLogActivityDesc, tjLogActivity.getApprovalState());
        }
        String txnCode = tjLogActivity.getTxnCode().startsWith(TXN094) ? TXN094 : tjLogActivity.getTxnCode();
        tjLogActivityDesc.setTxnCode(checkNullData(txnCode));
        tjLogActivityDesc.setTxnName(checkNullData(env.getProperty(tjLogActivity.getTxnCode())));
        tjLogActivityDesc.setCustomerNum(checkNullData(tjLogActivity.getReferenceId()));
        tjLogActivityDesc.setDescription(checkNullData(getActivityDesc(tjLogActivity)));
        tjLogActivityDesc.setTxnRunningNum(checkNullData(activityRepository.nextVal(tjLogActivity.getBranchId())));
        tjLogActivityDesc.setAcctNo(checkNullData(tjLogActivity.getAccountNo()));
        tjLogActivityDesc.setProdCode(checkNullData(tjLogActivity.getProdCode()));
        tjLogActivityDesc.setBookbr(checkNullData(tjLogActivity.getBookbr()));

        TJLogActivityDto tjLogActivityDto = new TJLogActivityDto();
        tjLogActivityDto.setBranchId(tjLogActivity.getBranchId());
        tjLogActivityDto.setUserId(tjLogActivity.getEmployeeId());
        if (tjLogActivity.getTimestampCreated() != null) {
            tjLogActivityDto.setTimestampCreated(tjLogActivity.getTimestampCreated());
        } else {
            tjLogActivityDto.setTimestampCreated(new Timestamp(System.currentTimeMillis()));
        }
        tjLogActivityDto.setActivityJson(tjLogActivityDesc);

        return tjLogActivityDto;
    }

    private String getActivityDesc(TJLogActivity tjLogActivity) {
        String txnCode = tjLogActivity.getTxnCode();
        if (txnCode.startsWith(TXN094)) {
            txnCode = TXN094;
        }
        String desc = env.getProperty("DESC_" + txnCode);


        if ("Pass".equalsIgnoreCase(tjLogActivity.getWatchlistData())) {
            desc = desc.replace(", CID: ID_No, Title First_Name Last_Name, User staff ID: Employee_ID Supervisor staff ID: Supervisor_ID", "");
        }

        if (tjLogActivity.isForeigner() || "TXN100".equalsIgnoreCase(tjLogActivity.getTxnCode())) {
            desc = desc.replace("Title", checkNullData(tjLogActivity.getEngTitle()));
            desc = desc.replace("First_Name", checkNullData(tjLogActivity.getEngFirstName()));
            desc = desc.replace(" Middle_Name", checkNullMiddleName(tjLogActivity.getEngMiddleName()));
            desc = desc.replace("Last_Name", checkNullData(tjLogActivity.getEngLastName()));
            desc = desc.replace("ID_No|", checkNullData(tjLogActivity.getDocNo()) + ",");
            desc = desc.replace("ID_No", "" + checkNullData(tjLogActivity.getDocNo()));
        } else {
            desc = desc.replace("Title", checkNullData(tjLogActivity.getThaiTitle()));
            desc = desc.replace("First_Name", checkNullData(tjLogActivity.getThaiFirstName()));
            desc = desc.replace(" Middle_Name", checkNullMiddleName(tjLogActivity.getThaiMiddleName()));
            desc = desc.replace("Last_Name", checkNullData(tjLogActivity.getThaiLastName()));
            desc = desc.replace("ID_No|", checkNullData(tjLogActivity.getIdNo()) + ",");
            desc = desc.replace("ID_No", "" + checkNullData(tjLogActivity.getIdNo()));
        }

        desc = desc.replace("Email_ID", checkNullData(tjLogActivity.getEmailId()));
        desc = desc.replace("Field_Name|", checkNullData(tjLogActivity.getFieldName()) + ",");
        desc = desc.replace("Field_Name", checkNullData(tjLogActivity.getFieldName()));
        desc = desc.replace("Mobile_No|", checkNullData(tjLogActivity.getMobileNo()) + ",");
        desc = desc.replace("Mobile_No", checkNullData(tjLogActivity.getMobileNo()));
        desc = desc.replace("Ref_No", checkNullData(tjLogActivity.getRefNo()));
        desc = desc.replace("Account_No|", checkNullData(tjLogActivity.getAccountNo()) + ",");
        desc = desc.replace("Account_No", checkNullData(tjLogActivity.getAccountNo()));
        desc = desc.replace("Card_No", checkNullData(tjLogActivity.getCardNo()));
        desc = desc.replace("Fee_Type", checkNullData(tjLogActivity.getFeeType()));
        desc = desc.replace("Fee_Code", checkNullData(tjLogActivity.getFeeCode()));
        desc = desc.replace("Old_Value", checkNullData(tjLogActivity.getOldValue()));
        desc = desc.replace("New_Value", checkNullData(tjLogActivity.getNewValue()));
        desc = desc.replace("Answer_Y/N|", checkNullData(tjLogActivity.getConsentStatus()) + ",");
        desc = desc.replace("KYC_Level|", checkNullData(tjLogActivity.getKycLevel()) + ",");
        desc = desc.replace("T&C_Name", checkNullData(tjLogActivity.getTc()));
        desc = desc.replace("Reason_Code", checkNullData(tjLogActivity.getReasonCode()));
        desc = desc.replace("DOPA_Desc", checkNullData(tjLogActivity.getDopaDesc()));
        desc = desc.replace("PromptPay_ID", checkNullData(tjLogActivity.getPromptPayId()));
        desc = desc.replace("Face_Score", checkNullData(tjLogActivity.getFaceScore()));
        desc = desc.replace("Relative_Data|", checkNullData(tjLogActivity.getWatchlistPerson()) + ",");
        desc = desc.replace("Watchlist_Data", checkNullData(tjLogActivity.getWatchlistData()));
        desc = desc.replace("Account_Linked", checkNullData(tjLogActivity.getLinkedAccount()));
        desc = desc.replace("Version|", checkNullData(tjLogActivity.getConsentVersion()) + ",");
        desc = desc.replace("TAX_ID", checkNullData(tjLogActivity.getTc()));
        desc = desc.replace("Service_Name", checkNullData(tjLogActivity.getServiceName()));
        desc = desc.replace("ValidatedResult", checkNullData(tjLogActivity.getDeticaResult()));
        desc = desc.replace("UpdatedResult", checkNullData(tjLogActivity.getRiskResult()));
        desc = desc.replace("Condition_message", checkNullData(tjLogActivity.getCondition_message()));
        desc = desc.replace("Client_ID", checkNullData(tjLogActivity.getClientNumber()));
        desc = desc.replace("Employee_ID", checkNullData(tjLogActivity.getEmployeeId()) + ",");
        desc = desc.replace("consentStatus", checkNullData(tjLogActivity.getConsentStatus()));
        desc = desc.replace("consentVersion", checkNullData(tjLogActivity.getConsentVersion()));
        desc = desc.replace("consentType", checkNullData(tjLogActivity.getConsentType()));
        desc = desc.replace("consentUpdateDate", checkNullData(tjLogActivity.getConsentUpdateDate()));
        desc = desc.replace("granularity", checkNullData(tjLogActivity.getGranularity()));
        desc = desc.replace("productName", checkNullData(tjLogActivity.getProductName()));
        desc = desc.replace("feeCardType", checkNullData(tjLogActivity.getCardType()));
        desc = desc.replace("verifyStatus", checkNullData(tjLogActivity.getVerifyStatus()));
        desc = desc.replace("updatedStatus", checkNullData(tjLogActivity.getUpdateStatus()));
        desc = desc.replace("Card_Ref", checkNullData(tjLogActivity.getCardRef()));
        desc = desc.replace("OLD_ID", checkNullData(tjLogActivity.getOldDocNO()));
        desc = desc.replace("NEW_ID", checkNullData(tjLogActivity.getNewDocNO()));
        desc = desc.replace("DOB", checkNullData(tjLogActivity.getDOB()));
        desc = desc.replace("NationalityCode", checkNullData(tjLogActivity.getNationalityCode()));
        desc = desc.replace("Capture_Status", checkNullData(tjLogActivity.getFaceCaptureStatus()));
        desc = desc.replace("Send Email Status", checkNullData(tjLogActivity.getOtpEmailStatus()));
        desc = desc.replace("Email", checkNullData(tjLogActivity.getOtpEmailContactEmail()));
        desc = desc.replace("Red list result", checkNullData(tjLogActivity.getInternalRedlistFlag()));
        desc = desc.replace("Ref. Code", checkNullData(tjLogActivity.getOtpEmailRefCode()));
        desc = desc.replace("Verify OTP Status", checkNullData(tjLogActivity.getOtpEmailStatus()));
        desc = desc.replace("<Enroll_Flag>", checkNullData(tjLogActivity.getEnrollFlag()));
        desc = desc.replace("|<Face_Level_Desc>", checkNullData(tjLogActivity.getFaceLevelDesc()));

        if (tjLogActivity.isApprove()) {
            ApprovalInfo approvalInfo = tjLogActivity.getApprovalInfo().stream()
                    .findFirst()
                    .orElse(null);

            if (approvalInfo != null && approvalInfo.getApprovalDetails() != null) {
                ApprovalDetailsItem approvalDetails = approvalInfo.getApprovalDetails().stream()
                        .findFirst()
                        .orElse(null);

                if (approvalDetails != null) {
                    desc = desc.replace("Supervisor_ID", checkNullData(approvalDetails.getChecker()));
                }
            }
        }
        desc = desc.replace("DESC_TXN093", checkNullData(tjLogActivity.getTXN093()));

        return desc;
    }

    private void setAuthIds(List<ApprovalInfo> approvalInfos, TJLogActivityDesc tjLogActivityDesc, TJLogApproval approval) {

        ApprovalInfo apprInfo = approvalInfos.stream()
                .filter(it -> it.getFunctionCode().equals(approval.getFunctionCode()) && "A".equalsIgnoreCase(it.getApprovalStatus()))
                .findAny().orElse(null);
        if (apprInfo != null && approval.getApprovalRequired() == approval.getApprovalCount()) {
            tjLogActivityDesc.setAuthId1(apprInfo.getApprovalDetails().get(0).getChecker());
            if (approval.getApprovalRequired() == 2) {
                tjLogActivityDesc.setAuthId2(apprInfo.getApprovalDetails().get(1).getChecker());
            }
        }
    }

    private String checkNullData(String input) {
        return input != null ? input.trim() : "";
    }

    private String checkNullMiddleName(String middleName) {
        if ("".equalsIgnoreCase(middleName)) {
            return "";
        }
        return middleName != null ? " " + middleName.trim() : "";
    }
}
