package th.co.scb.onboardingapp.model.entity;


import lombok.Data;

@Data
public class GeneralParameterEntity {
    private String type;
    private String value;
    private boolean serverOnly;
}
