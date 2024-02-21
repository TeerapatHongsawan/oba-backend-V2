package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.model.CaptureResponse;
import th.co.scb.onboardingapp.service.DocumentApplicationService;
import th.co.scb.onboardingapp.service.WatchListApplicationService;
import th.co.scb.onboardingapp.utility.MockModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WatchListControllerTest {

    @InjectMocks
    private WatchListController watchListController;

    @Mock
    private WatchListApplicationService watchListApplicationService;

    MockModel mockModel = new MockModel();

    @Test
    public void testGetPepInfoFail() {

        when(watchListApplicationService.getPep(any(),any())).thenReturn(mockModel.getWatchlistFailResponse());
        assertEquals(watchListController.getPepInfo(mockModel.getWatchlistRequest(),mockModel.getObaAuthentication()).get(0).getResultCode(),"400");
        verify(watchListApplicationService, times(1)).getPep(any(), any());

    }
    @Test
    public void testGetPepInfoSuccess() {

        when(watchListApplicationService.getPep(any(),any())).thenReturn(mockModel.getWatchlistSuccessResponse());
        assertEquals(watchListController.getPepInfo(mockModel.getWatchlistRequest(),mockModel.getObaAuthentication()).get(0).getResultCode(),"200");
        verify(watchListApplicationService, times(1)).getPep(any(), any());
    }
}