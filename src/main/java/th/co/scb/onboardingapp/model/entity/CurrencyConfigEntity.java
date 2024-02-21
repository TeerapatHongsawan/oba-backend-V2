package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CURRENCY_CONFIG")
public class CurrencyConfigEntity {

    @Id
    @Column(name = "CURRENCYCODE", length = 4, nullable = false)
    private String currencyCode;

    @Column(name = "CURRENCYDESC", length = 5)
    private String currencyDesc;

    @Column(name = "SEQ", length = 11)
    private int seq;

    @Column(name = "CURR_NAME_EN", length = 200)
    private String currNameEn;

    @Column(name = "CURR_NAME_TH", length = 200)
    private String currNameTh;

    @Column(name = "ACTIVE", length = 1)
    private String active;

}

