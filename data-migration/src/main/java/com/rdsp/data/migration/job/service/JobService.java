package com.rdsp.data.migration.job.service;

import com.rdsp.data.migration.job.entity.DataMigrationJob;

/**
 * 任务管理
 */
public interface JobService {
    DataMigrationJob getById(Long jobId);
    void save(DataMigrationJob job);
}
