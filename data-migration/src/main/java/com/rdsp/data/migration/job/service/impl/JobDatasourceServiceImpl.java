package com.rdsp.data.migration.job.service.impl;

import com.rdsp.data.migration.job.service.JobDatasourceService;
import com.rdsp.data.migration.pojo.JobDatasource;
import com.rdsp.data.migration.pojo.JobHiveDatasource;
import org.springframework.stereotype.Service;

@Service
public class JobDatasourceServiceImpl implements JobDatasourceService {
    @Override
    public JobDatasource getReaderDsById(Long id) {
        JobDatasource readJobDatasource = new JobDatasource();
        readJobDatasource.setDatasource("postgresql");
        readJobDatasource.setDatasourceName("dataX");
        readJobDatasource.setJdbcDriverClass("org.postgresql.Driver");
        readJobDatasource.setJdbcUsername("postgres");
        readJobDatasource.setJdbcPassword("postgres");
        readJobDatasource.setJdbcUrl("jdbc:postgresql://192.168.137.201:5432/dataX");
        return readJobDatasource;
    }

    @Override
    public JobHiveDatasource getWriterDsById(Long id) {
        JobHiveDatasource writeJobDatasource = new JobHiveDatasource();
        writeJobDatasource.setDatasource("hive");
        writeJobDatasource.setDatasourceName("db_hive1");
        writeJobDatasource.setJdbcDriverClass("org.apache.hive.jdbc.HiveDriver");
        writeJobDatasource.setJdbcUsername("root");
        writeJobDatasource.setJdbcPassword("1234");
        writeJobDatasource.setJdbcUrl("jdbc:hive2://192.168.137.201:10000/db_hive1");
        writeJobDatasource.setHiveDefaultFS("hdfs://192.168.137.201:9000");
        writeJobDatasource.setHivePath("/hive/warehouse/db_hive1.db");
        writeJobDatasource.setHiveFileType("text");
        writeJobDatasource.setHiveFieldDelimiter("\t");
        return writeJobDatasource;
    }
}
