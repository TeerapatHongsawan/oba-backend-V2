package th.co.scb.onboardingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.FaceCompareMatchLevelEntity;

import java.util.List;

@Repository
public interface FaceCompareMatchLevelJpaRepository extends JpaRepository<FaceCompareMatchLevelEntity, String> {
    FaceCompareMatchLevelEntity findByMatchLevel(String matchLevel);
    List<FaceCompareMatchLevelEntity> findByMatchValue(String matchValue);
}