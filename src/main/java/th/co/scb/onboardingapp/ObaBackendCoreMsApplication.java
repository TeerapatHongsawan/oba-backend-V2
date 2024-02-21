package th.co.scb.onboardingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"th.co.scb.onboardingapp"})
@EntityScan(basePackages = {"th.co.scb.onboardingapp.model.entity"})
@EnableJpaRepositories(basePackages = {"th.co.scb.onboardingapp.repository"})
public class ObaBackendCoreMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObaBackendCoreMsApplication.class, args);
	}

}
