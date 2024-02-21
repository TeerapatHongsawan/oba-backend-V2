package th.co.scb.onboardingapp.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import th.co.scb.onboardingapp.helper.LocalDateTimeToStringSerializer;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "FACE_COMPARE_MATCH_LEVEL")
public class FaceCompareMatchLevelEntity {

    @Id
    @Column(name = "MATCH_ID", nullable = false)
    private String matchId;

    @Column(name = "MATCH_LEVEL", length = 32, nullable = false)
    private String matchLevel;

    @Column(name = "MATCH_VALUE", length = 1, nullable = false)
    private String matchValue;

    @Column(name = "VENDOR", length = 50, nullable = false)
    private String vendor;

    @Column(name = "CREATE_DATE")
    @JsonSerialize(using = LocalDateTimeToStringSerializer.class)
    private LocalDateTime createDate;

    @Column(name = "UPDATE_DATE")
    @JsonSerialize(using = LocalDateTimeToStringSerializer.class)
    private LocalDateTime updateDate;
}

