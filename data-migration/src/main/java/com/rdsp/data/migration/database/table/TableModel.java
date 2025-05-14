package com.rdsp.data.migration.database.table;

import lombok.Data;

import java.util.List;

/**
 * Hive表数据模型，包含表名、列信息和分区信息
 */
@Data
public class TableModel {
    private String dbName;
    private String tableName;
    private String comment;
    private List<TableColumn> columns;
    private String partitionColumn;
    private String partitionType;
}
