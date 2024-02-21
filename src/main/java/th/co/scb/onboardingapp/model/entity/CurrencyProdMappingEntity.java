package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import th.co.scb.onboardingapp.model.CurrencyProdMappingKey;


@Data
@Entity
@Table(name = "CURRENCY_PROD_MAPPING")
@IdClass(CurrencyProdMappingKey.class)
public class CurrencyProdMappingEntity {

    @Id
    @Column(name = "INDI_PROD_CODE", length = 8, nullable = false)
    private String indiProdCode;

    @Id
    @Column(name = "CURRENCYCODE", length = 4, nullable = false)
    private String currencyCode;

    @Column(name = "RESIDENT_TYPE", length = 1)
    private String residentType;

    @Column(name = "ACTIVE", length = 1)
    private String active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currencyCode", nullable = false)
    private CurrencyConfigEntity currencyConfigEntity;

}

