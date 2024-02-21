package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountTypeInfo {
    private String code;
    private String name;
    private String nameEn;
    private String shortDescription;
    private String description;
    private String descriptionFCD;
    private String image;
    private String salesheetLink;
    private String salesheetLinkEn;
    private String defaultOrder;
    private List<AccountInfo> accounts;
}
