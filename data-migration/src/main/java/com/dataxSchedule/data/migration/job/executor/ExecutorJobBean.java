package com.dataxSchedule.data.migration.job.executor;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 数据迁移job
 */
public class ExecutorJobBean extends QuartzJobBean {
    protected static final Logger logger = LoggerFactory.getLogger(ExecutorJobBean.class);

    @Autowired
    RunJobService buildJobService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        String jobId = context.getJobDetail().getJobDataMap().getString("jobId");
        logger.info("开始执行迁移任务-jobId：" + jobId);
        if (StringUtils.isNotBlank(jobId)){
            buildJobService.runDataxJob(Long.parseLong(jobId));
        }
        logger.info("结束执行迁移任务-jobId：" + jobId);
    }
}
