package com.vtidc.mymail.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtidc.mymail.config.enums.StatusFlowEmailType;
import com.vtidc.mymail.dto.SaveFlowEmailDto;
import com.vtidc.mymail.repo.FlowEmailRepository;
import com.vtidc.mymail.service.FlowEmailService;
import lombok.NoArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@NoArgsConstructor
public class SendLateEMailJob implements Job {

    @Autowired
    private FlowEmailService flowEmailService;

    @Autowired
    private FlowEmailRepository flowEmailRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void execute(JobExecutionContext context) {
        try {
             SaveFlowEmailDto saveFlowEmailDto = objectMapper.readValue(
                    context.getMergedJobDataMap().get("flowEmailDto").toString(), SaveFlowEmailDto.class);
            flowEmailService.sendEmail(saveFlowEmailDto);
            flowEmailRepository.updateStatusById(saveFlowEmailDto.getId(), StatusFlowEmailType.sent.name());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
