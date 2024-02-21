package th.co.scb.onboardingapp.model;

import lombok.Data;
import th.co.scb.entapi.external_agency_dopa.model.CardStatusOutManual;

@Data
public class Dopa {

    private CardStatusOutManual result;
    private String status;
}
