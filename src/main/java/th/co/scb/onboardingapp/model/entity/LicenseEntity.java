package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "LICENSE")
@IdClass(LicenseKey.class)
public class LicenseEntity {

    @Column(name = "EMPLOYEE_ID", nullable = false, length = 6)
    private String employeeId;

    @Id
    @Column(name = "LICENSE_TYPE", nullable = false, length = 255)
    private String licenseType;

    @Id
    @Column(name = "LICENSE_NUMBER", nullable = false, length = 20)
    private String licenseNumber;

    @Column(name = "EXPIRY_DATE")
    private LocalDateTime expiryDate;

}