package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.entity.LicenseEntity;
import th.co.scb.onboardingapp.model.entity.LicenseKey;
import th.co.scb.onboardingapp.repository.LicenseJpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LicenseService {

    @Autowired
    private LicenseJpaRepository licenseRepository;

    public LicenseEntity getLicense(String type, String number) {
        LicenseKey licenseKey = new LicenseKey();
        licenseKey.setLicenseNumber(number);
        licenseKey.setLicenseType(type);
        LicenseEntity result = licenseRepository.findById(licenseKey).orElse(null);
        if (result != null && !result.getExpiryDate().toLocalDate().isBefore(LocalDateTime.now().toLocalDate())) {
            return result;
        } else {
            return null;
        }
    }

    public List<LicenseEntity> getLicenses(String username) {
        List<LicenseEntity> result = licenseRepository.findByEmployeeIdByOrderByExpiryDateDescAndOrderByLicenseNumberDesc(username);
        result = result.stream().filter(it->!it.getExpiryDate()
                .toLocalDate().isBefore(LocalDateTime.now().toLocalDate())).collect(Collectors.toList());
        return result;
    }
}
