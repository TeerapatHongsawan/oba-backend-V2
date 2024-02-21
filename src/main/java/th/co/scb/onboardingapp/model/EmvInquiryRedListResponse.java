package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.List;

@Data
public class EmvInquiryRedListResponse {
    private List<EmvEmailAddress> emailAddressList;
}
