package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.helper.CipherHelper;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Service
public class FaceTechSecurityService {

    public static final int AES_AUTHENTICATION_TAG_LENGTH = 128;
    public static final String CIPHER_CKM_AES_GCM = "AES/GCM/NoPadding";

    public String encryptImage(SecretKey key, byte[] cipherText, byte[] iv) {
        byte[] encryptText = null;
        try {
            Cipher cipher = CipherHelper.getInstance(CIPHER_CKM_AES_GCM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(AES_AUTHENTICATION_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
            encryptText = cipher.doFinal(cipherText);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Base64.getEncoder().encodeToString(encryptText);
    }

    public byte[] generateIv() {
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public SecretKey convertStringToSecretKey(String encodedKey) {
        return new SecretKeySpec(Base64.getDecoder().decode(encodedKey), "AES");
    }
}