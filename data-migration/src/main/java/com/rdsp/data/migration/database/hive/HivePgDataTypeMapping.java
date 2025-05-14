package com.rdsp.data.migration.database.hive;
/**
 * Hive与Pg数据类型映射对照类
 */
public class HivePgDataTypeMapping {
    public static String getHiveTypeByPgType(String pgsqlType) {
        for (HivePgDataTypeEnum mapping : HivePgDataTypeEnum.values()) {
            if (mapping.getPgsqlType().equals(pgsqlType)) {
                return mapping.getHiveType();
            }
        }
        return null;
    }
}
