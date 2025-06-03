package com.vtidc.mymail.config.security.permisstion;

import com.vtidc.mymail.config.enums.ActionCode;
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

import java.util.List;

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

        SaveUserDto saveUserDto = userService.getUserDetail(user.getId());
        List<SaveRoleDto> saveRoleDtoList = saveUserDto.getSaveRoleDtoList()
                .stream().filter(role -> role.getRoleCode().equals(RoleCode.email.name())).toList();
        for (SaveRoleDto saveRoleDto : saveRoleDtoList) {
            if ((permission.toString().contains(saveRoleDto.getActionCode()))) {
                if(saveRoleDto.getActionCode().equals(ActionCode.create.name())) {
                    return true;
                } else {
                    if(saveRoleDto.getActionCode().equals(ActionCode.view.name())) {
                        if (targetDomainObject != null) {
                            if(targetDomainObject instanceof SearchAccountRequest searchAccountRequest){
                                if("Tổ chức".equals(saveRoleDto.getConfigData().get("role"))){
                                    searchAccountRequest.setOrgId(user.getOrgId());
                                }
                                if("Cá nhân".equals(saveRoleDto.getConfigData().get("role"))){
                                    searchAccountRequest.setUsername(user.getUsername());
                                }
                                return true;
                            } else if(targetDomainObject instanceof Integer id){
                                String createdBy = emailService.getCreatedByById(id);
                                if("Cá nhân".equals(saveRoleDto.getConfigData().get("role"))){
                                    return user.getUsername().equals(createdBy);
                                }
                                if("Tổ chức".equals(saveRoleDto.getConfigData().get("role"))){
                                    return userService.haveSameOrganization(user.getUsername(), createdBy);
                                }
                                return "Toàn hệ thống".equals(saveRoleDto.getConfigData().get("role"));
                            }

                        }
                        return false;
                    } else if(saveRoleDto.getActionCode().equals(ActionCode.update.name())) {
                        if (targetDomainObject != null) {
                            if(targetDomainObject instanceof SaveEmailDto saveEmailDto){
                                if("Cá nhân".equals(saveRoleDto.getConfigData().get("role"))){
                                    return user.getUsername().equals(saveEmailDto.getCreatedBy());
                                }
                                if("Tổ chức".equals(saveRoleDto.getConfigData().get("role"))){
                                    return userService.haveSameOrganization(user.getUsername(), saveEmailDto.getCreatedBy());
                                }
                                return "Toàn hệ thống".equals(saveRoleDto.getConfigData().get("role"));
                            }
                        }
                        return false;
                    } else if(saveRoleDto.getActionCode().equals(ActionCode.delete.name())) {
                        if (targetDomainObject != null) {
                            if(targetDomainObject instanceof Integer id){
                                String createdBy = emailService.getCreatedByById(id);
                                if("Cá nhân".equals(saveRoleDto.getConfigData().get("role"))){
                                    return user.getUsername().equals(createdBy);
                                }
                                if("Tổ chức".equals(saveRoleDto.getConfigData().get("role"))){
                                    return userService.haveSameOrganization(user.getUsername(), createdBy);
                                }
                                return "Toàn hệ thống".equals(saveRoleDto.getConfigData().get("role"));
                            }
                        }
                        return false;
                    }
                }
            }
        }
        return false;
    }
}

