package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.entity.AccessMappingChannelEntity;
import th.co.scb.onboardingapp.model.entity.AccessMappingTypeEntity;
import th.co.scb.onboardingapp.repository.AccessMappingChannelRepository;
import th.co.scb.onboardingapp.repository.AccessMappingTypeRepository;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AccessMappingService {

    @Autowired
    private AccessMappingTypeRepository accessMappingTypeRepository;

    @Autowired
    private AccessMappingChannelRepository accessMappingChannelRepository;

    @Cacheable("accessMappingType")
    public Optional<AccessMappingTypeEntity> getFunctionByType(String type) {
        return accessMappingTypeRepository.findById(type);
    }

    @Cacheable("accessMappingChannel")
    public Optional<AccessMappingChannelEntity> getFunctionByChannel(String channel) {
        return accessMappingChannelRepository.findById(channel);
    }

    @Cacheable("accessMappings")
    public Map<String, AccessMappingTypeEntity> getUserTypeMap() {
        return accessMappingTypeRepository.findAll().stream().collect(Collectors.toMap(AccessMappingTypeEntity::getType, Function.identity()));
    }
}
