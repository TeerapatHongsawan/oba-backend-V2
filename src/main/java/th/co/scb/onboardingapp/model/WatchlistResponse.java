package th.co.scb.onboardingapp.model;

import lombok.Data;
import th.co.scb.entapi.watchlist.model.WatchlistValidationResponseWithDetail;

@Data
public class WatchlistResponse extends WatchlistValidationResponseWithDetail {
    private Relation relation = Relation.SELF;
    private String thaiTitle;
}
