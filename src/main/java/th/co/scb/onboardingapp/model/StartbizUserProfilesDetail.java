package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class StartbizUserProfilesDetail {
    private String staffID;

    private String userRole;

    private String titleTH;

    private String firstNameTH;

    private String lastNameTH;

    private String titleEN;

    private String firstNameEN;

    private String lastNameEN;

    private String branchCode;

    private String branchNameTH;

    private String userPosition;

    private String ocCode;

    private String managerID;

    private String email;

    private String corpCode;

    private String prefixTH;

    private String segCode;

    private String prefixEN;

    private String sourceSys;

    private boolean accountOwner;

    private boolean accountOwnerManager;

    private boolean ocOwner;

    private boolean manager;

    private boolean kycOnly;

    private String corpTTL;

    private String jobCode;

    private String multiRole;

    private String oicList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private Date timeStampCreated;
}

