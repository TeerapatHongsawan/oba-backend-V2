package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Approver {

    private String name;
    private AuthorizedLevel approverLevel;

}