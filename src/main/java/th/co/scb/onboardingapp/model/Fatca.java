package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class Fatca {
    private String fatcaStatus;
    private FatcaQuestionnaires questionnaires;
    private W8FormDetails w8FormDetails;
    private W9FormDetails w9FormDetails;
    private String cityOfBirth;
    private String countryOfBirth;
}

