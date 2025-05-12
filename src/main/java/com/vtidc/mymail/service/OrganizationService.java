package com.vtidc.mymail.service;

import com.vtidc.mymail.config.enums.OrgStatus;
import com.vtidc.mymail.config.enums.TagEntityType;
import com.vtidc.mymail.dto.EmailDto;
import com.vtidc.mymail.dto.SaveEmailDto;
import com.vtidc.mymail.dto.SaveOrganizationDto;
import com.vtidc.mymail.dto.search.SearchOrgRequest;
import com.vtidc.mymail.dto.zimbra.OrganizationDto;
import com.vtidc.mymail.entities.Organization;
import com.vtidc.mymail.entities.TagJoin;
import com.vtidc.mymail.repo.OrganizationRepository;
import com.vtidc.mymail.repo.TagJoinRepository;
import com.vtidc.mymail.repo.projection.OrganizationWithEmailDtoProjection;
import com.vtidc.mymail.ultis.CommonUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final TagJoinRepository tagJoinRepository;

    private final EmailService emailService;

    public Page<Organization> search(SearchOrgRequest searchOrgRequest) {
        String keyword = searchOrgRequest.getKeyword();
        int page = searchOrgRequest.getPage();
        int size = searchOrgRequest.getSize();
        Integer tagId = searchOrgRequest.getTagId();

        Sort sort = Sort.unsorted();
        if (searchOrgRequest.getSortAscending() != null && searchOrgRequest.getSortBy() != null) {
            Sort.Direction direction = Sort.Direction.fromString(searchOrgRequest.getSortAscending());
            sort = Sort.by(direction, searchOrgRequest.getSortBy());
        }

        if (keyword != null &&keyword.matches(".*[+\\-@~<>*()\"].*")) {
            keyword = "\"" + keyword + "\"";
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        if(keyword == null || keyword.length() <= 2) {
            if(tagId == null) {
                return organizationRepository.findAll(pageable);
            }else {
                return organizationRepository.findByTagId(TagEntityType.distribution_list.name(), tagId, pageable);
            }
        }

        if(tagId == null) {
            return organizationRepository.fullTextSearch(keyword, pageable);
        }
        return organizationRepository.fullTextSearch(TagEntityType.distribution_list.name(), keyword, tagId, pageable);
    }

    public Organization addOrganization(SaveOrganizationDto saveOrganizationDto) {
        Organization organization = new Organization();

        organization.setName(saveOrganizationDto.getName());
        organization.setTradeName(saveOrganizationDto.getTradeName());
        organization.setCompanyType(saveOrganizationDto.getCompanyType());
        organization.setTaxCode(saveOrganizationDto.getTaxCode());
        organization.setAddress(saveOrganizationDto.getAddress());
        organization.setLegalRepresentative(saveOrganizationDto.getLegalRepresentative());
        organization.setLicenseIssuedDate(saveOrganizationDto.getLicenseIssuedDate());
        organization.setOperationStartDate(saveOrganizationDto.getOperationStartDate());
        organization.setPhoneNumber(saveOrganizationDto.getPhoneNumber());
        organization.setLicenseFile(saveOrganizationDto.getLicenseFile());

        organization = organizationRepository.save(organization);

        if(saveOrganizationDto.getTagIdList() != null) {
            for (Integer tagId : saveOrganizationDto.getTagIdList()) {
                tagJoinRepository.save(new TagJoin(null, tagId, organization.getId(), TagEntityType.organization.name()));
            }
        }
        return organization;
    }

    public Organization editOrganization(SaveOrganizationDto saveOrganizationDto) {
        Organization organization = organizationRepository.findById(saveOrganizationDto.getId()).orElse(null);
        if(organization == null) {
            throw new RuntimeException("Tổ chức k tồn tại");
        }

        organization.setName(saveOrganizationDto.getName());
        organization.setTradeName(saveOrganizationDto.getTradeName());
        organization.setCompanyType(saveOrganizationDto.getCompanyType());
        organization.setTaxCode(saveOrganizationDto.getTaxCode());
        organization.setAddress(saveOrganizationDto.getAddress());
        organization.setLegalRepresentative(saveOrganizationDto.getLegalRepresentative());
        organization.setLicenseIssuedDate(saveOrganizationDto.getLicenseIssuedDate());
        organization.setOperationStartDate(saveOrganizationDto.getOperationStartDate());
        organization.setPhoneNumber(saveOrganizationDto.getPhoneNumber());
        organization.setLicenseFile(saveOrganizationDto.getLicenseFile());

        organization = organizationRepository.save(organization);
        List<Integer> tagIdListOld = tagJoinRepository.findByEntityIdAndType(organization.getId(), TagEntityType.organization.name())
                .stream().map(TagJoin::getTagId).toList();
        CommonUtils.DiffResult<Integer> diffResultTagId = CommonUtils.diffLists(
                tagIdListOld, saveOrganizationDto.getTagIdList(), i -> i);

        for (Integer tagId : diffResultTagId.toDelete) {
            tagJoinRepository.deleteByEntityIdAndTagIdAndType(organization.getId(), tagId, TagEntityType.distribution_list.name());
        }

        for (Integer tagId : diffResultTagId.toAdd) {
            tagJoinRepository.save(new TagJoin(null, tagId, organization.getId(), TagEntityType.distribution_list.name()));
        }
        return organization;
    }

    public boolean deleteOrganization(Integer id) {
        Optional<Organization> optionalOrganization = organizationRepository.findById(id);
        if (optionalOrganization.isPresent()) {
            Organization organization = optionalOrganization.get();
            organizationRepository.deleteById(organization.getId());
            tagJoinRepository.deleteByEntityIdAndType(organization.getId(), TagEntityType.distribution_list.name());
            // k xóa email trong tổ chức
            return true;
        }
        return false;
    }

    public OrganizationDto getDetails(Integer id) {
        List<OrganizationWithEmailDtoProjection> projections = organizationRepository.getDetailById(id);

        if (projections.isEmpty()) return null;

        OrganizationWithEmailDtoProjection first = projections.get(0);
        OrganizationDto dto = new OrganizationDto();
        dto.setId(first.getId());
        dto.setName(first.getName());
        dto.setStatus(first.getStatus());
        dto.setTradeName(first.getTradeName());
        dto.setCompanyType(first.getCompanyType());
        dto.setTaxCode(first.getTaxCode());
        dto.setAddress(first.getAddress());
        dto.setLegalRepresentative(first.getLegalRepresentative());
        dto.setLicenseIssuedDate(first.getLicenseIssuedDate());
        dto.setOperationStartDate(first.getOperationStartDate());
        dto.setPhoneNumber(first.getPhoneNumber());
        dto.setLicenseFile(first.getLicenseFile());
        if(first.getEmailId() != null && first.getEmailMail() != null && first.getDisplayName() != null) {
            dto.setEmail(new EmailDto(first.getEmailId(), first.getEmailMail(), first.getDisplayName()));
        }
        return dto;
    }

    @Transactional
    public void approveOrganization(Integer id, SaveEmailDto emailDto) {
        Optional<Organization> optionalOrganization = organizationRepository.findById(id);
        if (optionalOrganization.isPresent()) {
            if(optionalOrganization.get().getStatus().equals(OrgStatus.active.name())) {
                throw new RuntimeException("Tổ chức đã được phê duyệt");
            }
            Organization organization = optionalOrganization.get();
            organization.setMail(emailDto.getMail());
            organization.setStatus(OrgStatus.active.name());
            organizationRepository.save(organization);
            emailDto.setOrgId(id);
            emailService.createEmail(emailDto);
        }else {
            throw new RuntimeException("Tổ chức k tồn tại");
        }
    }
}
