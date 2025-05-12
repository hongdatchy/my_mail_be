package com.vtidc.mymail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtidc.mymail.config.enums.TagEntityType;
import com.vtidc.mymail.dto.SaveUserApproveEmailDto;
import com.vtidc.mymail.dto.SaveUserDto;
import com.vtidc.mymail.dto.SaveRoleDto;
import com.vtidc.mymail.dto.search.SearchUserRequest;
import com.vtidc.mymail.entities.TagJoin;
import com.vtidc.mymail.entities.User;
import com.vtidc.mymail.entities.UserApproveEmail;
import com.vtidc.mymail.entities.UserConfig;
import com.vtidc.mymail.repo.TagJoinRepository;
import com.vtidc.mymail.repo.UserApproveEmailRepository;
import com.vtidc.mymail.repo.UserConfigRepository;
import com.vtidc.mymail.repo.UserRepository;
import com.vtidc.mymail.repo.projection.UserRoleActionProjection;
import com.vtidc.mymail.ultis.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserConfigRepository userConfigRepository;

    private final TagJoinRepository tagJoinRepository;

    private final UserApproveEmailRepository userApproveEmailRepository;

    public SaveUserDto getUserDetail(Integer userId) {
        List<UserRoleActionProjection> projections = userRepository.getUserDetail(userId, TagEntityType.user.name());

        if (projections.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserRoleActionProjection first = projections.get(0);

        List<SaveRoleDto> roles = projections.stream()
                .filter(p -> p.getRoleId() != null)
                .map(role -> {
                    Map<String, Object> configData = null;
                    try {
                        configData = objectMapper.readValue(role.getConfigData(), Map.class);
                    } catch (Exception ignored) {}
                    return new SaveRoleDto(role.getRoleId(), role.getRoleName(), role.getActionId(), role.getActionName(), configData);
                })
                .distinct()
                .toList();

        List<SaveUserApproveEmailDto> saveUserApproveEmailDtoList = projections.stream()
                .filter(p -> p.getUserSendId() != null)
                .map(p -> new SaveUserApproveEmailDto(p.getId(), p.getUserSendId(), p.getUserSendOrgName(), p.getUserSendMail()))
                .distinct()
                .toList();

        SaveUserDto saveUserDto = new SaveUserDto();
        saveUserDto.setId(first.getId());
        saveUserDto.setUsername(first.getUsername());
        saveUserDto.setMail(first.getMail());
        saveUserDto.setOrgId(first.getOrgId());
        saveUserDto.setOrgName(first.getOrgName());
        saveUserDto.setSaveRoleDtoList(roles);
        saveUserDto.setTagIdList(CommonUtils.jsonArrayToIntegerList(first.getTagIdList()));
        saveUserDto.setSaveUserApproveEmailDtoList(saveUserApproveEmailDtoList);
        saveUserDto.setPassword(first.getPassword());
        return saveUserDto;
    }

    private Page<SaveUserDto> userRoleActionProjectionToSaveUserDto(Page<UserRoleActionProjection> projectionPage) {
        return projectionPage.map(proj -> {
            List<Integer> tagIds = new ArrayList<>();
            try {
                if (proj.getTagIdList() != null) {
                    tagIds = objectMapper.readValue(proj.getTagIdList(), new TypeReference<>() {});
                }
            } catch (Exception e) {
                // Log nếu cần
            }
            SaveUserDto dto = new SaveUserDto();
            dto.setId(proj.getId());
            dto.setUsername(proj.getUsername());
            dto.setOrgName(proj.getOrgName());
            dto.setMail(proj.getMail());
            dto.setTagIdList(tagIds);
            return dto;
        });
    }

    public Page<SaveUserDto> search(SearchUserRequest searchUserRequest) {
        String keyword = searchUserRequest.getKeyword();

        Integer tagId = searchUserRequest.getTagId();
        int page = searchUserRequest.getPage();
        int size = searchUserRequest.getSize();
        Sort sort = Sort.unsorted();
        if (searchUserRequest.getSortAscending() != null && searchUserRequest.getSortBy() != null) {
            Sort.Direction direction = Sort.Direction.fromString(searchUserRequest.getSortAscending());
            sort = Sort.by(direction, searchUserRequest.getSortBy());
        }

        if (keyword != null &&keyword.matches(".*[+\\-@~<>*()\"].*")) {
            keyword = "\"" + keyword + "\"";
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        if(keyword == null || keyword.length() <= 2){
            if(tagId == null){
                return userRoleActionProjectionToSaveUserDto(userRepository.findAllProjection(TagEntityType.user.name(), pageable));
            }else {
                return userRoleActionProjectionToSaveUserDto(userRepository.findByTagId(TagEntityType.user.name(), tagId, pageable));
            }
        }

        if(tagId == null){
            return userRoleActionProjectionToSaveUserDto(userRepository.fullTextSearch(TagEntityType.user.name(), keyword, pageable));
        }
        return userRoleActionProjectionToSaveUserDto(userRepository.fullTextSearch(TagEntityType.user.name(), keyword, tagId, pageable));
    }

    @Transactional
    public User createUser(SaveUserDto requestCreateUserDto) {
        User user = new User();
        user.setUsername(requestCreateUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestCreateUserDto.getPassword()));
        user.setMail(requestCreateUserDto.getMail());
        user.setOrgId(requestCreateUserDto.getOrgId());
        user = userRepository.save(user);

        if(requestCreateUserDto.getSaveRoleDtoList() != null){
            List<UserConfig> userConfigs = new ArrayList<>();
            for (SaveRoleDto roleDto : requestCreateUserDto.getSaveRoleDtoList()) {
                UserConfig userConfig = new UserConfig();
                userConfig.setUserId(user.getId());
                userConfig.setRoleId(roleDto.getRoleId());
                userConfig.setActionId(roleDto.getActionId());
                userConfig.setConfigData(roleDto.getConfigData());
                userConfigs.add(userConfig);
            }
            userConfigRepository.saveAll(userConfigs);

        }
        if (requestCreateUserDto.getTagIdList() != null) {
            List<TagJoin> tagJoins = new ArrayList<>();
            for (Integer tagId : requestCreateUserDto.getTagIdList()) {
                tagJoins.add(new TagJoin(null, tagId, user.getId(), TagEntityType.user.name()));
            }
            tagJoinRepository.saveAll(tagJoins);
        }

        if (requestCreateUserDto.getSaveUserApproveEmailDtoList() != null) {
            List<UserApproveEmail> userApproveEmails = new ArrayList<>();
            for (SaveUserApproveEmailDto saveUserApproveEmailDto : requestCreateUserDto.getSaveUserApproveEmailDtoList()) {
                userApproveEmails.add(new UserApproveEmail(null, saveUserApproveEmailDto.getUserApproveId(), saveUserApproveEmailDto.getUserSendId()));
            }
            userApproveEmailRepository.saveAll(userApproveEmails);
        }

        return user;
    }

    @Transactional
    public User updateUser(SaveUserDto requestUpdateUserDto) {
        SaveUserDto saveUserDto = getUserDetail(requestUpdateUserDto.getId());
        User user = new User();
        user.setId(requestUpdateUserDto.getId());
        user.setUsername(requestUpdateUserDto.getUsername());
        if(requestUpdateUserDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(requestUpdateUserDto.getPassword()));
        } else {
            user.setPassword(saveUserDto.getPassword());
        }
        user.setMail(requestUpdateUserDto.getMail());
        user.setOrgId(requestUpdateUserDto.getOrgId());
        user = userRepository.save(user);
        User finalUser = user;
        if(requestUpdateUserDto.getSaveRoleDtoList() != null){
            CommonUtils.DiffResult<SaveRoleDto> diffLists = CommonUtils.diffLists(saveUserDto.getSaveRoleDtoList(),
                    requestUpdateUserDto.getSaveRoleDtoList(),
                    item -> List.of(item.getRoleId(), item.getActionId()));

            userConfigRepository.saveAll(diffLists.toAdd.stream().map(saveRoleDto -> {
                UserConfig userConfig = new UserConfig();
                userConfig.setId(null);
                userConfig.setUserId(finalUser.getId());
                userConfig.setActionId(saveRoleDto.getActionId());
                userConfig.setConfigData(saveRoleDto.getConfigData());
                userConfig.setRoleId(saveRoleDto.getRoleId());
                return userConfig;
            }).collect(Collectors.toList()));
            for (SaveRoleDto dto : diffLists.toDelete) {
                userConfigRepository.deleteAllByActionIdAndRoleIdAndUserId(dto.getActionId(), dto.getRoleId(), finalUser.getId());
            }
        }
        if(requestUpdateUserDto.getSaveUserApproveEmailDtoList() != null){
            CommonUtils.DiffResult<SaveUserApproveEmailDto> diffLists = CommonUtils.diffLists(saveUserDto.getSaveUserApproveEmailDtoList(),
                    requestUpdateUserDto.getSaveUserApproveEmailDtoList(),
                    item -> List.of(item.getUserApproveId(), item.getUserSendId()));

            userApproveEmailRepository.saveAll(diffLists.toAdd.stream().map(saveRoleDto -> {
                UserApproveEmail userApproveEmail = new UserApproveEmail();
                userApproveEmail.setId(null);
                userApproveEmail.setIdUserSend(saveRoleDto.getUserSendId());
                userApproveEmail.setIdUserApprove(saveRoleDto.getUserApproveId());
                return userApproveEmail;
            }).collect(Collectors.toList()));
            for (SaveUserApproveEmailDto dto : diffLists.toDelete) {
                userApproveEmailRepository.deleteAllByIdUserApproveAndIdUserSend(dto.getUserApproveId(), dto.getUserSendId());
            }
        }
        if (requestUpdateUserDto.getTagIdList() != null) {
            List<Integer> tagIdListOld = tagJoinRepository.findByEntityIdAndType(user.getId(), TagEntityType.user.name())
                    .stream().map(TagJoin::getTagId).toList();
            CommonUtils.DiffResult<Integer> diffResult = CommonUtils.diffLists(
                    tagIdListOld, requestUpdateUserDto.getTagIdList(), i -> i);

            for (Integer tagId : diffResult.toDelete) {
                tagJoinRepository.deleteByEntityIdAndTagIdAndType(user.getId(), tagId, TagEntityType.user.name());
            }

            for (Integer tagId : diffResult.toAdd) {
                tagJoinRepository.save(new TagJoin(null, tagId, user.getId(), TagEntityType.user.name()));
            }
        }
        return user;

    }
}

