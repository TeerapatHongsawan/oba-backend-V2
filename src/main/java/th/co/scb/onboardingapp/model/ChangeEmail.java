package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class ChangeEmail extends BaseCommand {
    private String email;
}
