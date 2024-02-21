package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TJLogActivityDto {
    private String branchId;
    private String userId;
    private Timestamp timestampCreated;
    @JsonProperty("activityJson")
    private TJLogActivityDesc activityJson;
    private final String recordType = "TJ_LOG";
}

