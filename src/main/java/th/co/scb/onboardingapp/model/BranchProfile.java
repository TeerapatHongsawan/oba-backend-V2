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
public class BranchProfile {
    @JsonProperty("SAMOcCode")
    private String samOcCode;

    @JsonProperty("SCBOcCode")
    private String scbOcCode;

    @JsonProperty("SCBOcNameEN")
    private String scbOcNameEN;

    @JsonProperty("SCBOcNameTH")
    private String scbOcNameTH;

    private String branchId;
    private String branchNameEN;
    private String branchNameTH;
}
