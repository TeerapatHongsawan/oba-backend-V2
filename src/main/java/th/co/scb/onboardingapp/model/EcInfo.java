package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EcInfo {
    private List<EcWorkflow> ecWorkflow;

    @JsonProperty("status")
    private String status;

    @JsonProperty("ecApprovalId")
    private String ecApprovalId;

    @JsonProperty("statusUpdateTime")
    private String statusUpdateTime;
}
