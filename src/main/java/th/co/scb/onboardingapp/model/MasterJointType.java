package th.co.scb.onboardingapp.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MasterJointType {

    private String code;
    private String value;
    private String descriptionTh;
    private String descriptionEn;
}
