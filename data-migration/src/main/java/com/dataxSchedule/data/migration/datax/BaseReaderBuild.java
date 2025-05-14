package com.dataxSchedule.data.migration.datax;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.ImmutableList;
import com.dataxSchedule.data.migration.pojo.RdbmsReader;
import com.dataxSchedule.data.migration.pojo.JobDatasource;
import lombok.Data;

import java.util.Map;
import com.google.common.collect.Maps;
/**
 * DataX生成基类
 */
@Data
public class BaseReaderBuild {
    /**
     * 获取reader插件名称
     *
     * @return
     */
    private String name;

    /**
     * 构建
     *
     * @return rdbmsReader
     */
    public Map<String, Object> build(RdbmsReader rdbmsReader) {
        //构建
        Map<String, Object> readerObj = Maps.newLinkedHashMap();
        readerObj.put("name", getName());
        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
        Map<String, Object> connectionObj = Maps.newLinkedHashMap();

        JobDatasource jobDatasource = rdbmsReader.getJobDatasource();
        parameterObj.put("username", jobDatasource.getJdbcUsername());
        parameterObj.put("password", jobDatasource.getJdbcPassword());

        //判断是否是 querySql
        if (!StringUtils.isEmpty(rdbmsReader.getQuerySql())) {
            connectionObj.put("querySql", ImmutableList.of(rdbmsReader.getQuerySql()));
        } else {
            parameterObj.put("column", rdbmsReader.getRdbmsColumns());
            //判断是否有where
            if (!StringUtils.isEmpty(rdbmsReader.getWhereParam())) {
                parameterObj.put("where", rdbmsReader.getWhereParam());
            }
            connectionObj.put("table", rdbmsReader.getTables());
        }
        parameterObj.put("splitPk",rdbmsReader.getSplitPk());
        connectionObj.put("jdbcUrl", ImmutableList.of(jobDatasource.getJdbcUrl()));

        parameterObj.put("connection", ImmutableList.of(connectionObj));

        readerObj.put("parameter", parameterObj);

        return readerObj;
    }
}
