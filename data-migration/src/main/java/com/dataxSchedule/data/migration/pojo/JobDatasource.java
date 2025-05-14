package com.dataxSchedule.data.migration.pojo;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;

/**
 *数据源管理
 */
@Data
public class JobDatasource {
    private String datasource;
    private String datasourceName;
    private String jdbcDriverClass;
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;
    private String schemaName;
}
