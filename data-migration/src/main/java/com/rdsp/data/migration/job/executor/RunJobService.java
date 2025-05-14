package com.rdsp.data.migration.job.executor;

import com.rdsp.data.migration.job.entity.DataMigrationJobLog;
import com.rdsp.data.migration.pojo.Job;

import java.sql.SQLException;

/**
 * 生成数据迁移任务
 */
public interface RunJobService {
    void runDataxJob(Long jobId) ;//执行dataX Job
}
