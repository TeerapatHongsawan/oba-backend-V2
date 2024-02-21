package th.co.scb.onboardingapp.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.ActivityEntity;


@Repository
public interface ActivityJpaRepository extends JpaRepository<ActivityEntity, Integer> {
    @Query(value = "select getBranchNextval(?1, 'TJ_LOG') as NEXT_VAL", nativeQuery = true)
    String nextVal(String branchId);


    @Modifying
    @Transactional
    @Query(value = "delete  FROM ACTIVITY WHERE DATE(TIMESTAMP_CREATED) < :timeStamp AND RECORD_TYPE = 'TJ_LOG'", nativeQuery = true)
    int deleteActivity(@Param("timeStamp") String timeStamp);

}
