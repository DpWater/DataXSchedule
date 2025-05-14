package com.dataxSchedule.data.migration.datax;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.dataxSchedule.data.migration.pojo.HiveWriter;
import com.dataxSchedule.data.migration.pojo.JobDatasource;
import com.dataxSchedule.data.migration.pojo.RdbmsWriter;
import lombok.Data;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class BaseWriterBuild {
    private String name;
    public Map<String, Object> build(RdbmsWriter rdbmsWriter) {
        Map<String, Object> writerObj = Maps.newLinkedHashMap();
        writerObj.put("name", getName());

        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
        JobDatasource jobDatasource = rdbmsWriter.getJobDatasource();
        parameterObj.put("username", jobDatasource.getJdbcUsername());
        parameterObj.put("password", jobDatasource.getJdbcPassword());
        parameterObj.put("column", rdbmsWriter.getRdbmsColumns());
        parameterObj.put("preSql", splitSql(rdbmsWriter.getPreSql()));
        parameterObj.put("postSql", splitSql(rdbmsWriter.getPostSql()));

        Map<String, Object> connectionObj = Maps.newLinkedHashMap();
        connectionObj.put("table", rdbmsWriter.getTables());
        connectionObj.put("jdbcUrl", jobDatasource.getJdbcUrl());

        parameterObj.put("connection", ImmutableList.of(connectionObj));
        writerObj.put("parameter", parameterObj);

        return writerObj;
    }

    public Map<String, Object> buildHive(HiveWriter hiveWriter){
        return null;
    };

    private String[] splitSql(String sql) {
        String[] sqlArr = null;
        if (!StringUtils.isEmpty(sql)) {
            Pattern p = Pattern.compile("\r\n|\r|\n|\n\r");
            Matcher m = p.matcher(sql);
            String sqlStr = m.replaceAll("");
            sqlArr = sqlStr.split(";");
        }
        return sqlArr;
    }
}
