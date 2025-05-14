package com.dataxSchedule.data.migration.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Hive数据库构建Json传参
 */
@Data
public class HiveWriter {
    /**
     * hive列名
     */
    private List<Map<String,Object>> columns;

    /**
     * 数据源信息
     */
    private JobDatasource jdbcDatasource;

    private String writerDefaultFS;

    private String writerFileType;

    private String writerPath;

    private String writerFileName;

    private String writeMode;

    private String writeFieldDelimiter;

    private Boolean skipHeader;

    private String compress;

    private Map<String,String> hadoopConfig;
}
