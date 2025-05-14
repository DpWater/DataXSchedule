package com.dataxSchedule.data.migration.database;

import com.google.common.collect.Lists;
import com.dataxSchedule.data.migration.database.table.TableModel;
import com.dataxSchedule.data.migration.database.table.TableColumn;
import com.dataxSchedule.data.migration.pojo.JobDatasource;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Hive数据操作工具类
 */
public class HiveDbTool extends BaseDbTool{
    public HiveDbTool(JobDatasource jobDatasource) throws SQLException {
        super(jobDatasource);
    }
    public void createDb(String dbName) throws SQLException{
        String sql = "CREATE DATABASE IF NOT EXISTS " + dbName;
        execute(sql);
    }
    public void createTable(TableModel tableModel) throws SQLException{
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE EXTERNAL TABLE IF NOT EXISTS ");
        sqlBuilder.append(tableModel.getDbName()).append(".").append(tableModel.getTableName());
        sqlBuilder.append(" (\n");

        List<TableColumn> columns = tableModel.getColumns().stream().filter(c -> !c.getColumnName().equalsIgnoreCase(tableModel.getPartitionColumn())).collect(Collectors.toList());
        for (int i = 0; i < columns.size(); i++) {
            TableColumn column = columns.get(i);
            sqlBuilder.append("   `").append(column.getColumnName()).append("`");
            sqlBuilder.append(" ");
            sqlBuilder.append(column.getDataType());
            if (column.getComment() != null){
                sqlBuilder.append(" COMMENT '").append(column.getComment()).append("'");
            }
            if (i < columns.size() - 1) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("\n");
        }
        sqlBuilder.append(")");
        if (tableModel.getComment() != null){
            sqlBuilder.append(" COMMENT " + "'").append(tableModel.getComment()).append("'\n");
        }
        if (tableModel.getPartitionColumn() != null) {
            sqlBuilder.append("    PARTITIONED BY (");
            sqlBuilder.append("`").append(tableModel.getPartitionColumn()).append("`");
            sqlBuilder.append(" ");
            sqlBuilder.append(tableModel.getPartitionType());
            sqlBuilder.append(")\n");
        }
        sqlBuilder.append("    ROW FORMAT DELIMITED FIELDS TERMINATED BY '\\t'\n");
        sqlBuilder.append("    NULL DEFINED AS ''");
        execute(sqlBuilder.toString());
    }

    public void addTableColumns(String tableName,List<TableColumn> tableColumns) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        for(TableColumn tableColumn : tableColumns){
            sqlBuilder.setLength(0);
            sqlBuilder.append("alter table ")
                    .append(tableName)
                    .append(" add columns (")
                    .append(tableColumn.getColumnName())
                    .append(" ")
                    .append(tableColumn.getDataType())
                    .append(" ) cascade \n");
            execute(sqlBuilder.toString());
        }

    }
}
