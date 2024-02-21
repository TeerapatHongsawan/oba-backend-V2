package th.co.scb.onboardingapp.repository;

import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.ConsentContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


@Repository
public interface ConsentContentJpaRepository extends JpaRepository<ConsentContentEntity, String> {
    List<ConsentContentEntity> findByConsentTypeAndConsentVersionAndLinkAndVendor(String consentType, String version, String link, String vendor);

    List<ConsentContentEntity> findByConsentTypeAndConsentVersionAndLinkAndVendorAndLinkEN(String consentType, String version, String link, String vendor, String linkEN);
}
