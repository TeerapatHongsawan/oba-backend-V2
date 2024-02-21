package th.co.scb.onboardingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.LicenseEntity;
import th.co.scb.onboardingapp.model.entity.LicenseKey;

import java.time.LocalDateTime;
import java.util.List;

@Repository
    public interface LicenseJpaRepository extends JpaRepository<LicenseEntity, LicenseKey> {
    List<LicenseEntity> findByEmployeeId(String empId);
    List<LicenseEntity> findByEmployeeIdAndExpiryDateGreaterThanEqual(String empId, LocalDateTime dateTime);

    @Query(value = "SELECT * FROM LICENSE " +
            "WHERE EMPLOYEE_ID = :empId ORDER BY EXPIRY_DATE , LICENSE_NUMBER DESC", nativeQuery = true)
    List<LicenseEntity> findByEmployeeIdByOrderByExpiryDateDescAndOrderByLicenseNumberDesc(@Param("empId") String empId);
}
