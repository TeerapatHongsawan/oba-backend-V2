package th.co.scb.onboardingapp.model;

import lombok.Data;
import th.co.scb.entapi.watchlist.model.WatchlistValidationRequest;

@Data
public class WatchlistRequest extends WatchlistValidationRequest {
    private Relation relation = Relation.SELF;
    private String thaiTitle;
}

