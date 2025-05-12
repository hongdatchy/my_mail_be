package com.vtidc.mymail.repo;

import com.vtidc.mymail.entities.UserConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserConfigRepository extends JpaRepository<UserConfig, Integer> {

    @Transactional
    @Modifying
    void deleteAllByActionIdAndRoleIdAndUserId(Integer actionId, Integer roleId, Integer userId);

}