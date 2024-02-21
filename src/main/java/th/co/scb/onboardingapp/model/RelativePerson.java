package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class RelativePerson {
    private String relativeId;
    /* M1: TaxID, P1: CitizenID, P7: AlienID, P8: PassportNo, PS: SCBStaffID, P0: InternalPersonalID */
    private String relativeIDType;
    /* 01=Spouse */
    private String relationType;
    private String relativeTitle;
    private String relativeFirstName;
    private String relativeMiddleName;
    private String relativeLastName;
    private String relativeNationality;
    private boolean pepFlag;
}
