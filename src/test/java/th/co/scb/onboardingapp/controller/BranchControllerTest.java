package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.model.entity.BranchEntity;
import th.co.scb.onboardingapp.service.BranchApplicationService;
import th.co.scb.onboardingapp.utility.MockModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BranchControllerTest {

    @InjectMocks
    private BranchController branchController;

    @Mock
    private BranchApplicationService branchApplicationService;

    MockModel mockModel = new MockModel();
    @Test
    public void testBrowse() {

        List<BranchEntity> expect = new ArrayList<>();
        BranchEntity branch = new BranchEntity();
        branch.setBranchId("0");
        branch.setNameEn("s99999");
        branch.setNameThai("เทส");
        branch.setChannelType("2");
        branch.setRegionCode("0");
        branch.setOwnBranchOnly(false);
        branch.setBookingBranch(false);
        expect.add(branch);

        List<BranchEntity> response = new ArrayList<>();
        response.add(mockModel.branchEntity());
        when(branchApplicationService.getBranchs()).thenReturn(response);
        assertEquals(expect,branchController.browse());
    }
}