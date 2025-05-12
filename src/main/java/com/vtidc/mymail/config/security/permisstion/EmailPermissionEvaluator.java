package com.vtidc.mymail.config.security.permisstion;

import com.vtidc.mymail.entities.User;
import com.vtidc.mymail.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailPermissionEvaluator implements DomainPermissionEvaluator {

    private final UserService userService;

    @Override
    public boolean supports(String permission) {
        return permission.startsWith("email:");
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        User user = (User) authentication.getPrincipal();
        if(targetDomainObject != null) {
            return false;
        }else {
            return userService.getUserDetail(user.getId()).getSaveRoleDtoList().contains(permission.toString());
        }
    }
}

