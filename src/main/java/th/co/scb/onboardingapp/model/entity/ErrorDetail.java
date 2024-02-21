package th.co.scb.onboardingapp.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ErrorDetail  {
    private String code;
    private String description;
    private String originalErrorDesc;
    private String message;
    private String moreInfo;
    private String severitylevel;
    private String originalErrorCode;
}
