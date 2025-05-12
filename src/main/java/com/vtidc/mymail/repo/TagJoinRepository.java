package com.vtidc.mymail.repo;

import com.vtidc.mymail.entities.TagJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TagJoinRepository extends JpaRepository<TagJoin, Integer> {
    List<TagJoin> findByEntityIdAndType(Integer entityId, String tagId);

    @Transactional
    @Modifying
    void deleteByEntityIdAndTagIdAndType(Integer id, Integer tagId, String name);

    @Transactional
    @Modifying
    void deleteByEntityIdAndType(Integer id, String name);
}