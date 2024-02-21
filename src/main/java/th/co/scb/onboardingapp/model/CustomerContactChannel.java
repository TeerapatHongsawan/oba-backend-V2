package th.co.scb.onboardingapp.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.scb.entapi.individuals.model.ContactChannel;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerContactChannel extends ContactChannel {
    private String txnType;
    private boolean deleted;
    private Set<ServiceDescription> dependentServices;
    private String contactVerifyIndicator;
    private String internalVerifyEmailFlag;
    private String internalRedlistFlag;
}
