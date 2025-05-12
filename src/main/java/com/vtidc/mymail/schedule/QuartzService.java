package com.vtidc.mymail.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtidc.mymail.dto.SaveFlowEmailDto;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class QuartzService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    public void sendMailLater(SaveFlowEmailDto saveFlowEmailDto) {
        String flowId = saveFlowEmailDto.getId().toString();
        try {
            JobDetail jobDetail = JobBuilder.newJob(SendLateEMailJob.class)
                    .withIdentity(flowId)
                    .withDescription("send mail later")
                    .usingJobData("taskId", flowId)
                    .build();

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            if (scheduler.checkExists(jobDetail.getKey())){
                scheduler.deleteJob(jobDetail.getKey());
            }

            Instant future = saveFlowEmailDto.getStartDate();
            Date futureDate = Date.from(future);
            scheduler.scheduleJob(jobDetail, TriggerBuilder.newTrigger().forJob(jobDetail)
                    .startAt(futureDate)
                    .withIdentity(flowId)
                    .usingJobData("taskId", flowId)
                    .usingJobData("flowEmailDto", objectMapper.writeValueAsString(saveFlowEmailDto))
                    .withDescription("Remind date task")
                    .build());
            scheduler.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
