package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "RED_LIST")
@IdClass(RedListKey.class)
public class RedListEntity {

    @Id
    @Column(name = "CSE_ID", nullable = false, length = 32)
    private String caseId;

    @Id
    @Column(name = "EMAIL", nullable = false, length = 70)
    private String email;

    @Column(name = "RED_LIST_FLAG", nullable = false, length = 1)
    private String redListFlag;

    @Column(name = "CRT_DT", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdDate;
}
