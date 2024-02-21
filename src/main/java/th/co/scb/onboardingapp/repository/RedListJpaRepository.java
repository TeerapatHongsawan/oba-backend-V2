package th.co.scb.onboardingapp.repository;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.RedListEntity;
import th.co.scb.onboardingapp.model.entity.RedListKey;
import java.util.List;

@Repository
public interface RedListJpaRepository extends JpaRepository<RedListEntity, RedListKey> {
    List<RedListEntity> findByCaseId(String caseId);

    @Transactional
    int deleteByCaseId(String caseId);
}