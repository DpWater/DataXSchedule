package com.dataxSchedule.data.migration.job.service;

import com.dataxSchedule.data.migration.job.entity.DataMigrationJob;

/**
 * 任务管理
 */
public interface JobService {
    DataMigrationJob getById(Long jobId);
    void save(DataMigrationJob job);
}
