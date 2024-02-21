package th.co.scb.onboardingapp.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.DocumentBlobEntity;

import java.util.Collection;
import java.util.List;

@Repository
public interface DocumentBlobJpaRepository extends JpaRepository<DocumentBlobEntity, String> {

    @Modifying
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Query(value = "DELETE FROM DOC_BLOB WHERE UPL_SSN_ID IN (:ssnIds)", nativeQuery = true)
    int deleteDocumentsByUploadSessionId(@Param("ssnIds") Collection<String> ssnIds);

    @Query(value = "SELECT  b.UPL_SSN_ID" +
            " FROM DOC_BLOB AS b" +
            " INNER JOIN DOC_STG AS s ON b.UPL_SSN_ID = s.UPL_SSN_ID AND s.STATUS = 'COMPL' " +
            " LEFT JOIN DOC_STG_RECONCILE AS rec ON rec.ID = s.ID" +
            " WHERE b.TIMESTAMP_CREATED < :date " +
            " GROUP BY b.UPL_SSN_ID" +
            " HAVING count(rec.ID) = 0" +
            " LIMIT 5000", nativeQuery = true)
    List<String> getDocumentsByStatusCompleted(@Param("date") String toDate);

    @Query(value = "SELECT b.UPL_SSN_ID " +
            " FROM DOC_BLOB as b " +
            " INNER JOIN DOC_STG AS doc_all ON b.UPL_SSN_ID = doc_all.UPL_SSN_ID" +
            " LEFT JOIN DOC_STG AS doc_compl ON doc_compl.ID = doc_all.ID AND doc_compl.STATUS = 'COMPL'" +
            " LEFT JOIN DOC_STG_RECONCILE AS rec_all ON rec_all.ID = doc_all.ID" +
            " LEFT JOIN DOC_STG_RECONCILE AS rec_success ON rec_success.ID = doc_all.ID AND rec_success.REC_STATUS = 'SUCCESS'" +
            " WHERE b.TIMESTAMP_CREATED > (CURDATE() - INTERVAL 90 DAY) " +
            " GROUP BY UPL_SSN_ID" +
            " HAVING count(rec_all.ID) > 0 AND (count(rec_success.id) = count(rec_all.ID)) AND (count(doc_all.ID) = count(doc_compl.ID))" +
            " LIMIT 5000", nativeQuery = true)
    List<String> getDocumentByReconcileStatusSuccess();


    @Query(value = "SELECT b.UPL_SSN_ID FROM DOC_BLOB b LEFT JOIN DOC_STG s ON b.UPL_SSN_ID = s.UPL_SSN_ID" +
            " WHERE s.UPL_SSN_ID IS NULL" +
            " AND Date(b.TIMESTAMP_CREATED) < :date" +
            " AND b.MIME_TP= 'image/png'" +
            " limit 5000", nativeQuery = true)
    List<String> getDocumentSignature(@Param("date") String toDate);

    @Query(value = "SELECT b.UPL_SSN_ID " +
            "FROM  DOC_BLOB b LEFT JOIN DOC_STG s" +
            " ON b.UPL_SSN_ID = s.UPL_SSN_ID" +
            " WHERE s.UPL_SSN_ID IS NULL" +
            " AND Date(b.TIMESTAMP_CREATED) <= :date" +
            " AND MIME_TP <> 'image/png'" +
            " limit 1000", nativeQuery = true)
    List<String> getDocumentNotMacthDocStg(@Param("date") String toDate);

    @Query(value = "SELECT UPL_SSN_ID FROM  DOC_BLOB order by TIMESTAMP_CREATED limit 5000", nativeQuery = true)
    List<String> getDocument();

    @Query(value = "SELECT BIN_DATA FROM  DOC_BLOB where UPL_SSN_ID = :uploadSessionId ;", nativeQuery = true)
    byte[] getDocumentByUploadSessionId(String uploadSessionId);


}
