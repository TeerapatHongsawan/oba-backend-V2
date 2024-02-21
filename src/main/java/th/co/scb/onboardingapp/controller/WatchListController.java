package th.co.scb.onboardingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.BklAns;
import th.co.scb.onboardingapp.model.WatchlistRequest;
import th.co.scb.onboardingapp.model.WatchlistResponse;
import th.co.scb.onboardingapp.service.WatchListApplicationService;


import java.util.List;

@RestController
public class WatchListController {

    @Autowired
    private WatchListApplicationService watchListApplicationService;

//    @PostMapping("/api/watchlist/exact-match")
//    public List<WatchlistResponse> getWatchListInfo(@RequestBody WatchlistRequest[] data, ObaAuthentication auth) {
//        return watchListApplicationService.getWatchlist(data, auth);
//    }

    @PostMapping("/api/watchlist/exact-match-pep")
    public List<WatchlistResponse> getPepInfo(@RequestBody WatchlistRequest[] data, ObaAuthentication auth) {
        return watchListApplicationService.getPep(data, auth);
    }

//    @PostMapping("/api/watchlist/exact-match-bkl")
//    public List<BklAns> getBklInfo(@RequestBody WatchlistRequest[] data) {
//        return watchListApplicationService.getBklAns(data);
//    }
}

