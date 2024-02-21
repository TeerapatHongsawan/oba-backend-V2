package th.co.scb.onboardingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.MasterJointTypeEntity;


@Repository
public interface CustommerJointInfoRepository extends JpaRepository<MasterJointTypeEntity,String> {
}
