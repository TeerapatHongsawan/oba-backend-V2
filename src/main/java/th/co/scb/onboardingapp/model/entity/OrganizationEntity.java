package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "ORGANIZATION")
public class OrganizationEntity {

    @Id
    @Column(name = "OC_CODE", length = 6)
    private String ocCode;

    @Column(name = "OC_NAME_TH", length = 256)
    private String ocNameTh;

    @Column(name = "OC_NAME_EN", length = 256)
    private String ocNameEn;
}
