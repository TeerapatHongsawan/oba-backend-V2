package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
 public class Scope {
    private Display display;
    private String key;
    private String value;
    private String originalValue;
    private boolean changed;
}