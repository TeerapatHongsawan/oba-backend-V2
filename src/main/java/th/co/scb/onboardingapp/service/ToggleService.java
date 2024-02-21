package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.ToggleConfig;
import th.co.scb.onboardingapp.model.entity.ToggleEntity;
import th.co.scb.onboardingapp.repository.ToggleJpaRepository;

import java.util.List;

@Service
public class ToggleService {

    @Autowired
    ToggleJpaRepository toggleJpaRepository;

    @Autowired
    MappingHelper mappingHelper;

    @Cacheable(value = "toggles", unless = "#result == null")
    public List<ToggleConfig> toggles() {
        List<ToggleEntity> list = toggleJpaRepository.findAll();
        return  mappingHelper.mapAsList(list,ToggleConfig.class);
    }
}
