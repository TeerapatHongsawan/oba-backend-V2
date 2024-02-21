package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.Entity;
import lombok.Data;

import jakarta.persistence.Column;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "DOC_BLOB")
public class DocumentBlobEntity {

    @Id
    @Column(name = "UPL_SSN_ID", nullable = false, length = 40)
    private String uploadSessionId;

    @Column(name = "MIME_TP", length = 80)
    private String mimeType;

    @Column(name = "TIMESTAMP_CREATED")
    private LocalDateTime createdTime;

    @Column(name = "TIMESTAMP_UPDATED")
    private LocalDateTime updatedTime;

    @Column(name = "JS_DATA", columnDefinition = "JSON")
    @ColumnTransformer(
            read = "CAST(JS_DATA as text)", // Read the JSONB column as text
            write = "?::jsonb" // Write the Java property as JSONB
    )
    private String jsonData;

    @Column(name = "BIN_DATA", columnDefinition = "BLOB")
    private byte[] binaryData;
}
