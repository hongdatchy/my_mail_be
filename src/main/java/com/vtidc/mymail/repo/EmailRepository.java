package com.vtidc.mymail.repo;

import com.vtidc.mymail.entities.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Integer> {

    @Query(value = """
                SELECT e.* FROM email e
                LEFT JOIN tag_join tj ON tj.entity_id = e.id AND tj.type = :typeTag
                WHERE MATCH(e.mail, e.display_name) AGAINST(:keyword IN BOOLEAN MODE)
                  AND e.type = :type
                  AND tj.tag_id = :tagId
            """,
            countQuery = """
                SELECT COUNT( e.id) FROM email e
                LEFT JOIN tag_join tj ON tj.entity_id = e.id AND tj.type = :typeTag
                WHERE MATCH(e.mail, e.display_name) AGAINST(:keyword IN BOOLEAN MODE)
                  AND e.type = :type
                  AND tj.tag_id = :tagId
                """,
            nativeQuery = true)
    Page<Email> fullTextSearch(String typeTag, String keyword, String type, Integer tagId, Pageable pageable);

    @Query(value = "SELECT * FROM email WHERE MATCH(mail, display_name) AGAINST(?1 IN BOOLEAN MODE) AND TYPE = ?2",
            countQuery = "SELECT count(*) FROM email WHERE MATCH(mail, display_name) AGAINST(?1 IN BOOLEAN MODE) AND TYPE = ?2",
            nativeQuery = true)
    Page<Email> fullTextSearch(String keyword, String type, Pageable pageable);

    @Query("""
                SELECT e FROM Email e
                JOIN TagJoin tj ON tj.entityId = e.id
                WHERE tj.tagId = :tagId AND tj.type = :typeTag AND e.type = :type
            """)
    Page<Email> findByTypeAndTagId(String typeTag, String type, Integer tagId, Pageable pageable);

    Page<Email> findByType(String type, Pageable pageable);

    Optional<Email> findByMail(String mail);

}