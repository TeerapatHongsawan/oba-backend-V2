package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.WatchlistRequest;
import th.co.scb.onboardingapp.model.WatchlistResponse;


import java.util.List;

@Component
public class GetPepCommandHandler {

    @Autowired
    private WatchListService watchListService;

    public List<WatchlistResponse> getPep(WatchlistRequest[] data, ObaAuthentication authentication) {
        return watchListService.getPepInfo(data, authentication);
    }
}
