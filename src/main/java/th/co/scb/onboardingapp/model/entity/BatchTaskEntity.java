package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "BATCH_TASK")
public class BatchTaskEntity {

    public static final String PENDING_STATUS = "Pending";
    public static final String SUCCESS_STATUS = "Success";
    public static final String FAIL_STATUS = "Fail";
    public static final String RERUN_TYPE = "Rerun";
    public static final String ROLLBACK_RERUN_TYPE = "RollbackRerun";
    public static final String FAIL_RERUN_TYPE = "FailRerun";
    public static final String SERVICE_TYPE = "Service";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "CSE_ID", length = 32)
    private String caseId;

    @Column(name = "TASK_NAME", length = 48)
    private String taskName;

    @Column(name = "STATUS", length = 32)
    private String status;

    @Column(name = "TYPE", length = 16)
    private String type;

    @Column(name = "CRT_DT")
    private LocalDateTime createdDate;

    @Column(name = "UPD_DT")
    private LocalDateTime updatedDate;

    @Column(name = "ACCT_NO", length = 255)
    private String accountNumber;

    @Column(name = "REJECT_REASON", columnDefinition = "TEXT")
    private String rejectReason;

    @Column(name = "APPFORM_NO", length = 255)
    private String appformNo;

    @Column(name = "Run")
    private Integer run;
}


