package th.co.scb.onboardingapp.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "ACTIVITY")
public class ActivityEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "BRANCH_ID", length = 6)
    private String branchId;

    @Column(name = "USER_ID", length = 32)
    private String userId;

    @Column(name = "TIMESTAMP_CREATED")
    private Timestamp timestampCreated;

    @Column(name = "ACTIVITY_JSON", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String  activityJson;

    @Column(name = "RECORD_TYPE", length = 15)
    private String recordType;

}
