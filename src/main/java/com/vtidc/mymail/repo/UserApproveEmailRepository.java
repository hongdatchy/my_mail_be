package com.vtidc.mymail.repo;

import com.vtidc.mymail.entities.UserApproveOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserApproveOrganizationRepository extends JpaRepository<UserApproveOrganization, Integer> {

    @Transactional
    @Modifying
    void deleteAllByIdUserApproveAndIdUserSend(Integer userApproveId, Integer userSendId);

    void deleteByIdUserApproveOrIdUserSend(Integer userApproveId, Integer userSendId);
}