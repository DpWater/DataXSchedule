package com.rdsp.data.migration.job.service.impl;

import com.rdsp.data.migration.database.BaseDbTool;
import com.rdsp.data.migration.job.entity.DataMigrationJob;
import com.rdsp.data.migration.job.mapper.DataMigrationJobMapper;
import com.rdsp.data.migration.job.service.JobDatasourceService;
import com.rdsp.data.migration.job.service.JobService;
import com.rdsp.data.migration.pojo.Job;
import com.rdsp.data.migration.pojo.JobHiveDatasource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

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
