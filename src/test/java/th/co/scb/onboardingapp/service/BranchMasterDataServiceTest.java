package th.co.scb.onboardingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.repository.BranchRepository;
import th.co.scb.onboardingapp.utility.MockModel;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BranchMasterDataServiceTest {

    @InjectMocks
    private BranchMasterDataService branchMasterDataService;
    @Mock
    private BranchRepository branchRepository;

    MockModel mockModel = new MockModel();

    @Test
    public void shouldReturnBranchEntityListWhenGetBranches() {
        when(branchRepository.findAll()).thenReturn(mockModel.branchEntityList());
        assertEquals(mockModel.branchEntityList(), branchMasterDataService.branches());
    }
}
