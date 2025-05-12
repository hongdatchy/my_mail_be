package com.vtidc.mymail.repo;

import com.vtidc.mymail.entities.User;
import com.vtidc.mymail.repo.projection.UserRoleActionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User getFirstUserByUsername(String username);

    @Query(value = """
            SELECT
                u.id AS id,
                u.username AS username,
                u.mail AS mail,
                r.id AS roleId,
                r.name AS roleName,
                a.id AS actionId,
                a.name AS actionName,
                uc.config_data AS configData,
                o.id AS orgId,
                o.name AS orgName,
                JSON_ARRAYAGG(tj.tag_id) AS tagIdList,
                u.id AS userApproveEmailId,
                user_send.id AS userSendId,
                org_send.name AS userSendOrgName,
                user_send.mail AS userSendMail,
                u.password
            FROM `user` u
            LEFT JOIN user_config uc ON uc.user_id = u.id
            LEFT JOIN `role` r ON uc.role_id = r.id
            LEFT JOIN `action` a ON uc.action_id = a.id
            LEFT JOIN `organization` o ON u.org_id = o.id
            LEFT JOIN tag_join tj ON tj.entity_id = u.id AND tj.type = :typeTag
            LEFT JOIN user_approve_email uae ON uae.id_user_approve = u.id
            LEFT JOIN user user_send ON uae.id_user_send = user_send.id
            LEFT JOIN organization org_send ON user_send.org_id = org_send.id
            WHERE u.id = :userId
            GROUP BY
                u.id, u.username, u.mail,
                r.id, r.name,
                a.id, a.name,
                uc.config_data,
                o.id, o.name,
                uae.id,
                user_send.id, user_send.username, user_send.mail
            """, nativeQuery = true)
    List<UserRoleActionProjection> getUserDetail(Integer userId, String typeTag);


    @Query(value = """
            SELECT
                u.id,
                u.username AS username,
                u.mail AS mail,
                o.name AS orgName,
                JSON_ARRAYAGG(tj.tag_id) AS tagIdList
            FROM user u
            LEFT JOIN tag_join tj ON tj.entity_id = u.id AND tj.type = :typeTag
            LEFT JOIN `organization` o ON u.org_id = o.id
            WHERE MATCH(u.username, u.mail) AGAINST (:keyword IN BOOLEAN MODE)
            GROUP BY u.id
            HAVING JSON_CONTAINS(JSON_ARRAYAGG(tj.tag_id), CAST(CONCAT('[', :tagId, ']') AS JSON))
            """,
            countQuery = """
            SELECT COUNT(DISTINCT u.id)
            FROM user u
            LEFT JOIN tag_join tj ON tj.entity_id = u.id AND tj.type = :typeTag
            WHERE MATCH(u.username, u.mail) AGAINST (:keyword IN BOOLEAN MODE)
            GROUP BY u.id
            HAVING JSON_CONTAINS(JSON_ARRAYAGG(tj.tag_id), CAST(CONCAT('[', :tagId, ']') AS JSON))
            """,
            nativeQuery = true)
    Page<UserRoleActionProjection> fullTextSearch(String typeTag, String keyword, Integer tagId, Pageable pageable);


    @Query(value = """
            SELECT
                u.id,
                o.name AS orgName,
                u.username AS username,
                u.mail AS mail,
                JSON_ARRAYAGG(tj.tag_id) AS tagIdList
            FROM user u
            LEFT JOIN tag_join tj ON tj.entity_id = u.id AND tj.type = :typeTag
            LEFT JOIN `organization` o ON u.org_id = o.id
            WHERE MATCH(u.username, u.mail) AGAINST (:keyword IN BOOLEAN MODE)
            GROUP BY u.id
            """,
            countQuery = """
            SELECT COUNT(DISTINCT u.id)
            FROM user u
            WHERE MATCH(u.username, u.mail) AGAINST (:keyword IN BOOLEAN MODE)
            """,
            nativeQuery = true)
    Page<UserRoleActionProjection> fullTextSearch(String typeTag, String keyword, Pageable pageable);


    @Query(value = """
            SELECT
                u.id,
                u.username AS username,
                u.mail AS mail,
                o.name AS orgName,
                JSON_ARRAYAGG(tj.tag_id) AS tagIdList
            FROM user u
            LEFT JOIN `organization` o ON u.org_id = o.id
            LEFT JOIN tag_join tj ON tj.entity_id = u.id AND tj.type = :typeTag
            GROUP BY u.id
            HAVING JSON_CONTAINS(JSON_ARRAYAGG(tj.tag_id), CAST(CONCAT('[', :tagId, ']') AS JSON))
            """,
    countQuery = """
            SELECT COUNT(DISTINCT u.id)
            FROM user u
            LEFT JOIN tag_join tj ON tj.entity_id = u.id AND tj.type = :typeTag
            GROUP BY u.id
            HAVING JSON_CONTAINS(JSON_ARRAYAGG(tj.tag_id), CAST(CONCAT('[', :tagId, ']') AS JSON))
            """, nativeQuery = true)
    Page<UserRoleActionProjection> findByTagId(String typeTag, Integer tagId, Pageable pageable);

    @Query(value = """
            SELECT
                u.id,
                u.username AS username,
                o.name AS orgName,
                u.mail AS mail,
                JSON_ARRAYAGG(tj.tag_id) AS tagIdList
            FROM user u
            LEFT JOIN tag_join tj ON tj.entity_id = u.id AND tj.type = :typeTag
            LEFT JOIN `organization` o ON u.org_id = o.id
            GROUP BY u.id
            """,
            countQuery = """
            SELECT COUNT(DISTINCT u.id)
            FROM user u
            """, nativeQuery = true)
    Page<UserRoleActionProjection> findAllProjection(String typeTag, Pageable pageable);

}