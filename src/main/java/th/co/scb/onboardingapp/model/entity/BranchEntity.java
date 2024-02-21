package th.co.scb.onboardingapp.model.entity;

import lombok.Data;


@Data
public class BranchEntity {
    private String branchId;
    private String nameEn;
    private String nameThai;
    private String regionCode;
    private String channelType;
    private boolean isBookingBranch;
    private boolean ownBranchOnly;
}
