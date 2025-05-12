package com.vtidc.mymail.service;

import com.vtidc.mymail.config.enums.StatusFlowEmailType;
import com.vtidc.mymail.config.enums.TagEntityType;
import com.vtidc.mymail.dto.SaveFlowEmailDto;
import com.vtidc.mymail.dto.FlowEmailEntityDto;
import com.vtidc.mymail.dto.search.SearchFlowEmailRequest;
import com.vtidc.mymail.dto.zimbra.*;
import com.vtidc.mymail.entities.*;
import com.vtidc.mymail.repo.*;
import com.vtidc.mymail.repo.projection.FlowEmailProjection;
import com.vtidc.mymail.schedule.QuartzService;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class FlowEmailService {

    private final FlowEmailRepository flowEmailRepository;

    private final FlowEmailFromRepository flowEmailFromRepository;

    private final FlowEmailToRepository flowEmailToRepository;

    private final FlowEmailApproveRepository flowEmailApproveRepository;

    private final ZimbraService zimbraService;

    private final QuartzService quartzService;

    private final TagJoinRepository tagJoinRepository;

    public void createFlowEmail(SaveFlowEmailDto saveFlowEmailDto) {
        if(saveFlowEmailDto.getContent() != null) {
            String rawHtml = saveFlowEmailDto.getContent();
            String plainText = Jsoup.parse(rawHtml).text().trim();

            if (plainText.isEmpty()) {
                throw new RuntimeException("Nội dung không được để trống");
            }
        }

        if(!saveFlowEmailDto.getStartNow() && saveFlowEmailDto.getStartDate() == null){
            throw new RuntimeException("Thời gian gửi không được để trống");
        }

        FlowEmail flowEmail = new FlowEmail();
        flowEmail.setName(saveFlowEmailDto.getName());

        flowEmail.setStartDate(saveFlowEmailDto.getStartDate());
        flowEmail.setStartNow(saveFlowEmailDto.getStartNow());
        flowEmail.setContent(saveFlowEmailDto.getContent());
        flowEmail.setStatus(StatusFlowEmailType.not_send.name());
        flowEmail = flowEmailRepository.save(flowEmail);

        if(saveFlowEmailDto.getTagIdList() != null){
            for (Integer tagId : saveFlowEmailDto.getTagIdList()) {
                tagJoinRepository.save(new TagJoin(null, tagId, flowEmail.getId(), TagEntityType.flow_email.name()));
            }
        }


        for (FlowEmailEntityDto flowEmailFromDto : saveFlowEmailDto.getFlowEmailFromDtoList()) {
            FlowEmailFrom flowEmailFrom = new FlowEmailFrom();
            flowEmailFrom.setEntityId(flowEmailFromDto.getEntityId());
            flowEmailFrom.setFlowEmailId(flowEmail.getId());
            flowEmailFrom.setType(flowEmailFromDto.getType());
            flowEmailFromRepository.save(flowEmailFrom);
        }

        for (FlowEmailEntityDto flowEmailToDto : saveFlowEmailDto.getFlowEmailToDtoList()) {
            FlowEmailTo flowEmailTo = new FlowEmailTo();
            flowEmailTo.setEntityId(flowEmailToDto.getEntityId());
            flowEmailTo.setFlowEmailId(flowEmail.getId());
            flowEmailTo.setType(flowEmailToDto.getType());
            flowEmailToRepository.save(flowEmailTo);
        }

        for (FlowEmailEntityDto flowEmailApproveDto : saveFlowEmailDto.getFlowEmailapproveDtoList()) {
            FlowEmailApprove flowEmailApprove = new FlowEmailApprove();
            flowEmailApprove.setEntityId(flowEmailApproveDto.getEntityId());
            flowEmailApprove.setFlowEmailId(flowEmail.getId());
            flowEmailApprove.setType(flowEmailApproveDto.getType());
            flowEmailApproveRepository.save(flowEmailApprove);
        }
    }

    public Page<FlowEmail> search(SearchFlowEmailRequest searchFlowEmailRequest) {
        String keyword = searchFlowEmailRequest.getKeyword();
        Integer tagId = searchFlowEmailRequest.getTagId();
        int page = searchFlowEmailRequest.getPage();
        int size = searchFlowEmailRequest.getSize();
        Sort sort = Sort.unsorted();
        if (searchFlowEmailRequest.getSortAscending() != null && searchFlowEmailRequest.getSortBy() != null) {
            Sort.Direction direction = Sort.Direction.fromString(searchFlowEmailRequest.getSortAscending());
            sort = Sort.by(direction, searchFlowEmailRequest.getSortBy());
        }

        if (keyword != null &&keyword.matches(".*[+\\-@~<>*()\"].*")) {
            keyword = "\"" + keyword + "\"";
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        if(keyword == null || keyword.length() <= 2){
            if(tagId == null){
                return flowEmailRepository.findAll(pageable);
            }else {
                return flowEmailRepository.findByTagId(TagEntityType.flow_email.name(), tagId, pageable);
            }
        }
        if(tagId == null){
            return flowEmailRepository.fullTextSearch(keyword, pageable);
        }
        return flowEmailRepository.fullTextSearch(TagEntityType.flow_email.name(), keyword, tagId, pageable);
    }

    public SaveFlowEmailDto getFlowEmail(Integer id) {
        List<FlowEmailProjection> projections = flowEmailRepository.findDetailById(id, TagEntityType.flow_email.name());
        if (projections.isEmpty()) {
            throw new RuntimeException("Không tìm thấy luồng email");
        }

        FlowEmailProjection p = projections.get(0);

        SaveFlowEmailDto dto = new SaveFlowEmailDto();
        dto.setId(p.getId());
        dto.setName(p.getName());

        dto.setStartDate(p.getStartDate());
        dto.setStartNow(p.getStartNow() == 1);
        dto.setContent(p.getContent());
        dto.setStatus(p.getStatus());

        // gom lại các list
        dto.setFlowEmailFromDtoList(
                projections.stream()
                        .filter(fp -> fp.getFlowEmailFromEntityId() != null &&
                                fp.getFlowEmailFromEmail() != null &&
                                fp.getFlowEmailFromType() != null)
                        .map(fp -> {
                            FlowEmailEntityDto e = new FlowEmailEntityDto();
                            e.setEntityId(fp.getFlowEmailFromEntityId());
                            e.setEmail(fp.getFlowEmailFromEmail());
                            e.setType(fp.getFlowEmailFromType());
                            return e;
                        })
                        .distinct() // nếu cần tránh trùng
                        .toList()
        );

        dto.setFlowEmailToDtoList(
                projections.stream()
                        .filter(fp -> fp.getFlowEmailFromEntityId() != null &&
                                fp.getFlowEmailFromEmail() != null &&
                                fp.getFlowEmailFromType() != null)
                        .map(fp -> {
                            FlowEmailEntityDto e = new FlowEmailEntityDto();
                            e.setEntityId(fp.getFlowEmailToEntityId());
                            e.setEmail(fp.getFlowEmailToEmail());
                            e.setType(fp.getFlowEmailToType());
                            return e;
                        })
                        .distinct()
                        .toList()
        );

        dto.setFlowEmailapproveDtoList(
                projections.stream()
                        .filter(fp -> fp.getFlowEmailFromEntityId() != null &&
                                fp.getFlowEmailFromEmail() != null &&
                                fp.getFlowEmailFromType() != null)
                        .map(fp -> {
                            FlowEmailEntityDto e = new FlowEmailEntityDto();
                            e.setEntityId(fp.getFlowEmailApproveEntityId());
                            e.setEmail(fp.getFlowEmailApproveEmail());
                            e.setType(fp.getFlowEmailApproveType());
                            return e;
                        })
                        .distinct()
                        .toList()
        );

        dto.setTagIdList(
                projections.stream()
                        .map(FlowEmailProjection::getTagId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList()
        );

        return dto;
    }

    @Transactional
    public void approveFlowEmail(Integer id) {
        SaveFlowEmailDto saveFlowEmailDto = getFlowEmail(id);
        if (saveFlowEmailDto == null) {
            return ;
        }
        FlowEmail flowEmail = flowEmailRepository.findById(id).orElse(null);
        if(flowEmail == null) {
            return ;
        }
        if (flowEmail.getStatus().equals(StatusFlowEmailType.sent.name())) {
            throw new RuntimeException("Email đã được gửi");
        }
        if(flowEmail.getStatus().equals(StatusFlowEmailType.scheduled.name())) {
            throw new RuntimeException("Email đã được đặt lịch gửi");
        }
        if(flowEmail.getStartNow()){
            flowEmail.setStatus(StatusFlowEmailType.sent.name());
            flowEmailRepository.save(flowEmail);
            sendEmail(saveFlowEmailDto);
        }else{
            flowEmail.setStatus(StatusFlowEmailType.scheduled.name());
            flowEmailRepository.save(flowEmail);
            quartzService.sendMailLater(saveFlowEmailDto);
        }
    }

    public void sendEmail(SaveFlowEmailDto saveFlowEmailDto) {
        SendMsgRequest request = new SendMsgRequest();

        Message msg = new Message();

        List<EmailAddress> emailAddresses = new ArrayList<>();

        for (FlowEmailEntityDto flowEmailFromDto : saveFlowEmailDto.getFlowEmailFromDtoList()) {
            emailAddresses.add(new EmailAddress("f", flowEmailFromDto.getEmail()));
        }

        for (FlowEmailEntityDto flowEmailToDto : saveFlowEmailDto.getFlowEmailToDtoList()) {
            emailAddresses.add(new EmailAddress("t", flowEmailToDto.getEmail()));
        }

        msg.setEmailAddresses(emailAddresses);

        // Chủ đề
        msg.setSubject(saveFlowEmailDto.getName());

        // Nội dung HTML
        MimePart htmlPart = new MimePart();
        htmlPart.setContentType("text/html");
        htmlPart.setContent(saveFlowEmailDto.getContent());
        msg.setMimePart(htmlPart);

        // Gán vào request
        request.setMessage(msg);

        zimbraService.sendMsg(request);
    }


}
