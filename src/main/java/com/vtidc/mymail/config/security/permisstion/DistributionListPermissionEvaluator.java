package com.vtidc.mymail.config.security.permisstion;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class DistributionListPermissionEvaluator implements DomainPermissionEvaluator {

    @Override
    public boolean supports(String permission) {
        return permission.startsWith("distribution:");
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }
}

