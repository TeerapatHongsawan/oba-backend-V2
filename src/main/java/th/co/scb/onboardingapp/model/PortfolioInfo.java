package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.List;

@Data
public class PortfolioInfo {
    private List<PortfolioItem> accounts;
    private boolean hasCommands;
}