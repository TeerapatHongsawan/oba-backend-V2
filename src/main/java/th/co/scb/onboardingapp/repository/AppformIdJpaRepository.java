package th.co.scb.onboardingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.DocumentStageEntity;

@Repository
public interface AppformIdJpaRepository extends JpaRepository<DocumentStageEntity, String> {

    @Query(value = "select getBranchNextval(?1, 'APPFORM_SEQ') as NEXT_VAL", nativeQuery = true)
    String nextProductAppFormID(String branchId);

    @Query(value = "select getBranchNextval(?1, 'SERVICE_SEQ') as NEXT_VAL", nativeQuery = true)
    String nextServiceAppFormID(String branchId);
}
