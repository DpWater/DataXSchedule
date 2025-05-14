package com.dataxSchedule.data.migration.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Hive数据源管理
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobHiveDatasource extends JobDatasource {
    private String hiveDefaultFS;
    private String hivePath;
    private String hiveFileType;
    private String hiveFileName;
    private String hiveFieldDelimiter;
}
