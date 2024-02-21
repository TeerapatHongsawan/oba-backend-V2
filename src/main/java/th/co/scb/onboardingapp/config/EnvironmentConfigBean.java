package th.co.scb.onboardingapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EnvironmentConfigBean {

    private Environment environment;

    public EnvironmentConfigBean(@Autowired Environment environment) {
        this.environment = environment;
    }

    public String getValue(String key) {
        String value = environment.getProperty(key);
        log.debug("[getValue : key,value]={},{}", key, value);
        return value;
    }
}
