package th.co.scb.onboardingapp.helper;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class CipherHelper {
    private CipherHelper() {
    }

    public static Cipher getInstance(String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance(algorithm);
    }
}
