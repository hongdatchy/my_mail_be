package com.vtidc.mymail.repo;

import com.vtidc.mymail.entities.UserApproveEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserApproveEmailRepository extends JpaRepository<UserApproveEmail, Integer> {

    @Transactional
    @Modifying
    void deleteAllByIdUserApproveAndIdUserSend(Integer userApproveId, Integer userSendId);
}