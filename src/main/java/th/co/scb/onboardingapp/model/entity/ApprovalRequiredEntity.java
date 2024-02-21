package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "APPROVAL_REQUIRE")
public class ApprovalRequiredEntity {

    @Id
    @Column(name = "FUNCTION_CODE")
    private String functionCode;

    @Column(name = "APPROVAL_REQUIRED")
    private int approvalRequired;

    @Column(name = "SSC_REQUIRED")
    private int sscRequired;

    @Column(name = "BRANCH_REQUIRED")
    private int branchRequired;
}
