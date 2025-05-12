package com.vtidc.mymail.config.security.permisstion;

import com.vtidc.mymail.entities.User;
import com.vtidc.mymail.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component("permissionChecker")
@AllArgsConstructor
public class PermissionChecker implements PermissionEvaluator {

    private final List<DomainPermissionEvaluator> evaluators;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (permission == null) {
            return false;
        }

        return evaluators.stream()
                .filter(e -> e.supports((String) permission))
                .findFirst()
                .map(e -> e.hasPermission(authentication, targetDomainObject,  permission))
                .orElse(false);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return true;
    }
}

