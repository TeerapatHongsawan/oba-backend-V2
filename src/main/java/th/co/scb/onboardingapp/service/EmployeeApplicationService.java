package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.NotFoundException;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.EmployeeInfo;
import th.co.scb.onboardingapp.model.EmployeeMe;
import th.co.scb.onboardingapp.model.ValidateRequest;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;
import th.co.scb.onboardingapp.model.entity.LicenseEntity;
import th.co.scb.onboardingapp.repository.OcJpaRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeApplicationService {

    @Autowired
    private MappingHelper mappingHelper;

    @Autowired
    private EmployeeService employeeService;

//    @Autowired
//    private LdapValidator ldapValidator;
//
    @Autowired
    private LicenseService licenseService;

    @Autowired
    private OcJpaRepository ocJpaRepository;

    @Autowired(required = false)
    private PermissionService permissionService;

    public List<EmployeeInfo> getEmployeesInfo(ObaAuthentication authentication) {
        List<EmployeeEntity> list = employeeService.getEmployeeList(authentication.getBranchId());
        return mappingHelper.mapAsList(list, EmployeeInfo.class);
    }

    public EmployeeMe getEmployeeMe(ObaAuthentication authentication) {
        EmployeeEntity employeeEntity = employeeService.getEmployee(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Employee"));

        EmployeeMe employeeMe = mappingHelper.map(employeeEntity, EmployeeMe.class);

        ocJpaRepository.findById(employeeMe.getOcCode()).ifPresentOrElse(organization -> {
            employeeMe.setOcName(organization.getOcNameTh());
            employeeMe.setOcNameEn(organization.getOcNameEn());
        }, () -> new NotFoundException("Organization"));

        employeeService.getBranch(authentication.getBranchId()).ifPresentOrElse(branch -> {
            employeeMe.setBranchId(branch.getBranchId());
            employeeMe.setBranchNameThai(branch.getNameThai());
            employeeMe.setBranchNameEn(branch.getNameEn());
        }, () -> new NotFoundException("Branch"));

        employeeService.getLoginBranch(authentication.getName(), authentication.getBranchId())
                .ifPresentOrElse(branch -> employeeMe.setApprovalBranchId(branch.getApprovalBranchId()
                ), () -> new NotFoundException("Login_branch"));

        employeeMe.setRoles(authentication.getRoles());
        employeeMe.setLicenses(licenseService.getLicenses(authentication.getName()).stream().collect(Collectors.toMap(LicenseEntity::getLicenseType, LicenseEntity::getLicenseNumber, (a, b) -> a)));

        if (permissionService != null) {
            employeeMe.setPermissions(permissionService.retrievePermissions(authentication.getRoles()));
        }

        return employeeMe;
    }

//    public EmployeeInfo getEmployeeLicense(String licenseType, String licenseNumber) {
//        LicenseEntity license = licenseService.getLicense(licenseType, licenseNumber);
//        if (license != null) {
//            EmployeeEntity employee = employeeService.getEmployee(license.getEmployeeId())
//                    .orElseThrow(() -> new NotFoundException("Employee"));
//            return mappingHelper.map(employee, EmployeeInfo.class);
//        } else {
//            throw new NotFoundException("License");
//        }
//    }

//    public void validateEmployee(ValidateRequest validateRequest) {
//        ldapValidator.validate(validateRequest.getUsername(), validateRequest.getSecret());
//    }
}
