package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "WORKFLOW")
public class WorkflowEntity {

    public static final String COMPLETE = "Complete";
    public static final String FAIL = "Fail";
    public static final String INITIAL = "Initial";
    public static final String WARNING = "Warning";

    @Id
    @Column(name = "CSE_ID", length = 32)
    private String caseId;

    @Column(name = "STATUS", length = 45)
    private String status;

    @Column(name = "WORKFLOW")
    private String workflow;

    @Column(name = "MORE_ACTION")
    private String moreAction;

    @Column(name = "HAS_EMAIL", length = 10)
    private String hasEmail;

    @Column(name = "CRT_DT")
    private LocalDateTime createdDate;

    @Column(name = "UPD_DT")
    private LocalDateTime updatedDate;
}
