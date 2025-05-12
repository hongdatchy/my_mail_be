package com.vtidc.mymail.config.security.permisstion;

import org.springframework.security.core.Authentication;

public interface DomainPermissionEvaluator {
    boolean supports(String permission);
    boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission);
}

