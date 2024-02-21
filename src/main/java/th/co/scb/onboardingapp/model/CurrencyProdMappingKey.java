package th.co.scb.onboardingapp.model;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class CurrencyProdMappingKey implements Serializable {
    private String indiProdCode;
    private String currencyCode;
}
