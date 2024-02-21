package th.co.scb.onboardingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.repository.AccessMappingChannelRepository;
import th.co.scb.onboardingapp.repository.AccessMappingTypeRepository;
import th.co.scb.onboardingapp.utility.MockModel;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccessMappingServiceTest {

    @InjectMocks
    private AccessMappingService accessMappingService;

    @Mock
    AccessMappingTypeRepository accessMappingTypeRepository;

    @Mock
    AccessMappingChannelRepository accessMappingChannelRepository;

    MockModel mockModel = new MockModel();

    @Test
    public void shouldReturnAccessMappingTypeEntityWhenGetFunctionByType() {
        when(accessMappingTypeRepository.findById(anyString())).thenReturn(Optional.of(mockModel.accessMappingTypeEntity()));
        assertEquals(Optional.of(mockModel.accessMappingTypeEntity()), accessMappingService.getFunctionByType("branch"));
    }

    @Test
    public void shouldReturnAccessMappingChannelEntityWhenGetFunctionByChannel() {
        when(accessMappingChannelRepository.findById(anyString())).thenReturn(Optional.of(mockModel.accessMappingChannelEntity()));
        assertEquals(Optional.of(mockModel.accessMappingChannelEntity()), accessMappingService.getFunctionByChannel("2"));
    }
}
