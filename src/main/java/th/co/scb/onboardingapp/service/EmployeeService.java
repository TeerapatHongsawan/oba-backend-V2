package th.co.scb.onboardingapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.exception.ErrorCodes;
import th.co.scb.onboardingapp.exception.InternalErrorException;
import th.co.scb.onboardingapp.model.AuthorizedLevel;
import th.co.scb.onboardingapp.model.LoginBranchInfo;
import th.co.scb.onboardingapp.model.StartbizUserProfilesDetail;
import th.co.scb.onboardingapp.model.entity.*;
import th.co.scb.onboardingapp.repository.*;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ApprovalPermissionRepository approvalPermissionRepository;

    @Autowired
    private LoginBranchRepository loginBranchRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private LoginSessionRepository loginSessionRepository;

    @Cacheable("employee")
    public Optional<EmployeeEntity> getEmployee(String username) {
        return employeeRepository.findById(username);
    }

    @Cacheable("getAuthorizedLevelMap")
    public Map<String, AuthorizedLevel> getAuthorizedLevelMap(String username) {
        return approvalPermissionRepository.findByEmployeeId(username).stream()
                .collect(Collectors.toMap(ApprovalPermissionEntity::getApprovalBranchId, ApprovalPermissionEntity::getAuthorizedLevel));
    }

    @Cacheable(value = "employeeList", unless = "#result == null")
    public List<EmployeeEntity> getEmployeeList(String branchId) {
        return employeeRepository.findByBranchId(branchId);
    }

    @Cacheable("findApprovers")
    public List<EmployeeEntity> findApprovers(String approvalBranchId, AuthorizedLevel level) {
        List<AuthorizedLevel> levels = getAuthorizedLevelsRequired(level);
        Set<String> levelSet = levels.stream().map(Enum::name).collect(Collectors.toSet());
        return employeeRepository.findApprovers(approvalBranchId, levelSet);
    }

    @Cacheable("branch")
    public Optional<BranchEntity> getBranch(String branchId) {
        return branchRepository.findById(branchId);
    }


    @Cacheable("loginBranch")
    public Optional<LoginBranchInfo> getLoginBranch(String employeeId, String branchId) {
        Optional<LoginBranchEntity> loginBranch = loginBranchRepository.findByEmployeeIdAndBranchId(employeeId, branchId);
        LoginBranchInfo loginBranchInfo = new LoginBranchInfo();
        loginBranchInfo.setEmployeeId(loginBranch.get().getEmployeeId());
        loginBranchInfo.setBranchId(loginBranch.get().getBranchId());
        loginBranchInfo.setApprovalBranchId(loginBranch.get().getApprovalBranchId());
        try {
            loginBranchInfo.setRoles(new ObjectMapper().readValue(loginBranch.get().getRoles(), Set.class));
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(ErrorCodes.S00001.name(), ErrorCodes.S00001.getMessage());
        }
        return Optional.of(loginBranchInfo);
    }

    @Cacheable("branches")
    public List<OrganizationEntity> getBranches(String username) {
        return organizationRepository.findByEmployeeId(username);
    }

    @Cacheable("StartBizEmp")
    public StartbizUserProfilesDetail getStartBizEmp(String username) {
        Optional<StartbizUserProfilesDetail> result = employeeRepository.findByIdStartBiz(username);
        return result.orElse(null);
    }

    @Cacheable("organization")
    public OrganizationEntity getOrganization(String ocCode) {
        return organizationRepository.findById(ocCode).orElse(null);
    }

    public List<AuthorizedLevel> getAuthorizedLevelsRequired(AuthorizedLevel level) {
        if (AuthorizedLevel.SSC.equals(level)) {
            return Collections.singletonList(AuthorizedLevel.SSC);
        } else {
            return Arrays.asList(AuthorizedLevel.SC, AuthorizedLevel.SSC);
        }
    }

    @Cacheable("organizations")
    public List<OrganizationEntity> getOrganizations() {
        return organizationRepository.findAll();
    }


    public List<LoginSessionEntity> getLoginSession(String username, String appName) {
        return loginSessionRepository.findByEmployeeId(username, appName);
    }

}
