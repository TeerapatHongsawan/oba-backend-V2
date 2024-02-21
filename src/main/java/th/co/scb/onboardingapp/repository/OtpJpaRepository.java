package th.co.scb.onboardingapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.OtpEntity;

@Repository
public interface OtpJpaRepository extends JpaRepository<OtpEntity, String> {

}