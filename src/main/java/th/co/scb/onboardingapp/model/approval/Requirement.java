package th.co.scb.onboardingapp.model.approval;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Data
public class Requirement {
    private final Map<String, String> kyc;

    private Set<Boolean> addPhone;
    private Set<Boolean> specialFee;
    private Set<Boolean> ciChange;
    private Set<Boolean> newCustomer;
    private Set<Boolean> signature;
    private Set<Boolean> caOpen;
    private Set<Boolean> rtftWaive;

    private Set<Boolean> nameChange;
    private Set<Boolean> income;
    private Set<Boolean> titleChange;
    private Set<Boolean> typeChange;
    private Set<Boolean> ciDetail;
    private Set<Boolean> addressChange;
    private Set<Boolean> changePhone;
    private Set<Boolean> changeEmail;
    private Set<Boolean> changeAcctName;
    private Set<Boolean> acctName;
    private Set<Boolean> changeMailAddr;
    private Set<Boolean> changeAcctMail;
    private Set<Boolean> changeSignature;
    private Set<Boolean> convertPB;
    private Set<Boolean> uplift;

    public Requirement() {
        kyc = new LinkedHashMap<>();
        kyc.put("level1", null);
        kyc.put("level2", null);
        kyc.put("level3", null);
    }

    public void setKycLevel1(String value) {
        kyc.put("level1", value);
    }

    public void setKycLevel2(String value) {
        kyc.put("level2", value);
    }

    public void setKycLevel3(String value) {
        kyc.put("level3", value);
    }
}