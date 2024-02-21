package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class BklAns {
    private String wlID;
    private String accName;
    private String nationalID;
    private AccAddress accAddress;
    private ArrayList<WatchlistTypes> watchlistTypes;
    private String answer;
    private String prifix;
}
