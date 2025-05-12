package com.vtidc.mymail.service;

import com.vtidc.mymail.config.enums.TagEntityType;
import com.vtidc.mymail.dto.SaveDistributionListDto;
import com.vtidc.mymail.dto.EmailDto;
import com.vtidc.mymail.dto.search.SearchDistributionListRequest;
import com.vtidc.mymail.dto.zimbra.*;
import com.vtidc.mymail.entities.DistributionList;
import com.vtidc.mymail.entities.MailDistributionList;
import com.vtidc.mymail.entities.TagJoin;
import com.vtidc.mymail.repo.DistributionListRepository;
import com.vtidc.mymail.repo.MailDistributionListRepository;
import com.vtidc.mymail.repo.TagJoinRepository;
import com.vtidc.mymail.repo.projection.DistributionListProjection;
import com.vtidc.mymail.ultis.CommonUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class DistributionListService {

    private final DistributionListRepository distributionListRepository;

    private final MailDistributionListRepository mailDistributionListRepository;

    private final ZimbraService zimbraService;

    private final TagJoinRepository tagJoinRepository;

    public Page<DistributionList> search(SearchDistributionListRequest searchDistributionListRequest) {
        String keyword = searchDistributionListRequest.getKeyword();
        int page = searchDistributionListRequest.getPage();
        int size = searchDistributionListRequest.getSize();
        Integer tagId = searchDistributionListRequest.getTagId();

        Sort sort = Sort.unsorted();
        if (searchDistributionListRequest.getSortAscending() != null && searchDistributionListRequest.getSortBy() != null) {
            Sort.Direction direction = Sort.Direction.fromString(searchDistributionListRequest.getSortAscending());
            sort = Sort.by(direction, searchDistributionListRequest.getSortBy());
        }

        if (keyword != null &&keyword.matches(".*[+\\-@~<>*()\"].*")) {
            keyword = "\"" + keyword + "\"";
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        if(keyword == null || keyword.length() <= 2) {
            if(tagId == null) {
                return distributionListRepository.findAll(pageable);
            }else {
                return distributionListRepository.findByTagId(TagEntityType.distribution_list.name(), tagId, pageable);
            }
        }

        if(tagId == null) {
            return distributionListRepository.fullTextSearch(keyword, pageable);
        }
        return distributionListRepository.fullTextSearch(TagEntityType.distribution_list.name(), keyword, tagId, pageable);
    }

    public SaveDistributionListDto getDetails(Integer id) {
        List<DistributionListProjection> projections = distributionListRepository.getDetailsById(id, TagEntityType.distribution_list.name());
        if (projections == null || projections.isEmpty()) {
            throw new RuntimeException("Không tìm thấy nhóm mail");
        }

        DistributionListProjection first = projections.get(0);
        SaveDistributionListDto dto = new SaveDistributionListDto();
        dto.setId(first.getId());
        dto.setZimbraId(first.getZimbraId());

        dto.setDisplayName(first.getDisplayName());
        dto.setMail(first.getMail());

        List<EmailDto> emailDtoList = projections.stream()
                .filter(p -> p.getMemberId() != null)
                .map(p -> {
                    EmailDto emailDto = new EmailDto();
                    emailDto.setId(p.getMemberId());
                    emailDto.setMail(p.getMemberMail());
                    emailDto.setDisplayName(p.getMemberDisplayName());
                    return emailDto;
                })
                .collect(Collectors.toList());
        dto.setEmailDtoList(emailDtoList);
        List<Integer> tagList = projections.stream()
                .map(DistributionListProjection::getTagId)
                .filter(Objects::nonNull)
                .toList();
        dto.setTagIdList(tagList);
        return dto;
    }

    public SaveDistributionListDto addDistributionList(SaveDistributionListDto saveDistributionListDto) {
        if(distributionListRepository.findByMail(saveDistributionListDto.getMail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }
        List<Attr> attributes = new ArrayList<>();
        attributes.add(new Attr("zimbraMailStatus", "FALSE"));
        attributes.add(new Attr("displayName", saveDistributionListDto.getDisplayName()));
        CreateDistributionListResponse createDistributionListResponse = zimbraService.createDistributionList(
                new CreateDistributionListRequest(saveDistributionListDto.getMail(), attributes));
        DistributionList distributionList = new DistributionList();

        distributionList.setZimbraId(createDistributionListResponse.getDl().getId());
        distributionList.setDisplayName(saveDistributionListDto.getDisplayName());
        distributionList.setMail(saveDistributionListDto.getMail());
        distributionList = distributionListRepository.save(distributionList);

        if(saveDistributionListDto.getTagIdList() != null) {
            for (Integer tagId : saveDistributionListDto.getTagIdList()) {
                tagJoinRepository.save(new TagJoin(null, tagId, distributionList.getId(), TagEntityType.distribution_list.name()));
            }
        }


        List<EmailDto> emailDtoListNew = saveDistributionListDto.getEmailDtoList();

        if(emailDtoListNew != null && !emailDtoListNew.isEmpty()) {
            for (EmailDto emailDto : emailDtoListNew) {
                MailDistributionList mailDistributionList = new MailDistributionList();
                mailDistributionList.setDistributionListId(distributionList.getId());
                mailDistributionList.setEmailId(emailDto.getId());
                mailDistributionListRepository.save(mailDistributionList);

            }
            zimbraService.addDistributionListMember(new AddDistributionListMemberRequest(distributionList.getZimbraId(),
                    emailDtoListNew.stream().map(EmailDto::getMail).collect(Collectors.toList())));
        }


        return saveDistributionListDto;
    }

    public SaveDistributionListDto editDistributionList(SaveDistributionListDto saveDistributionListDto) {
        SaveDistributionListDto dtoOld = getDetails(saveDistributionListDto.getId());
        if(dtoOld == null) {
            throw new RuntimeException("Không tìm thấy nhóm mail");
        }
        if(distributionListRepository.findByMail(saveDistributionListDto.getMail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }
        DistributionList distributionList = new DistributionList();
        distributionList.setId(saveDistributionListDto.getId());
        distributionList.setZimbraId(saveDistributionListDto.getZimbraId());

        distributionList.setDisplayName(saveDistributionListDto.getDisplayName());
        distributionList.setMail(saveDistributionListDto.getMail());
        distributionList = distributionListRepository.save(distributionList);

        List<Integer> tagIdListOld = tagJoinRepository.findByEntityIdAndType(distributionList.getId(), TagEntityType.distribution_list.name())
                .stream().map(TagJoin::getTagId).toList();
        CommonUtils.DiffResult<Integer> diffResultTagId = CommonUtils.diffLists(
                tagIdListOld, saveDistributionListDto.getTagIdList(), i -> i);

        for (Integer tagId : diffResultTagId.toDelete) {
            tagJoinRepository.deleteByEntityIdAndTagIdAndType(distributionList.getId(), tagId, TagEntityType.distribution_list.name());
        }

        for (Integer tagId : diffResultTagId.toAdd) {
            tagJoinRepository.save(new TagJoin(null, tagId, distributionList.getId(), TagEntityType.distribution_list.name()));
        }

        List<Attr> attributes = new ArrayList<>();
        attributes.add(new Attr("displayName", saveDistributionListDto.getDisplayName()));
        zimbraService.modifyDistributionList(new ModifyDistributionListRequest(saveDistributionListDto.getZimbraId(), attributes));
        zimbraService.renameDistributionList(new RenameDistributionListRequest(saveDistributionListDto.getZimbraId(), saveDistributionListDto.getMail()));

        List<EmailDto> emailDtoListOld = dtoOld.getEmailDtoList();
        List<EmailDto> emailDtoListNew = saveDistributionListDto.getEmailDtoList();
        CommonUtils.DiffResult<EmailDto> diffResult = CommonUtils.diffLists(
                emailDtoListOld, emailDtoListNew, EmailDto::getId);
        if(!diffResult.toAdd.isEmpty()) {
            for (EmailDto emailDto : diffResult.toAdd) {
                MailDistributionList mailDistributionList = new MailDistributionList();
                mailDistributionList.setDistributionListId(distributionList.getId());
                mailDistributionList.setEmailId(emailDto.getId());
                mailDistributionListRepository.save(mailDistributionList);

            }
            zimbraService.addDistributionListMember(new AddDistributionListMemberRequest(saveDistributionListDto.getZimbraId(),
                    diffResult.toAdd.stream().map(EmailDto::getMail).collect(Collectors.toList())));
        }

        if(!diffResult.toDelete.isEmpty()) {
            for (EmailDto emailDto : diffResult.toDelete) {
                mailDistributionListRepository.deleteByEmailId(emailDto.getId());
            }
            zimbraService.removeDistributionListMember(new RemoveDistributionListMemberRequest(saveDistributionListDto.getZimbraId(),
                    diffResult.toDelete.stream().map(EmailDto::getMail).collect(Collectors.toList())));
        }


        return saveDistributionListDto;
    }

    public boolean deleteDistributionList(Integer id) {
        Optional<DistributionList> distributionListOptional = distributionListRepository.findById(id);
        if (distributionListOptional.isPresent()) {
            DistributionList distributionList = distributionListOptional.get();
            distributionListRepository.deleteById(distributionList.getId());
            tagJoinRepository.deleteByEntityIdAndType(distributionList.getId(), TagEntityType.distribution_list.name());
            zimbraService.deleteDistributionList(new DeleteDistributionListRequest(distributionList.getZimbraId()));
            return true;
        }
        return false;
    }
}
