package th.co.scb.onboardingapp.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.LoginSessionEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface LoginSessionJpaRepository extends JpaRepository<LoginSessionEntity, String> {


    //update when validate
    @Modifying
    @Transactional
    @Query(value = "update LOGIN_SESSION \n" +
            " set LAST_ACTIVITY_TIME = :ldt \n" +
            " where EMPLOYEE_ID = :employeeId and APP_NAME = :appName" , nativeQuery = true)
    void updateDataValidateLoginSession
    (@Param("employeeId") String employeeId,
     @Param("ldt") LocalDateTime ldt,
     @Param("appName") String appName);



}