package com.rdsp.data.migration.database.hive;

/**
 * Hive与Pg数据类型映射枚举类
 */
public enum HivePgDataTypeEnum {
    bool("bool", "BOOLEAN"),
    bpchar("bpchar", "CHAR"),
    date("date", "DATE"),
    float4("float4", "DOUBLE"),
    float8("float8", "DOUBLE"),
    int2("int2", "SMALLINT"),
    int4("int4", "INT"),
    int8("int8", "BIGINT"),
    numeric("numeric", "DECIMAL"),
    text("text", "STRING"),
    timestamp("timestamp", "TIMESTAMP"),
    timestamptz("timestamptz", "TIMESTAMP"),
    varchar("varchar", "VARCHAR")
    ;

    // 构造函数
    HivePgDataTypeEnum(String pgsqlType, String hiveType) {
        this.pgsqlType = pgsqlType;
        this.hiveType = hiveType;

    }

    private final String hiveType;
    private final String pgsqlType;

    public String getHiveType() {
        return hiveType;
    }

    public String getPgsqlType() {
        return pgsqlType;
    }
}
