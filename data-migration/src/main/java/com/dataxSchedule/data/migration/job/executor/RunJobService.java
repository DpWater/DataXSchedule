package com.dataxSchedule.data.migration.job.executor;

/**
 * 生成数据迁移任务
 */
public interface RunJobService {
    void runDataxJob(Long jobId) ;//执行dataX Job
}
