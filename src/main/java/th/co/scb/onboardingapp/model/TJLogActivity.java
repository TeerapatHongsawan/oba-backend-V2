package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class TJLogActivity {
    //common info
    private String txnCode;

    //Other info
    private String fieldName;
    private String oldValue;
    private String newValue;
    private String emailId;
    private String idNo;
    private String consentStatus;
    private String consentType;
    private String consentUpdateDate;
    private String consentVersion;
    private String granularity;
    private String updateStatus; // chaiyo re issue update card status
    private String accountNo;
    private String prodCode;
    private String cardNo;
    private String cardRef;
    private String feeType;
    private String refNo;
    private String mobileNo;
    private String kycLevel;
    private String reasonCode;
    private String dopaDesc;
    private String promptPayId;
    private String faceScore;
    private String tc;
    private boolean approve = false;
    private TJLogApproval approvalState;
    private int eSubRank;
    private String serviceName;

    //From api-proxy-service
    private String watchlistPerson;
    private String watchlistData;
    private String linkedAccount;

    //required CaseInfo
    private String caseId;
    private String branchId;
    private String bookbr;
    private String employeeId;
    private String referenceId;
    private String appFormNo;
    private Timestamp timestampCreated;
    private List<ApprovalInfo> approvalInfo;
    private String docNo;//manually set from CustomerInfo
    private String thaiTitle;//manually set from CustomerInfo
    private String thaiFirstName;//manually set from CustomerInfo
    private String thaiMiddleName;//manually set from CustomerInfo
    private String thaiLastName;//manually set from CustomerInfo
    private String condition_message;//manually set from CustomerInfo
    private String riskResult;
    private String deticaResult;
    private String ClientNumber;

    private String TXN093;//For convert epassbook

    // for verify card type
    private String productName;
    private String cardType;
    private String cardSubType;
    private String VerifyStatus;

    // chai yo
    private String feeCode;

    // foreigner / alien
    private String engTitle;
    private String engFirstName;
    private String engMiddleName;
    private String engLastName;
    private String oldDocNO;
    private String newDocNO;
    private String DOB;
    private String nationalityCode;
    private String faceCaptureStatus;
    private boolean isForeigner = false;
    private String enrollFlag;
    private String faceLevelDesc;

    // for OTP email
    private String otpEmailContactEmail;
    private String otpEmailRefCode;
    private String otpEmailStatus;

    // for customer contactchannel
    private String contactNumber;
    private String internalRedlistFlag;



    public void setFieldValues(String fieldName, String oldValue, String newValue){
        this.setFieldName(fieldName);
        this.setOldValue(oldValue);
        this.setNewValue(newValue);
    }
}
