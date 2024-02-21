package th.co.scb.onboardingapp.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


@Profile({"dev","qa","qaNext","sit","uat","ps","pt","prod"})
@Slf4j
@Service
public class CommonTransit {

    //TODO: To be implemented

//    @Autowired
//    private VaultTemplate vaultTemplate;

//    public String decryptText(String cipherText) {
//        VaultTransitOperations transitOperations = vaultTemplate.opsForTransit();
//
//        // Decrypt
//        String plaintext = transitOperations.decrypt(CommonConstants.TRANSIT_PATH, cipherText);
//        return plaintext;
//    }
}
