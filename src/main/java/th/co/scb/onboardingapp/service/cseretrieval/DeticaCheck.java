package th.co.scb.onboardingapp.service.cseretrieval;

import org.apache.commons.collections.CollectionUtils;
import th.co.scb.entapi.detica.model.DeticaValidationHitResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeticaCheck {
    public boolean worldCheck(DeticaValidationHitResult[] hitResults) {
        List<String> arrayListWorldCheck = new ArrayList<>();
        arrayListWorldCheck.add("Hit on WORLD CHECK NON-UN SANCTIONS GROUP LIST");
        arrayListWorldCheck.add("Hit on WORLD CHECK UN SANCTIONS GROUP LIST");
        return processCheckResult(hitResults, arrayListWorldCheck);
    }

    public boolean checkAMLO(DeticaValidationHitResult[] hitResults) {


        List<String> arrayListWorldCheck = new ArrayList<>();
        arrayListWorldCheck.add("Hit on AMLO High Risk List");


        return processCheckResult(hitResults, arrayListWorldCheck);
    }

    public boolean checkAMLOFreeze(DeticaValidationHitResult[] hitResults) {

        List<String> arrayListWorldCheck = new ArrayList<>();
        arrayListWorldCheck.add("Hit on AMLO Freeze04 List");
        arrayListWorldCheck.add("Hit on AMLO Freeze05 List");
        return processCheckResult(hitResults, arrayListWorldCheck);
    }

    private boolean processCheckResult(DeticaValidationHitResult[] hitResults, List<String> arrayListWorldCheck) {
        boolean check = false;
        if (hitResults != null) {
            List<DeticaValidationHitResult> deticaValidationHitResultList = null;
            deticaValidationHitResultList = Arrays.asList(hitResults);
            if (!CollectionUtils.isEmpty(deticaValidationHitResultList)) {
                for (DeticaValidationHitResult deticaValidationHitResult : deticaValidationHitResultList) {
                    String hitWorldCheckMessage = deticaValidationHitResult.getMessage();
                    check = arrayListWorldCheck.contains(hitWorldCheckMessage);
                    if (check) {
                        break;
                    }
                }
            }
        }
        return check;
    }
}
