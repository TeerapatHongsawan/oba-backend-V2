package th.co.scb.onboardingapp.model.common.predicate;

import th.co.scb.onboardingapp.service.FormBuilder;
import th.co.scb.onboardingapp.service.deposite.KYC101FormBuilder;

public interface AppFormBuilderPredicate {
    boolean buildForm(FormBuilder<?> formBuilder);
}
