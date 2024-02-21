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
public class DebitCardInfo {
    private String productId;
    private String productType;
    private String productCardType;
    private String stockCode;
    private String productNameTH;
    private String productNameEN;
    private String productDescription;
    private String productDescriptionEN;
    private String productDescriptionDetail;
    private String productDescriptionDetailEN;
    private String productImage;
    private List<DebitCardFeeInfo> feeCodes;
    private String productFeeInfo;
    private String productFeeInfoEN;
    private String defaultOrder;
    private String salesheetLink;
    private String salesheetLinkEN;
}
