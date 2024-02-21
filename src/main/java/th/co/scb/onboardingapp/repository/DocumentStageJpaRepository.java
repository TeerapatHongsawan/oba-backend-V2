package th.co.scb.onboardingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.DocumentStageEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentStageJpaRepository extends JpaRepository<DocumentStageEntity, String> {

    Optional<DocumentStageEntity> findByCaseIdAndDocumentType(String caseId, String docType);

    Optional<List<DocumentStageEntity>> findByCaseIdAndChannel(String caseId, String channel);

    @Query(value = "select getBranchNextval(?1, 'DOC_SEQ') as NEXT_VAL", nativeQuery = true)
    String nextVal(String branchId);
}
