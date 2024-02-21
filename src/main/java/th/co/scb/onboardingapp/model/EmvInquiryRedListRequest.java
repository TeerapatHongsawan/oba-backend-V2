package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.List;

@Data
public class EmvInquiryRedListRequest {
    private List<EmvEmailAddress> emailAddressList;
    private String idNumber;
    private String idType;
}
