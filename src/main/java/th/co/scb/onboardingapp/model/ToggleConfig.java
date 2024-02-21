package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ToggleConfig {

    private String toggleId;
    private String toggleName;
    private String toggleValue;
    private String createDate;
    private String updateDate;

}