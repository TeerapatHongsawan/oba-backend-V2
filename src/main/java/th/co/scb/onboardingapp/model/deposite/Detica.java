package th.co.scb.onboardingapp.model.deposite;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Detica {
    private String deticaAlertType;
    private String deticaAlertMessage;
    private String deticaAlertScore;
}
