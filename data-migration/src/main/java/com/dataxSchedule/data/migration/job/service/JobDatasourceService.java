package com.dataxSchedule.data.migration.job.service;

import com.dataxSchedule.data.migration.pojo.JobDatasource;
import com.dataxSchedule.data.migration.pojo.JobHiveDatasource;

public interface JobDatasourceService {
    JobDatasource getReaderDsById(Long id); //获取源数据源
    JobHiveDatasource getWriterDsById(Long id); //获取Hive数据源
}
