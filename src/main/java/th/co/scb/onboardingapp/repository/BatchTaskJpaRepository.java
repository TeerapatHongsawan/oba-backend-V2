package th.co.scb.onboardingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.BatchTaskEntity;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BatchTaskJpaRepository extends JpaRepository<BatchTaskEntity, Integer> {
    List<BatchTaskEntity> findByTaskNameAndStatus(String taskName, String status);
    List<BatchTaskEntity> findByTypeAndStatus(String type, String status);
    List<BatchTaskEntity> findByTypeAndStatusAndTaskName(String type, String status, String taskName);
    @Query(value = "SELECT * FROM BATCH_TASK " +
            "WHERE TYPE = :type AND STATUS =:status AND CRT_DT BETWEEN :reportDate AND :nextReportDate  AND TASK_NAME=:taskName" , nativeQuery = true)
    List<BatchTaskEntity> findByTypeAndStatusAndTaskNameAndDate(@Param("type") String type
            , @Param("status") String status
             , @Param("taskName") String taskName
            , @Param("reportDate") LocalDateTime reportDate
            , @Param("nextReportDate") LocalDateTime nextReportDate);
    @Query(value = "SELECT * FROM BATCH_TASK " +
            "WHERE TYPE = :type AND DATE(CRT_DT) = CURDATE() ", nativeQuery = true)
    List<BatchTaskEntity> findByTypeAndCurrentDate(@Param("type") String type);

    BatchTaskEntity findByCaseId(String caseId);
}
