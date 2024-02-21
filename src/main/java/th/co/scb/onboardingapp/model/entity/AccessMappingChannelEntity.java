package th.co.scb.onboardingapp.model.entity;

import lombok.Data;


@Data
public class AccessMappingChannelEntity {
    private String channel;
    private String functions;
    private boolean loginActive;
}