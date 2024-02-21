package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.BklAns;
import th.co.scb.onboardingapp.model.WatchlistRequest;
import th.co.scb.onboardingapp.model.WatchlistResponse;


import java.util.List;

@Service
public class WatchListApplicationService {



    @Autowired
    private GetPepCommandHandler getPepCommandHandler;


//    public List<WatchlistResponse> getWatchlist(WatchlistRequest[] data, ObaAuthentication authentication) {
//        return getWatchListCommandHandler.getWatchlist(data, authentication);
//    }

    public List<WatchlistResponse> getPep(WatchlistRequest[] data, ObaAuthentication authentication) {
        return getPepCommandHandler.getPep(data, authentication);
    }



}
