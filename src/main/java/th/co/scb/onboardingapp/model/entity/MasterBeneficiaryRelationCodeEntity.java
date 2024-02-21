package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "MST_BENE_REL_CD")
public class MasterBeneficiaryRelationCodeEntity {

    @Id
    @Column(name = "VALUE_CD", length = 2, nullable = false)
    private String valueCode;

    @Column(name = "VALUE_TDESC", length = 100)
    private String valueThaiDesc;

    @Column(name = "VALUE_EDESC", length = 100)
    private String valueEnglishDesc;

    @Column(name = "CBS_VALUE_CD", length = 2)
    private String cbsValueCode;

}

