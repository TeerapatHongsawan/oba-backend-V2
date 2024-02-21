package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FaceCompareMatchLevel {

    private String matchId;
    private String matchLevel;
    private String matchValue;
    private String vendor;
    private String createDate;
    private String updateDate;

}