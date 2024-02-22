package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "MST_JOINT_TYPE")
public class MasterJointTypeEntity {

    @Id
    private String code;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "DESCRIPTION_TH")
    private String descriptionTh;

    @Column(name = "DESCRIPTION_EN")
    private String descriptionEn;

    @Column(name = "status")
    private String status;
}
