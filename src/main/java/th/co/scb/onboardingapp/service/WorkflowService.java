package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.WorkflowInfo;
import th.co.scb.onboardingapp.model.entity.WorkflowEntity;
import th.co.scb.onboardingapp.repository.WorkflowJpaRepository;
import th.co.scb.onboardingapp.service.submission.WorkflowStep;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class WorkflowService {

    @Autowired
    WorkflowJpaRepository workflowRepository;

    @Autowired
    MappingHelper mappingHelper;

    public WorkflowInfo getWorkflow(String caseId) {
        WorkflowEntity workflow = workflowRepository.findById(caseId).orElse(null);
        if (workflow == null) {
            workflow = new WorkflowEntity();
            workflow.setStatus("Pending");
            workflow.setCaseId(caseId);
        }
        return mappingHelper.map(workflow, WorkflowInfo.class);
    }

    public void insertWorkFlow(CaseInfo caseInfo, List<WorkflowStep> steps, LocalDateTime startTime) {
        WorkflowEntity workflow = new WorkflowEntity();
        workflow.setCaseId(caseInfo.getCaseId());
        workflow.setWorkflow(mappingHelper.map(steps, String.class));
        workflow.setCreatedDate(startTime);
        workflow.setUpdatedDate(LocalDateTime.now());
        workflow.setStatus(WorkflowEntity.INITIAL);

        for (WorkflowStep workflowStep : steps) {
            if (workflowStep.isHardStop()) {
                if (WorkflowStep.ERROR.equals(workflowStep.getStatus())) {
                    workflow.setStatus(WorkflowEntity.FAIL);
                    break;
                }
            } else {
                if (WorkflowStep.ERROR.equals(workflowStep.getStatus())) {
                    workflow.setStatus(WorkflowEntity.WARNING);
                }
            }
        }

        if (WorkflowEntity.INITIAL.equals(workflow.getStatus())) {
            workflow.setStatus(WorkflowEntity.COMPLETE);
        }

        workflowRepository.save(workflow);
    }
}
