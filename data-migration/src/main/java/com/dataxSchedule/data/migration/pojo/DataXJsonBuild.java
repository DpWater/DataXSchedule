package com.dataxSchedule.data.migration.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 构建json
 *
 */
@Data
public class DataXJsonBuild implements Serializable {

    private Long readerDatasourceId;
    private List<String> readerTables;
    private RdbmsReader rdbmsReader;

    private Long writerDatasourceId;
    private List<String> writerTables;
    private HiveWriter hiveWriter;

}
