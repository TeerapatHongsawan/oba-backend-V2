package th.co.scb.onboardingapp.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "INDI_OTP")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OtpEntity {

    @Id
    @Column(name = "CSE_ID", nullable = false, length = 32)
    private String caseId;

    @Column(name = "MOBILE_NO", nullable = false, length = 10)
    private String mobileNo;

    @Column(name = "TOKEN", nullable = false, length = 10)
    private String token;

    @Column(name = "REFNO", nullable = false, length = 4)
    private String referenceNo;

    @Column(name = "DURATION", length = 5)
    private String duraiton;

    @Column(name = "CRT_DT")
    private Timestamp createdDate;

}
