package com.rdsp.data.migration.pojo;

import lombok.Data;

import java.util.List;

/**
 * 关系型数据库DataX构建Json传参
 */
@Data
public class RdbmsReader {
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
     * querySql 属性，如果指定了，则优先于columns参数
     */
    private String querySql;

    /**
     * 切分主键
     */
    private String splitPk;

    /**
     * where
     */
    private String whereParam;
}
