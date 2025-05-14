package com.dataxSchedule.data.migration.job.service.impl;

import com.dataxSchedule.data.migration.job.entity.DataMigrationJob;
import com.dataxSchedule.data.migration.job.mapper.DataMigrationJobMapper;
import com.dataxSchedule.data.migration.job.service.JobDatasourceService;
import com.dataxSchedule.data.migration.job.service.JobService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobDatasourceService jobDatasourceService;

    @Autowired
    private DataMigrationJobMapper dataMigrationJobMapper;

    @Override
    public DataMigrationJob getById(Long jobId) {
        return dataMigrationJobMapper.findById(jobId).get();

    }

    @Override
    public void save(DataMigrationJob job) {
        dataMigrationJobMapper.save(job);
    }

}
