package com.dataxSchedule.data.migration.database.table;

import lombok.Data;

/**
 * Hive表字段属性，包括列名、数据类型和是否可为空
 */
@Data
public class TableColumn {
    private String columnName;
    private String dataType;
    private String comment;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        TableColumn tableColumn = (TableColumn) obj;

        return columnName.equals(tableColumn.columnName);
    }

    @Override
    public int hashCode() {
        return columnName.hashCode();
    }
}
