package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TOGGLE")
public class ToggleEntity {

    @Id
    @Column(name = "TOGGLE_ID", nullable = false)
    private String toggleId;

    @Column(name = "TOGGLE_NAME", length = 256, nullable = false)
    private String toggleName;

    @Column(name = "TOGGLE_VALUE", length = 1, nullable = false)
    private String toggleValue;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

}

