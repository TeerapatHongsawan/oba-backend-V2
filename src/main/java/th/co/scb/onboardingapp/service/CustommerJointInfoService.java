package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.ObaBackendCoreMsApplication;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.MasterJointType;
import th.co.scb.onboardingapp.model.entity.MasterJointTypeEntity;
import th.co.scb.onboardingapp.repository.CustommerJointInfoRepository;


import java.util.List;


@Service
public class CustommerJointInfoService {

    @Autowired
    CustommerJointInfoRepository custommerJointInfoRepository;

    @Autowired
    MappingHelper mappingHelper;

    public List<MasterJointType> JointInfo() {
        List<MasterJointTypeEntity> list = custommerJointInfoRepository.findByStatus("Y");
        return mappingHelper.mapAsList(list, MasterJointType.class);
    }
}
