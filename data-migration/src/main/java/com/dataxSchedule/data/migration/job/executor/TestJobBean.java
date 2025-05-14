package com.dataxSchedule.data.migration.job.executor;

import com.dataxSchedule.data.migration.job.service.JobService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

public class TestJobBean extends QuartzJobBean {
    protected static final Logger logger = LoggerFactory.getLogger(TestJobBean.class);

    @Autowired
    private JobService jobService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("定时任务，当前时间 => {}", LocalDateTime.now());
    }
}
