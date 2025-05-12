package com.vtidc.mymail.repo;

import com.vtidc.mymail.entities.DistributionList;
import com.vtidc.mymail.repo.projection.DistributionListProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DistributionListRepository extends JpaRepository<DistributionList, Integer> {
    @Query(value = """
                SELECT dl.* FROM distribution_list dl
                LEFT JOIN tag_join tj ON tj.entity_id = dl.id AND tj.type = :typeTag
                WHERE MATCH(dl.mail, dl.display_name) AGAINST (:keyword IN BOOLEAN MODE)
                AND tj.tag_id = :tagId
            """,
            countQuery = """
                SELECT COUNT(dl.id) FROM distribution_list dl
                LEFT JOIN tag_join tj ON tj.entity_id = dl.id AND tj.type = :typeTag
                WHERE MATCH(dl.mail, dl.display_name) AGAINST (:keyword IN BOOLEAN MODE)
                AND tj.tag_id = :tagId
                """,
            nativeQuery = true)
    Page<DistributionList> fullTextSearch(String typeTag, String keyword, Integer tagId, Pageable pageable);


    @Query(value = "SELECT * FROM distribution_list WHERE MATCH(mail, display_name) AGAINST(?1 IN BOOLEAN MODE)",
            countQuery = "SELECT count(*) FROM distribution_list WHERE MATCH(display_name) AGAINST(?1 IN BOOLEAN MODE)",
            nativeQuery = true)
    Page<DistributionList> fullTextSearch(String keyword, Pageable pageable);

    @Query(value = """
        SELECT dl
        FROM DistributionList dl
        LEFT JOIN TagJoin tj ON tj.entityId = dl.id AND tj.type = :typeTag
        WHERE tj.tagId = :tagId
    """, countQuery = """
        SELECT COUNT(dl)
        FROM DistributionList dl
        LEFT JOIN TagJoin tj ON tj.entityId = dl.id AND tj.type = :typeTag
        WHERE tj.tagId = :tagId
    """)
    Page<DistributionList> findByTagId(String typeTag, Integer tagId, Pageable pageable);


    @Query(value = """
            SELECT 
                dl.id AS id,
                dl.created_by AS createdBy,
                dl.created_date AS createdDate,
                dl.updated_by AS updatedBy,
                dl.updated_date AS updatedDate,
                dl.zimbra_id AS zimbraId,
                tj.tag_id AS tagId,
                dl.display_name AS displayName,
                dl.mail AS mail,
                e.id AS memberId,
                e.mail AS memberMail,
                e.display_name AS memberDisplayName
            FROM distribution_list dl
            LEFT JOIN mail_distribution_list mdl ON dl.id = mdl.distribution_list_id
            LEFT JOIN email e ON mdl.email_id = e.id
            LEFT JOIN tag_join tj ON tj.entity_id = dl.id AND tj.type = :typeTag
            WHERE dl.id = :id
            """, nativeQuery = true)
    List<DistributionListProjection> getDetailsById(Integer id, String typeTag);


    Optional<DistributionList> findByMail(String mail);

}