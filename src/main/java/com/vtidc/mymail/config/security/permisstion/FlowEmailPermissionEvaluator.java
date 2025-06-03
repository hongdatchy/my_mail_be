package com.vtidc.mymail.config.security.permisstion;

import com.vtidc.mymail.config.enums.RoleCode;
import com.vtidc.mymail.config.security.MyUserDetails;
import com.vtidc.mymail.dto.SaveEmailDto;
import com.vtidc.mymail.dto.SaveRoleDto;
import com.vtidc.mymail.dto.SaveUserDto;
import com.vtidc.mymail.dto.search.SearchAccountRequest;
import com.vtidc.mymail.entities.User;
import com.vtidc.mymail.service.EmailService;
import com.vtidc.mymail.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class EmailPermissionEvaluator implements DomainPermissionEvaluator {

    private final UserService userService;
    private final EmailService emailService;

    @Override
    public boolean supports(String permission) {
        return permission.startsWith(RoleCode.email.name());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        User user = ((MyUserDetails) authentication.getPrincipal()).getUser();
        SaveUserDto userDetail = userService.getUserDetail(user.getId());

        return userDetail.getSaveRoleDtoList().stream()
                .filter(role -> role.getRoleCode().equals(RoleCode.email.name()))
                .filter(role -> permission.toString().contains(role.getActionCode()))
                .anyMatch(role -> checkPermission(role, userDetail, targetDomainObject));
    }

    private boolean checkPermission(SaveRoleDto role, SaveUserDto userDetail, Object target) {
        String actionCode = role.getActionCode();
        String scope = String.valueOf(role.getConfigData().getOrDefault("role", ""));

        return switch (actionCode) {
            case "create" -> true;
            case "view" -> checkViewPermission(scope, userDetail, target);
            case "update" -> checkUpdateOrDelete(scope, userDetail, target, true);
            case "delete" -> checkUpdateOrDelete(scope, userDetail, target, false);
            default -> false;
        };
    }

    private boolean checkViewPermission(String scope, SaveUserDto userDetail, Object target) {
        if (target instanceof SearchAccountRequest request) {
            switch (scope) {
                case "Tổ chức" -> request.setOrgId(userDetail.getOrgId());
                case "Cá nhân" -> request.setUsername(userDetail.getUsername());
                case "Toàn hệ thống" -> {}
                default -> {
                    return false;
                }
            }
            return true;
        }
        if (target instanceof Integer id) {
            String createdBy = emailService.getCreatedByById(id);
            return matchScope(scope, userDetail, createdBy);
        }
        return false;
    }

    private boolean checkUpdateOrDelete(String scope, SaveUserDto userDetail, Object target, boolean isUpdate) {
        Integer id = null;
        if (isUpdate && target instanceof SaveEmailDto dto) id = dto.getId();
        if (!isUpdate && target instanceof Integer i) id = i;
        if (id == null) return false;

        String createdBy = emailService.getCreatedByById(id);
        return matchScope(scope, userDetail, createdBy);
    }

    private boolean matchScope(String scope, SaveUserDto userDetail, String createdBy) {
        return switch (scope) {
            case "Cá nhân" -> Objects.equals(userDetail.getUsername(), createdBy);
            case "Tổ chức" -> userService.haveSameOrganization(userDetail.getUsername(), createdBy);
            case "Toàn hệ thống" -> true;
            default -> false;
        };
    }
}
