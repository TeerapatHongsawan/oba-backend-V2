package th.co.scb.onboardingapp.model.common.predicate;


import th.co.scb.onboardingapp.service.FormBuilder;
import th.co.scb.onboardingapp.service.deposite.KYC101FormBuilder;

public class AppFormKYC101 implements AppFormBuilderPredicate{
    @Override
    public boolean buildForm(FormBuilder<?> formBuilder) {
        return formBuilder instanceof KYC101FormBuilder;
    }
}
