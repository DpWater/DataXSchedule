package com.dataxSchedule.data.migration.pojo;

import com.dataxSchedule.data.migration.job.entity.DataMigrationJob;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Job extends DataMigrationJob {
    private JobDatasource readDataSource; //读取数据源
    private JobHiveDatasource writeDataSource; //写入数据源
}
