package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.exception.UnauthorizedException;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.entity.ApprovalEntity;
import th.co.scb.onboardingapp.model.entity.ApprovalRequiredEntity;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;
import th.co.scb.onboardingapp.model.enums.ApprovalErrorCodes;
import th.co.scb.onboardingapp.repository.ApprovalJpaRepository;
import th.co.scb.onboardingapp.repository.ApprovalRequiredJpaRepository;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ApprovalService {

    @Autowired
    ApprovalJpaRepository approvalRepository;

    @Autowired
    ApprovalRequiredJpaRepository approvalRequiredRepository;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    MappingHelper mappingHelper;

    @Value("${approval.remote.expiration-seconds}")
    private long remoteApprovalExpirationSeconds;

    @Autowired
    public ApprovalService(ApprovalJpaRepository approvalRepository, ApprovalRequiredJpaRepository approvalRequiredRepository, EmployeeService employeeService, MappingHelper mappingHelper) {
        this.approvalRepository = approvalRepository;
        this.approvalRequiredRepository = approvalRequiredRepository;
        this.employeeService = employeeService;
        this.mappingHelper = mappingHelper;
    }

    public Optional<ApprovalDto> getApproval(String id) {
        ApprovalEntity approval = approvalRepository.findById(id).orElse(null);
        if (approval == null) {
            return Optional.empty();
        }
        ApprovalDto info = mappingHelper.map(approval, ApprovalDto.class);
        return Optional.of(info);
    }

    public ApprovalDto saveApproval(ApprovalDto info) {
        ApprovalEntity approval = mappingHelper.map(info, ApprovalEntity.class);
        approvalRepository.save(approval);
        info.setVersion(approval.getVersion());
        return info;
    }

    public ApprovalDto approve(String username, ApprovalDto info) {
        Approver approver = getApprover(username, info.getApprovalBranchId());
        checkApproverPermission(approver, info);
        ApprovalDto approvalToSave = buildApproval(approver.getName(), info);
        return saveApproval(approvalToSave);
    }

    public ApprovalDto reject(String username, String rejectReason, ApprovalDto info) {
        Approver rejectBy = getApprover(username, info.getApprovalBranchId());
        checkApproverPermission(rejectBy, info);
        ApprovalDto approvalToSave = buildRejectApproval(rejectBy.getName(), rejectReason, info);
        return saveApproval(approvalToSave);
    }

    public ApprovalDto lock(String lockBy, ApprovalDto info) {
        info.setLockBy(lockBy);
        info.setLockDate(Date.from(Instant.now()));
        info.setUpdatedDate(Date.from(Instant.now()));

        return saveApproval(info);
    }

    public ApprovalDto unlock(ApprovalDto info) {
        info.setLockBy("");
        info.setLockDate(null);
        info.setUpdatedDate(Date.from(Instant.now()));

        return saveApproval(info);
    }

    @Cacheable("getApprovalRequired")
    public Optional<ApprovalRequiredEntity> getApprovalRequired(String functionCode) {
        return Optional.ofNullable(approvalRequiredRepository.findById(functionCode)).orElse(null);
    }

    @Cacheable("getApprovalRequiredMap")
    public Map<String, ApprovalRequiredEntity> getApprovalRequiredMap() {
        List<ApprovalRequiredEntity> list = approvalRequiredRepository.findAll();
        return list.stream().collect(Collectors.toMap(ApprovalRequiredEntity::getFunctionCode, Function.identity()));
    }

    private ApprovalDto buildApproval(String approveBy, ApprovalDto approval) {
        int approvalCount = approval.getApprovalCount() + 1;
        boolean isApproved = approvalCount >= approval.getApprovalRequired();
        boolean firstApproval = approvalCount == 1;
        ApprovalStatus status = isApproved ? ApprovalStatus.A : approval.getApprovalStatus();

        approval.setApprovalStatus(status);
        approval.setApprovalCount(approvalCount);
        approval.setLockBy("");
        approval.setLockDate(null);
        approval.setUpdatedDate(Date.from(Instant.now()));

        if (firstApproval) {
            approval.setApproval1(approveBy);
            approval.setApproval1Date(Date.from(Instant.now()));
            approval.setApproval1Status(ApprovalStatus.A);
        } else {
            approval.setApproval2(approveBy);
            approval.setApproval2Date(Date.from(Instant.now()));
            approval.setApproval2Status(ApprovalStatus.A);
        }

        return approval;
    }

    private ApprovalDto buildRejectApproval(String rejectBy, String rejectReason, ApprovalDto approval) {
        boolean firstApproval = approval.getApprovalCount() == 0;
        ApprovalStatus status = ApprovalStatus.R;

        approval.setApprovalStatus(status);
        approval.setLockBy("");
        approval.setLockDate(null);
        approval.setUpdatedDate(Date.from(Instant.now()));

        if (firstApproval) {
            approval.setApproval1(rejectBy);
            approval.setApproval1Date(Date.from(Instant.now()));
            approval.setApproval1Status(status);
            approval.setApproval1RejectReason(rejectReason);
        } else {
            approval.setApproval2(rejectBy);
            approval.setApproval2Date(Date.from(Instant.now()));
            approval.setApproval2Status(status);
            approval.setApproval2RejectReason(rejectReason);
        }

        return approval;
    }

    private void checkApproverPermission(Approver approver, ApprovalDto approval) {
        AuthorizedLevel level = getAuthorizedLevel(approval);
        List<AuthorizedLevel> authorizedLevelsRequired = employeeService.getAuthorizedLevelsRequired(level);

        if (!authorizedLevelsRequired.contains(approver.getApproverLevel())) {
            throw new UnauthorizedException(ApprovalErrorCodes.APPL_NO_PERMISSION.name(), ApprovalErrorCodes.APPL_NO_PERMISSION.getMessage());
        }
    }

    private AuthorizedLevel getAuthorizedLevel(ApprovalDto approval) {
        if (approval.getApprovalCount() < approval.getSscRequired()) {
            return AuthorizedLevel.SSC;
        } else {
            return AuthorizedLevel.SC;
        }
    }

    private Approver getApprover(String username, String approvalBranchId) {
        Map<String, AuthorizedLevel> map = employeeService.getAuthorizedLevelMap(username);
        AuthorizedLevel authorizedLevel = map.get(approvalBranchId);
        if (authorizedLevel == null) {
            throw new UnauthorizedException(ApprovalErrorCodes.APPL_NO_PERMISSION.name(), ApprovalErrorCodes.APPL_NO_PERMISSION.getMessage());
        }
        return new Approver(username, authorizedLevel);
    }

    public List<EmployeeEntity> getApprovers(ApprovalDto approval) {
        AuthorizedLevel level = getAuthorizedLevel(approval);

        return employeeService.findApprovers(approval.getApprovalBranchId(), level)
                .stream()
                .filter(it -> !Objects.equals(it.getEmployeeId(), approval.getApproval1()))
                .filter(it -> !Objects.equals(it.getEmployeeId(), approval.getSellerId()))
                .collect(Collectors.toList());
    }

    public List<ApprovalEntity> getRemoteApprovals(String employeeId, String approvalBranchId) {
        Date after = new Date();
        after.setTime(after.getTime() - remoteApprovalExpirationSeconds * 1000);

        Map<String, AuthorizedLevel> map = employeeService.getAuthorizedLevelMap(employeeId);
        return approvalRepository.findByApprovalStatusAndTypeAndSellerIdNotAndApprovalBranchIdStartingWithAndCreatedDateAfter(ApprovalStatus.P, ApprovalType.R, employeeId, approvalBranchId, after).stream()
                .filter(it -> it.getApprovalCount() >= it.getSscRequired() || map.get(it.getApprovalBranchId()) == AuthorizedLevel.SSC)
                .collect(Collectors.toList());
    }
}
