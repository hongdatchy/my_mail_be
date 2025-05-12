package com.vtidc.mymail.repo;

import com.vtidc.mymail.entities.MailDistributionList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailDistributionListRepository extends JpaRepository<MailDistributionList, Integer> {
    void deleteByEmailId(Integer emailId);
}