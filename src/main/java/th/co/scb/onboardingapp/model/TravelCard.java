package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class TravelCard extends EcInfo {
    private String email;
    private DebitCard debitCard;
}
