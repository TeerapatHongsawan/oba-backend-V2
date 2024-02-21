package th.co.scb.onboardingapp.service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.entity.AppFormConfigEntity;
import th.co.scb.onboardingapp.repository.AppformConfigJpaRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppformConfigService {

    @Autowired
    private AppformConfigJpaRepository appformConfigRepository;

    @Cacheable("getAppformConfig")
    public List<AppFormConfigEntity> getAppformConfig() {
        return appformConfigRepository.findAll();
    }

    public Boolean isEffective(LocalDate localDateTime, String filename, List<AppFormConfigEntity> appFormConfigs) {
        boolean getEffectTiveDate = true;
        boolean getExpireDate = true;
        AppFormConfigEntity appFormConfig = appFormConfigs.stream().filter(i -> i.getFileName().toLowerCase().equalsIgnoreCase(filename)).findFirst().orElse(null);
        if (appFormConfig != null) {
            if (appFormConfig.getEffectTiveDate() == null) {
                getEffectTiveDate = true;
            } else {
                //EffectTiveDate >= NOW =true
                getEffectTiveDate = appFormConfig.getEffectTiveDate().isBefore(localDateTime.plusDays(1));
            }
            if (appFormConfig.getExpireDate() == null) {
                getExpireDate = true;
            } else {
                //ExpireDate <= Now =true
                getExpireDate = appFormConfig.getExpireDate().isAfter(localDateTime.plusDays(-1));
            }
        }
        return getExpireDate && getEffectTiveDate;
    }

}
