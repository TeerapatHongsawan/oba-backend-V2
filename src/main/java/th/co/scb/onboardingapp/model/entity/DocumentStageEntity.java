package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "DOC_STG")
public class DocumentStageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "CUST_ID", columnDefinition = "TEXT")
    private String customerId;

    @Column(name = "ACCT_NO", columnDefinition = "TEXT")
    private String accountNumber;

    @Column(name = "DOC_TYPE", columnDefinition = "TEXT", nullable = false)
    private String documentType;

    @Column(name = "UPL_SSN_ID", columnDefinition = "TEXT", nullable = false)
    private String uploadSessionId;

    @Column(name = "CSE_ID", columnDefinition = "TEXT", nullable = false)
    private String caseId;

    @Column(name = "JS_ADDITIONAL_INFO", columnDefinition = "JSON")
    private String additionalInfo;

    @Column(name = "BRANCH_ID", columnDefinition = "TEXT", nullable = false)
    private String branchId;

    @Column(name = "STATUS", columnDefinition = "TEXT", nullable = false)
    private String status;

    @Column(name = "CHANNEL", columnDefinition = "TEXT", nullable = false)
    private String channel;

    @Column(name = "CRT_DT", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "UPT_DT")
    private LocalDateTime updatedDate;
    
}
