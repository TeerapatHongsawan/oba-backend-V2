package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CONSENT_CONTENT")
public class ConsentContentEntity {
    public static final String PRIVACY_NOTICE_FULL_TYPE = "004-F";
    public static final String PRIVACY_NOTICE_SHORT_TYPE = "004";
    public static final String CROSS_SELL_CONSENT_TYPE = "002";
    public static final String CROSS_SELL_PARTNER_CONSENT_TYPE = "002-C";
    public static final String MODEL_CONSENT_TYPE = "003";
    public static final String MARKETING_CONSENT_TYPE = "001";
    public static final String SENSITIVE_CONSENT_TYPE = "005";
    public static final String SCBS_MARKETING_CONSENT_TYPE = "010";
    public static final String SCBS_CROSSELL_CONSENT_TYPE = "011";
    public static final String CROSSMATCH_CONSENT_TYPE = "012";

    public static final String DOPA_CONSENT_TYPE = "006";

    public static final String DOPA_CONSENT_FULL_TYPE = "006-F";


    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long id;

    @Column(name = "CONSENT_TYPE", length = 45)
    private String consentType;

    @Column(name = "CONSENT_VERSION", length = 45)
    private String consentVersion;

    @Column(name = "LINK", length = 45)
    private String link;

    @Column(name = "CONTENT", length = 1000)
    private String content;

    @Column(name = "CONSENT_NAME", length = 255)
    private String consentName;

    @Column(name = "CREATEDATE")
    private LocalDateTime createDate;

    @Column(name = "UPDATEDATE")
    private LocalDateTime updateDate;

    @Column(name = "VENDOR", length = 45)
    private String vendor;

    @Column(name = "CONTENT_TITLE", length = 1000)
    private String contentTitle;

    @Column(name = "LINK_EN", length = 45)
    private String linkEN;

    @Column(name = "CONTENT_EN", length = 1000)
    private String contentEN;

}
