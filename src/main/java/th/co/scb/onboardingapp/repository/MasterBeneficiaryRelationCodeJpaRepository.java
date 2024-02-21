package th.co.scb.onboardingapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.MasterBeneficiaryRelationCodeEntity;

@Repository
public interface MasterBeneficiaryRelationCodeJpaRepository extends JpaRepository<MasterBeneficiaryRelationCodeEntity, String> {
}