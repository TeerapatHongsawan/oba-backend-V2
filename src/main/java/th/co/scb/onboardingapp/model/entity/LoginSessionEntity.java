package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "LOGIN_SESSION")
public class LoginSessionEntity {

    @Id
    @Column(name = "EMPLOYEE_ID", length = 16)
    private String employeeId;

    @Column(name = "LAST_ACTIVITY_TIME", length = 45)
    private LocalDateTime lastActivityTime;

    @Column(name = "DEVICE_ID", length = 45)
    private String deviceId;

    @Column(name = "STATUS", length = 45)
    private String status;

    @Column(name = "APP_NAME", length = 45)
    private String appName;

    @Column(name = "TOKEN", length = 400)
    private String token;
}