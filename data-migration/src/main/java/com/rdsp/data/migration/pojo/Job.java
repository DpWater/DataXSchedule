package com.rdsp.data.migration.pojo;

import com.rdsp.data.migration.job.entity.DataMigrationJob;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 任务
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Job extends DataMigrationJob {
    private JobDatasource readDataSource; //读取数据源
    private JobHiveDatasource writeDataSource; //写入数据源
}
