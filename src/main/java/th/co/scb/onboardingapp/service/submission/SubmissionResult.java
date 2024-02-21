package th.co.scb.onboardingapp.service.submission;

import lombok.Data;

@Data
public class SubmissionResult {
    private SubmissionState state;
    private Exception ex;
    private WorkflowStep step;
}
