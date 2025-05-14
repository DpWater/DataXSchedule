package com.dataxSchedule.data.migration.job.executor.impl;

import com.dataxSchedule.data.migration.job.executor.RunJobService;
import com.dataxSchedule.data.migration.job.service.JobDatasourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.sql.SQLException;

@SpringBootTest
class BuildJobServiceImplTest {
    @Autowired
    RunJobService buildJobService;
    @Autowired
    JobDatasourceService jobDatasourceService;
    @Test
    void contextLoads() throws SQLException, IOException, InterruptedException {
//        JobDatasource readDatasource = jobDatasourceService.getReaderDsById(null);
//        JobDatasource writeDatasource = jobDatasourceService.getWriterDsById(null);
//        buildJobService.createHiveTable(readDatasource,writeDatasource,"student2");
        buildJobService.runDataxJob(1L);

    }

}