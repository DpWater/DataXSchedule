package com.rdsp.data.migration.pojo;

import lombok.Data;

import java.util.List;

/**
 * 关系型数据库DataX构建Json传参
 */
@Data
public class RdbmsWriter {
    /**
     * 表名
     */
    private List<String> tables;

    /**
     * 列名
     */
    private List<String> rdbmsColumns;

    /**
     * 数据源信息
     */
    private JobDatasource jobDatasource;

    /**
     * preSql 属性
     */
    private String preSql;

    /**
     * postSql 属性
     */
    private String postSql;

}
