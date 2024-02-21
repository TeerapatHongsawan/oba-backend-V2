package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoAdditionalDetails {

    private String benefits;
    private String interest;
    private String interestLink;
    private String interestNote;
    private String condition;
    private String remark;
    private String benefitsEn;
    private String interestEn;
    private String interestLinkEn;
    private String interestNoteEn;
    private String conditionEn;
    private String remarkEn;

}
