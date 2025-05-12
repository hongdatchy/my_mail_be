package com.vtidc.mymail.repo;

import com.vtidc.mymail.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
}