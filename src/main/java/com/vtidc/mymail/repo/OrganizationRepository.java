package com.vtidc.mymail.repo;

import com.vtidc.mymail.entities.DistributionList;
import com.vtidc.mymail.entities.Organization;
import com.vtidc.mymail.repo.projection.OrganizationWithEmailDtoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

    @Query(value = """
                SELECT org.* FROM organization org
                LEFT JOIN tag_join tj ON tj.entity_id = org.id AND tj.type = :typeTag
                WHERE MATCH(org.name) AGAINST (:keyword IN BOOLEAN MODE)
                AND tj.tag_id = :tagId
            """,
            countQuery = """
                SELECT COUNT(org.id) FROM organization org
                LEFT JOIN tag_join tj ON tj.entity_id = org.id AND tj.type = :typeTag
                WHERE MATCH(org.name) AGAINST (:keyword IN BOOLEAN MODE)
                AND tj.tag_id = :tagId
                """,
            nativeQuery = true)
    Page<Organization> fullTextSearch(String typeTag, String keyword, Integer tagId, Pageable pageable);

    @Query(value = "SELECT * FROM organization WHERE MATCH(name) AGAINST(?1 IN BOOLEAN MODE)",
            countQuery = "SELECT count(*) FROM organization WHERE MATCH(name) AGAINST(?1 IN BOOLEAN MODE)",
            nativeQuery = true)
    Page<Organization> fullTextSearch(String keyword, Pageable pageable);

    @Query(value = """
        SELECT org
        FROM Organization org
        LEFT JOIN TagJoin tj ON tj.entityId = org.id AND tj.type = :typeTag
        WHERE tj.tagId = :tagId
    """, countQuery = """
        SELECT COUNT(org)
        FROM Organization org
        LEFT JOIN TagJoin tj ON tj.entityId = org.id AND tj.type = :typeTag
        WHERE tj.tagId = :tagId
    """)
    Page<Organization> findByTagId(String typeTag, Integer tagId, Pageable pageable);

    @Query(value = """
    SELECT 
        o.id AS id,
        o.name AS name,
        o.mail AS mail,
        o.status AS status,
        o.trade_name AS tradeName,
        o.company_type AS companyType,
        o.tax_code AS taxCode,
        o.address AS address,
        o.legal_representative AS legalRepresentative,
        o.license_issued_date AS licenseIssuedDate,
        o.operation_start_date AS operationStartDate,
        o.phone_number AS phoneNumber,
        o.license_file AS licenseFile,

        e.id AS emailId,
        e.zimbra_id AS zimbraId,
        e.display_name AS displayName,
        e.mail AS emailMail,
        e.type AS type

    FROM organization o
    LEFT JOIN email e ON o.id = e.org_id
    WHERE o.id = :orgId
    """, nativeQuery = true)
    List<OrganizationWithEmailDtoProjection> getDetailById(Integer orgId);


}