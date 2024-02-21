package th.co.scb.onboardingapp.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.CaseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface CaseJpaRepository extends JpaRepository<CaseEntity, Integer> {

    Optional<CaseEntity> findByCaseId(String id);

    Optional<CaseEntity> findByAppFormNo(String appformno);

    List<CaseEntity> findByCreatedDateBetweenAndBranchId
            (LocalDateTime reportDate, LocalDateTime nextReportDate, String branchId);

    List<CaseEntity> findByCreatedDateBetweenOrderByBranchId
            (LocalDateTime reportDate, LocalDateTime nextReportDate);

    List<CaseEntity> findByCompletedDateBetweenAndBranchIdAndCaseStatus
            (LocalDateTime reportDate, LocalDateTime nextReportDate, String branchId, String status);

    List<CaseEntity> findByCompletedDateBetweenAndCaseStatusOrderByBranchId
            (LocalDateTime reportDate, LocalDateTime nextReportDate, String status);

    List<CaseEntity> findByCompletedDateBetweenOrderByBranchId
            (LocalDateTime reportDate, LocalDateTime nextReportDate);

    List<CaseEntity> findByCompletedDateBetweenAndBookingBranchIdAndCaseStatusAndBranchIdNot
            (LocalDateTime reportDate, LocalDateTime nextReportDate, String bookingBranchId, String status, String branchId);

    List<CaseEntity> findByCompletedDateBetweenAndBranchIdAndCaseStatusAndEmployeeId
            (LocalDateTime reportDate, LocalDateTime nextReportDate, String branchId, String status, String employeeId);

    List<CaseEntity> findByCompletedDateBetweenAndBookingBranchIdAndCaseStatusAndEmployeeIdAndBranchIdNot
            (LocalDateTime reportDate, LocalDateTime nextReportDate, String branchId, String status, String employeeId, String branchId2);

    List<CaseEntity> findByCompletedDateBetweenAndBranchId
            (LocalDateTime reportDate, LocalDateTime nextReportDate, String branchId);

    List<CaseEntity> findByCompletedDateBetweenAndBranchIdAndEmployeeId
            (LocalDateTime reportDate, LocalDateTime nextReportDate, String branchId, String employeeId);

    @Query(value = "SELECT * FROM CSE WHERE CRT_DT BETWEEN :createdDate AND :nextCreatedDate AND CSE_ST_CD = :status " +
            "AND (JS_PROD_INFO LIKE '%cardReferenceNo%' OR JS_CUST_INFO LIKE '%cardReferenceNo%')", nativeQuery = true)
    List<CaseEntity> getCaseInfoByCreatedDateBetweenAndLikeCardReferenceNo(@Param("createdDate") String createdDate, @Param("nextCreatedDate") String nextCreatedDate, @Param("status") String status);

    @Query(value = "SELECT * FROM CSE " +
            "WHERE EMP_ID = :empId AND CSE_ST_CD = :cseStatusCode AND DATE(CRT_DT) = CURRENT_DATE " +
            "AND BRANCH_ID = :branchId", nativeQuery = true)
    List<CaseEntity> getApprovedCaseList(@Param("empId") String empId, @Param("cseStatusCode") String cseStatusCode, @Param("branchId") String branchId);

    @Query(value = "SELECT * FROM CSE " +
            "WHERE EMP_ID = :empId AND CSE_ST_CD = :cseStatusCode AND DATE(CRT_DT) = CURRENT_DATE " +
            "AND BRANCH_ID = :branchId " +
            "AND CSE_ID = :caseId", nativeQuery = true)
    List<CaseEntity> getChaiYoApprovedCaseList(@Param("empId") String empId, @Param("cseStatusCode") String cseStatusCode, @Param("branchId") String branchId, @Param("caseId") String caseId);

    @Query(value = "SELECT APPFORM_NO FROM CSE WHERE CSE_ID = :caseId", nativeQuery = true)
    String getAppFormNoByCaseId(@Param("caseId") String caseId);

    @Query(value = "SELECT CAST(JS_DMS_INFO AS TEXT) AS JS_DMS_INFO FROM CSE WHERE CSE_ID = :caseId", nativeQuery = true)
    String getDocumentInfo(@Param("caseId") String caseId);

    @Query(value = "SELECT CAST(JS_CUST_INFO AS TEXT) AS JS_CUST_INFO FROM CSE WHERE CSE_ID = :caseId", nativeQuery = true)
    String getCustomerInfo(@Param("caseId") String caseId);

    @Query(value = "SELECT CAST(JS_CUST_INFO AS TEXT) AS JS_CUST_INFO FROM CSE WHERE APPFORM_NO = :appform_no", nativeQuery = true)
    String getCustomerInfoByAppForm(@Param("appform_no") String appform_no);

    @Query(value = "SELECT CAST(JS_PROD_INFO AS TEXT) AS JS_PROD_INFO FROM CSE WHERE CSE_ID = :caseId", nativeQuery = true)
    String getProductInfo(@Param("caseId") String caseId);

    @Query(value = "SELECT CAST(JS_PAY_INFO AS TEXT) AS JS_PAY_INFO FROM CSE WHERE CSE_ID = :caseId", nativeQuery = true)
    String getPaymentInfo(@Param("caseId") String caseId);

    @Query(value = "SELECT CAST(JS_APPR_INFO AS TEXT) AS JS_APPR_INFO FROM CSE WHERE CSE_ID = :caseId", nativeQuery = true)
    String getApprovalInfo(@Param("caseId") String caseId);

    @Query(value = "SELECT CAST(JS_ADDITIONAL_INFO AS TEXT) AS JS_ADDITIONAL_INFO FROM CSE WHERE CSE_ID = :caseId", nativeQuery = true)
    String getAdditionalCaseInfo(@Param("caseId") String caseId);

    @Query(value = "SELECT A.* " +
            "FROM CSE A INNER JOIN CARD_REF C ON A.CSE_ID=C.CSE_ID " +
            "WHERE A.CSE_ST_CD = 'ONBD' AND C.CARDREF = :cardRef", nativeQuery = true)
    List<CaseEntity> getCaseInfoByCardRef(@Param("cardRef") String cardRef);

    int countByCompletedDateBetweenAndCaseStatus(LocalDateTime reportDate, LocalDateTime nextReportDate, String status);

    List<CaseEntity> findByCompletedDateBetweenAndCaseStatus(LocalDateTime reportDate, LocalDateTime nextReportDate, String status, Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM CSE C INNER JOIN ( SELECT CSE_ID from BATCH_TASK where STATUS = :status group by CSE_ID ) B " +
            "ON C.CSE_ID = B.CSE_ID WHERE C.COMPL_DT BETWEEN :reportDate AND :nextReportDate ", nativeQuery = true)
    List<CaseEntity> getCaseInfoByFailedBatchTask(@Param("reportDate") LocalDateTime reportDate,
                                                  @Param("nextReportDate") LocalDateTime nextReportDate,
                                                  @Param("status") String status);

    @Query(value = "SELECT * " +
            "FROM CSE " +
            "WHERE COMPL_DT BETWEEN :reportDate AND :nextReportDate " +
            "AND CSE_ST_CD = 'ONBD' " +
            "AND JSON_EXTRACT(JS_ADDITIONAL_INFO, '$.empProfile.type') =:employeeType"
            , nativeQuery = true)
    List<CaseEntity> getCaseInfoByCompletedDateBetweenAndEmployeeType(
            @Param("reportDate") LocalDateTime reportDate,
            @Param("nextReportDate") LocalDateTime nextReportDate,
            @Param("employeeType") String employeeType);

    @Query(value = "SELECT JS_DMS_INFO->'$[*].uploadSessionId' AS UPLSSN from CSE " +
            " where CSE_ST_CD in ('CLSD','REJD','OPEN')" +
            " and JS_DMS_INFO->'$[*].uploadSessionId' is not null and DATE(CRT_DT) = :date limit 500"
            , nativeQuery = true)
    List<String> getCaseInfoALLStatusByUploadSession(@Param("date") String strDate);

    @Query(value = "select CSE_ID from CSE where  CSE_ST_CD='ONBD' and COMPL_DT>= NOW()- INTERVAL 1 DAY limit 500"
            , nativeQuery = true)
    List<String> findCSEIDLastDayLimitCompletedDate();

}
