package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.FaceCompareMatchLevel;
import th.co.scb.onboardingapp.model.entity.FaceCompareMatchLevelEntity;
import th.co.scb.onboardingapp.repository.FaceCompareMatchLevelJpaRepository;


import java.util.List;

@Service
public class FaceCompareMatchLevelService {

    @Autowired
    private FaceCompareMatchLevelJpaRepository faceCompareMatchLevelJpaRepository;

    @Autowired
    private MappingHelper mappingHelper;

    @Cacheable(value = "faceCompareMatchLevels", unless = "#result == null")
    public List<FaceCompareMatchLevel> faceCompareMatchLevels() {
        List<FaceCompareMatchLevelEntity> list = faceCompareMatchLevelJpaRepository.findAll();
        return  mappingHelper.mapAsList(list, FaceCompareMatchLevel.class);
    }

    public FaceCompareMatchLevel mapFaceCompareMatchLevel(String matchLevel) {
        FaceCompareMatchLevelEntity faceCompareMatchLevel = faceCompareMatchLevelJpaRepository.findByMatchLevel(matchLevel);
        return  mappingHelper.map(faceCompareMatchLevel, FaceCompareMatchLevel.class);
    }

    public List<FaceCompareMatchLevel> mapFaceCompareMatchValue(String matchValue) {
        List<FaceCompareMatchLevelEntity> faceCompareMatchLevelEntities = faceCompareMatchLevelJpaRepository.findByMatchValue(matchValue);
        return  mappingHelper.mapAsList(faceCompareMatchLevelEntities, FaceCompareMatchLevel.class);
    }
}
