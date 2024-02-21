package th.co.scb.onboardingapp.service;



import th.co.scb.onboardingapp.model.approval.Permissions;

import java.util.Set;

public interface PermissionService {
    Permissions retrievePermissions(Set<String> roles);
}
