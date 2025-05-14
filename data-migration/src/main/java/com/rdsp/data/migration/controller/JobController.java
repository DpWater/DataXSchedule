package com.rdsp.data.migration.controller;

import com.rdsp.data.migration.job.config.QuartzHandler;
import com.rdsp.data.migration.job.entity.DataMigrationJob;
import com.rdsp.data.migration.job.service.JobService;
import com.rdsp.data.migration.vo.Response;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/data/migration/job")
public class JobController {
    @Autowired
    private QuartzHandler quartzHandler;

    @Autowired
    private JobService jobService;

    /**
     * 更新定时任务
     * @param jobId 任务
     * @return 结果
     */
    @RequestMapping(value = "/update")
    public Response update(Long jobId){
        DataMigrationJob queryJob = jobService.getById(jobId);
        String status = quartzHandler.getStatus(queryJob);
        boolean result = false;
        if (!(Trigger.TriggerState.NONE.toString()).equals(status)) {
            result = quartzHandler.updateCronExpression(queryJob, queryJob.getCronExpression());
        }
        return result ? Response.ok() : Response.error();
    }

    /**
     * 定时任务启动
     * @param jobId 任务
     * @return 结果
     */
    @RequestMapping(value = "/start")
    public Response start(Long jobId) throws ClassNotFoundException {
        DataMigrationJob queryJob = jobService.getById(jobId);
        Class<?> clazz = Class.forName(queryJob.getBeanClass());
        boolean result = quartzHandler.start(queryJob, clazz);
        if (result){
            String status = quartzHandler.getStatus(queryJob);
            queryJob.setStatus(status);
            jobService.save(queryJob);
        }
        return result ? Response.ok() : Response.error();
    }
    /**
     * 定时任务暂停
     * @param jobId 任务
     * @return 结果
     */
    @RequestMapping(value = "/pasue")
    public Response pasue(Long jobId){
        DataMigrationJob queryJob = jobService.getById(jobId);
        String status = quartzHandler.getStatus(queryJob);
        if (!((Trigger.TriggerState.NORMAL.toString()).equals(status) || (Trigger.TriggerState.PAUSED.toString()).equals(status)
                || (Trigger.TriggerState.BLOCKED.toString()).equals(status))) {
            return Response.error("当前状态不可暂停");
        }
        if ((Trigger.TriggerState.PAUSED.toString()).equals(status)) {
            return Response.ok();
        }
        boolean result = quartzHandler.pasue(queryJob);
        if (result){
            status = quartzHandler.getStatus(queryJob);
            queryJob.setStatus(status);
            jobService.save(queryJob);
        }
        return result ? Response.ok() : Response.error();
    }
    /**
     * 定时任务立即执行
     * @param jobId 任务
     * @return 结果
     */
    @RequestMapping(value = "/trigger")
    public Response trigger(Long jobId){
        DataMigrationJob queryJob = jobService.getById(jobId);
        String status = quartzHandler.getStatus(queryJob);
        if (!((Trigger.TriggerState.NORMAL.toString()).equals(status) || (Trigger.TriggerState.PAUSED.toString()).equals(status)
                || (Trigger.TriggerState.COMPLETE.toString()).equals(status))) {
            return Response.error("当前状态不可立即执行");
        }
        boolean result = quartzHandler.trigger(queryJob);
        if (result){
            status = quartzHandler.getStatus(queryJob);
            queryJob.setStatus(status);
            jobService.save(queryJob);
        }
        return result ? Response.ok() : Response.error();
    }
    /**
     * 定时任务 删除
     * @param jobId 任务
     * @return 结果
     */
    @RequestMapping(value = "/delete")
    public Response delete(Long jobId){
        DataMigrationJob queryJob = jobService.getById(jobId);
        boolean result = false;
        if (!(Trigger.TriggerState.NONE.toString()).equals(queryJob.getStatus())) {
            result = quartzHandler.delete(queryJob);
        }
        return result ? Response.ok() : Response.error();
    }
}
