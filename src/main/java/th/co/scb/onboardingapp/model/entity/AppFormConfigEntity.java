package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "APPFORM_CONFIG")
public class AppFormConfigEntity {

    @Id
    private Integer ID;

    @Column(name = "FILENAME")
    private String fileName;

    @Column(name = "EFFECTIVE_DATE")
    private LocalDate effectTiveDate;

    @Column(name = "EXPIRE_DATE")
    private LocalDate expireDate;
}

