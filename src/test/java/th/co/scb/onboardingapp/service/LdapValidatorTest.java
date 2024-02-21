package th.co.scb.onboardingapp.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import th.co.scb.onboardingapp.exception.UnauthorizedException;
import th.co.scb.onboardingapp.utility.MockModel;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LdapValidatorTest {
    @Autowired
    private LdapValidator ldapValidator;

    MockModel mockModel = new MockModel();

    @Test
    public void shouldSuccessWhenUseValidate() {
        ldapValidator.validate("s99999", "scb1234!");
    }

    @Test(expected = UnauthorizedException.class)
    public void shouldFailWhenUseValidateAndUsernameIsEmpty() {
        ldapValidator.validate("", "scb1234!");
    }

    @Test(expected = UnauthorizedException.class)
    public void shouldFailWhenUseValidateAndPasswordInvalid() {
        ldapValidator.validate("s99999", "1150!");
    }
}
