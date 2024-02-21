package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.List;

@Data
public class WorkflowInfo {
    private String caseId;
    private List<Workflow> workflow;
    private List<WorkflowMoreAction> moreAction;
    private String status;
    private String hasEmail;
}
