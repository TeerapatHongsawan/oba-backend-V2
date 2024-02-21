package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.service.GeneralParamService;
import th.co.scb.onboardingapp.utility.MockModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GeneralParamsControllerTest {

    @InjectMocks
    private GeneralParamsController generalParamsController;

    @Mock
    private GeneralParamService generalParamService;
    MockModel mockModel = new MockModel();

    @Test
    public void getFrontendGeneralParamsTest() {
        generalParamsController.getFrontendGeneralParams();


        verify(generalParamService, times(1)).getFrontEndGeneralParam();
    }

//    @Test
//    public void getConvertEpassFlagGeneralParamsTest() {
//        generalParamsController.getConvertEpassFlagGeneralParams();
//        verify(generalParamService, times(1)).getConvertEpassbookFlag();
//    }

    @Test
    public void getAllowForeignerRolesTest() {

        when(generalParamService.getAllowForeignerRoles()).thenReturn(mockModel.getAllowForeignerRoles());
        assertThat(generalParamsController.getAllowForeignerRoles()).isNotNull();
    }

    @Test
    public void getForeignerAllowProductsTest() {
        when(generalParamService.getForeignerAllowProducts()).thenReturn(mockModel.getForeignerAllowProducts());
        assertThat(generalParamsController.getForeignerAllowProducts()).isNotNull();


    }


    @Test
    public void getFrontEndGeneralParamTest() {
        when(generalParamService.getFrontEndGeneralParam()).thenReturn(mockModel.getFrontEndGeneralParam());
        assertThat(generalParamsController.getFrontendGeneralParams()).isNotNull();


    }
    @Test
    public void getPopupEmvRedlistEmailTest() {
        when(generalParamService.getPopupEmvRedlistEmail()).thenReturn(mockModel.getPopupEmvRedlistEmail());
        assertThat(generalParamsController.getPopupEmvRedlistEmailGeneralParams()).isNotNull();


    }
}
