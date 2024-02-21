package th.co.scb.onboardingapp.model;

import lombok.Data;
import th.co.scb.entapi.detica.model.DeticaValidationHitResult;
import th.co.scb.entapi.sales_services.model.Consent;
import th.co.scb.entapi.securities.model.AccountStatusResponse;

import javax.xml.crypto.KeySelector;
import java.util.List;

@Data
public class IdenDetail {
    private Fatca fatca;
    private Monitoring monitoring;
    private Consent marketingConsent;
    private Consent generalConsent;
    private DeticaValidationHitResult[] cdd;
    private String result;
    private Kyc kyc;
    private AccountStatusResponse securities;
    private Dopa dopa;
    private Purpose purpose;
    private List<BklAns> bklAns;
    private String statusCode;
    private String statusDesc;
    private List<ConsentData> data;
    private List<ConsentMaintenanceData> dataMaintenance;
    private UpliftNDID upliftNDID;
    private Crs crs;
    private String matchLevel;
    private String matchScore;
}