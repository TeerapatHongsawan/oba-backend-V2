package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class ObjectiveInfo {
    private String key;
    private String objectiveValue;
    private String objectiveChangedValue;
    private boolean changed;
}
