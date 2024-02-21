package th.co.scb.onboardingapp.service.common;

import lombok.extern.slf4j.Slf4j;
import th.co.scb.onboardingapp.model.common.AppFormDocumentType;
import th.co.scb.onboardingapp.model.common.predicate.AppFormBuilderPredicate;
import th.co.scb.onboardingapp.model.common.predicate.AppFormKYC101;


@Slf4j
public class AppFormPredicateService {

    private AppFormPredicateService() {
    }

    public static AppFormBuilderPredicate filterForm(AppFormDocumentType appFormDocumentType) {

        log.debug(String.format("Get AppForm Predicate : %s", appFormDocumentType.name()));

        switch (appFormDocumentType) {
            case P029_KYC_101:
                return new AppFormKYC101();
            default:
                return null;
        }
    }
}