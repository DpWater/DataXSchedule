package com.rdsp.data.migration.job.service;

import com.rdsp.data.migration.pojo.JobDatasource;
import com.rdsp.data.migration.pojo.JobHiveDatasource;

public interface JobDatasourceService {
    JobDatasource getReaderDsById(Long id); //获取源数据源
    JobHiveDatasource getWriterDsById(Long id); //获取Hive数据源
}
