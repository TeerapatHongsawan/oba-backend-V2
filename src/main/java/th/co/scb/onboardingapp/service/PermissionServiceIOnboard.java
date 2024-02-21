package th.co.scb.onboardingapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import th.co.scb.onboardingapp.exception.ApplicationException;
import th.co.scb.onboardingapp.model.approval.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
public class PermissionServiceIOnboard implements PermissionService {

    private final ResourceLoader resourceLoader;

    private final PermissionConfig permissionConfig;

    private static final String PERMISSION_CONFIG_LOCATION = "classpath:permission.json";

    @Autowired
    private GeneralParamService generalParamService;

    @Autowired
    public PermissionServiceIOnboard(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.permissionConfig = getPermissionConfig(objectMapper);
        roleDuplicityCheck(this.permissionConfig);
    }

    @Override
    public Permissions retrievePermissions(Set<String> roles) {
        Permissions permissions = new Permissions();
        Object consent = generalParamService.getConsent().getValue().get("list");

        if (permissionConfig != null) {
            Set<String> products = retrieveProductPermissions(roles);
            Set<UserRoleApprovalConfig> approvalConfig = createUserRoleApprovalConfigs(roles);
            EditableFieldsConfig editableFieldsConfig = retrieveEditableFieldsPermissions(roles);
            ValidateFieldsConfig validateFieldsConfig = retrieveValidateFieldsPermissions(roles);
            Set<String> additionalRoles = retrieveAdditionalRolesPermissions(roles);

            permissions.setProducts(products);
            permissions.setApprovalConfig(approvalConfig);
            permissions.setEditableFields(editableFieldsConfig);
            permissions.setValidateFields(validateFieldsConfig);
            permissions.setAdditionalRoles(additionalRoles);
            permissions.setConsent(consent);
        }

        return permissions;
    }

    private Set<String> retrieveProductPermissions(Set<String> roles) {
        Set<RoleProductConfig> productPermissions = ofNullable(permissionConfig).map(PermissionConfig::getProducts).orElseGet(Collections::emptySet);

        return productPermissions.stream()
                .filter(a -> a.getRoles() != null)
                .filter(a -> a.getRoles().stream().anyMatch(roles::contains))
                .map(RoleProductConfig::getValues)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(Collections::emptySet);
    }

    private Set<String> retrieveAdditionalRolesPermissions(Set<String> roles) {
        Set<RoleAdditionalRolesConfig> additionalRolesPermissions = ofNullable(permissionConfig).map(PermissionConfig::getAdditionalRoles).orElseGet(Collections::emptySet);

        return additionalRolesPermissions.stream()
                .filter(a -> a.getRoles() != null)
                .filter(a -> a.getRoles().stream().anyMatch(roles::contains))
                .map(RoleAdditionalRolesConfig::getValues)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(Collections::emptySet);
    }

    private EditableFieldsConfig retrieveEditableFieldsPermissions(Set<String> roles) {
        Set<RoleEditableConfig> roleEditableConfigs = ofNullable(permissionConfig).map(PermissionConfig::getEditable).orElseGet(Collections::emptySet);

        return roleEditableConfigs.stream()
                .filter(a -> a.getRoles() != null)
                .filter(a -> a.getRoles().stream().anyMatch(roles::contains))
                .map(RoleEditableConfig::getValues)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(EditableFieldsConfig::new);
    }

    private ValidateFieldsConfig retrieveValidateFieldsPermissions(Set<String> roles) {
        Set<RoleValidateConfig> roleValidateConfigs = ofNullable(permissionConfig).map(PermissionConfig::getValidate).orElseGet(Collections::emptySet);

        return roleValidateConfigs.stream()
                .filter(a -> a.getRoles() != null)
                .filter(a -> a.getRoles().stream().anyMatch(roles::contains))
                .map(RoleValidateConfig::getValues)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(ValidateFieldsConfig::new);
    }

    private Set<UserRoleApprovalConfig> createUserRoleApprovalConfigs(Set<String> roles) {
        Set<ApprovalConfig> approvalConfigs = permissionConfig.getApprovalConfiguration();

        //filter by first role
        Set<ApprovalConfig> approvalConfigInfosByRole = approvalConfigs.stream()
                .filter(a -> a.getRoles() != null)
                .filter(a -> a.getRoles().stream().anyMatch(roles::contains))
                .collect(Collectors.toSet());

        Set<UserRoleApprovalConfig> result = new LinkedHashSet<>();

        //group by facial
        Map<String, List<ApprovalConfig>> facialMap = approvalConfigInfosByRole.stream()
                .collect(Collectors.groupingBy(a -> String.join(";", a.getFacial().toString(), a.getFacialResult()), LinkedHashMap::new, Collectors.toList()));

        for (List<ApprovalConfig> approvalConfigInfosByKyc : facialMap.values()) {
            UserRoleApprovalConfig config = new UserRoleApprovalConfig();

            for (ApprovalConfig approvalConfig : approvalConfigInfosByKyc) {
                config.setFacial(approvalConfig.getFacial());
                config.setFacialResult(approvalConfig.getFacialResult());

                Requirement requirement = new Requirement();

                requirement.setAddPhone(prepareBooleanSet(approvalConfig.getAddMobilePhone()));
                requirement.setCiChange(prepareBooleanSet(approvalConfig.getCiChange()));
                requirement.setSignature(prepareBooleanSet(approvalConfig.getRequireSignature()));
                requirement.setSpecialFee(prepareBooleanSet(approvalConfig.getSpecialFeeCode()));
                requirement.setNewCustomer(prepareBooleanSet(approvalConfig.getNewCustomer()));
                requirement.setCaOpen(prepareBooleanSet(approvalConfig.getCaOpen()));
                requirement.setRtftWaive(prepareBooleanSet(approvalConfig.getRtftWaive()));

                requirement.setNameChange(prepareBooleanSet(approvalConfig.getNameChange()));
                requirement.setIncome(prepareBooleanSet(approvalConfig.getIncome()));
                requirement.setTitleChange(prepareBooleanSet(approvalConfig.getTitleChange()));
                requirement.setTypeChange(prepareBooleanSet(approvalConfig.getTypeChange()));
                requirement.setCiDetail(prepareBooleanSet(approvalConfig.getCiDetail()));
                requirement.setAddressChange(prepareBooleanSet(approvalConfig.getAddressChange()));
                requirement.setChangePhone(prepareBooleanSet(approvalConfig.getChangePhone()));
                requirement.setChangeEmail(prepareBooleanSet(approvalConfig.getChangeEmail()));
                requirement.setChangeAcctName(prepareBooleanSet(approvalConfig.getChangeAcctName()));
                requirement.setChangeMailAddr(prepareBooleanSet(approvalConfig.getChangeMailAddr()));
                requirement.setChangeAcctMail(prepareBooleanSet(approvalConfig.getChangeAcctMail()));
                requirement.setSignature(prepareBooleanSet(approvalConfig.getChangeSignature()));
                requirement.setConvertPB(prepareBooleanSet(approvalConfig.getConvertPB()));
                requirement.setUplift(prepareBooleanSet(approvalConfig.getUplift()));

                requirement.setKycLevel1(approvalConfig.getKycLevel1());
                requirement.setKycLevel2(approvalConfig.getKycLevel2());
                requirement.setKycLevel3(approvalConfig.getKycLevel3());

                config.getRequirement().add(requirement);
            }
            result.add(config);
        }

        return result;
    }

    private Set<Boolean> prepareBooleanSet(Boolean addMobilePhone) {
        Set<Boolean> result;

        if (addMobilePhone == null) {
            result = Stream.of(true, false).collect(Collectors.toSet());
        } else if (addMobilePhone) {
            result = Collections.singleton(true);
        } else {
            result = Collections.singleton(false);
        }

        return result;
    }

    private void roleDuplicityCheck(PermissionConfig permissionConfig) {
        Set<String> roles = new HashSet<>();
        //check products
        boolean duplicateProduct = ofNullable(permissionConfig.getProducts())
                .map(Collection::stream)
                .map(s -> s.map(RoleProductConfig::getRoles)
                        .filter(CollectionUtils::isNotEmpty)
                        .flatMap(Collection::stream)
                        .anyMatch(r -> !roles.add(r)))
                .orElse(false);

        //check validate
        roles.clear();
        boolean duplicateValidate = ofNullable(permissionConfig.getValidate())
                .map(Collection::stream)
                .map(s -> s.map(RoleValidateConfig::getRoles)
                        .filter(CollectionUtils::isNotEmpty)
                        .flatMap(Collection::stream)
                        .anyMatch(r -> !roles.add(r)))
                .orElse(false);

        //check editable
        roles.clear();
        boolean duplicateEditable = ofNullable(permissionConfig.getEditable())
                .map(Collection::stream)
                .map(s -> s.map(RoleEditableConfig::getRoles)
                        .filter(CollectionUtils::isNotEmpty)
                        .flatMap(Collection::stream)
                        .anyMatch(r -> !roles.add(r)))
                .orElse(false);

        //check additionalRoles
        roles.clear();
        boolean duplicateAdditionalRoles = ofNullable(permissionConfig.getAdditionalRoles())
                .map(Collection::stream)
                .map(s -> s.map(RoleAdditionalRolesConfig::getRoles)
                        .filter(CollectionUtils::isNotEmpty)
                        .flatMap(Collection::stream)
                        .anyMatch(r -> !roles.add(r)))
                .orElse(false);

        try {
            assertDuplicity(duplicateProduct, "products");
            assertDuplicity(duplicateValidate, "validate");
            assertDuplicity(duplicateEditable, "editable");
            assertDuplicity(duplicateAdditionalRoles, "additionalRoles");
        } catch (IllegalArgumentException e) {
            log.error("Validations problem, duplicate role detected in permissions configuration", e);
        }

    }

    private PermissionConfig getPermissionConfig(ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(resourceLoader.getResource(PERMISSION_CONFIG_LOCATION).getInputStream(), PermissionConfig.class);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private void assertDuplicity(boolean duplicity, String property) {
        if (duplicity) {
            throw new IllegalArgumentException("Duplicate role in property " + property);
        }
    }
}
