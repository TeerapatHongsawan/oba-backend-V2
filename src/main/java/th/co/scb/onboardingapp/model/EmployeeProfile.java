package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProfile {
    @JsonProperty("SAMEmail")
    private String samEmail;

    @JsonProperty("SAMId")
    private String samId;

    @JsonProperty("SCBEmail")
    private String scbEmail;
    
    private String empFirstNameEN;
    private String empFirstNameTH;
    private String empId;
    private String empLastNameEN;
    private String empLastNameTH;
    private String positionName;
    private Boolean isPersonalBanker;
    private String type;
}
