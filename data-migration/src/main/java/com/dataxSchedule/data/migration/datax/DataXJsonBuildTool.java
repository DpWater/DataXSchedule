package com.dataxSchedule.data.migration.datax;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.dataxSchedule.data.migration.pojo.HiveWriter;
import com.dataxSchedule.data.migration.pojo.RdbmsReader;
import com.dataxSchedule.data.migration.pojo.DataXJsonBuild;
import lombok.Data;

import java.util.Map;

/**
 * 构建DataX Json
 */
@Data
public class DataXJsonBuildTool {

    private BaseReaderBuild readerBuild;

    private BaseWriterBuild writerBuild;

    private Map<String, Object> buildReader;

    private Map<String, Object> buildWriter;

    private RdbmsReader rdbmsReader;

    private HiveWriter hiveWriter;

    public void initReader(DataXJsonBuild dataxJson) {
        //后边可以根据数据库类型进行扩展
        this.readerBuild = new PostgresqlReaderBuild();
        this.rdbmsReader = dataxJson.getRdbmsReader();
        this.buildReader = buildReader();
    }

    public void initWriter(DataXJsonBuild dataxJson){
        //后边可以根据数据库类型进行扩展
        this.writerBuild = new HiveWriterBuild();
        this.hiveWriter = dataxJson.getHiveWriter();
        this.buildWriter = buildHiveWriter();
    }

    public Map<String, Object> buildJob() {
        Map<String, Object> res = Maps.newLinkedHashMap();
        Map<String, Object> jobMap = Maps.newLinkedHashMap();
        jobMap.put("setting", buildSetting());
        jobMap.put("content", ImmutableList.of(buildContent()));
        res.put("job", jobMap);
        return res;
    }

    public Map<String, Object> buildSetting(){
        Map<String, Object> res = Maps.newLinkedHashMap();
        Map<String, Object> speedMap = Maps.newLinkedHashMap();
        Map<String, Object> errorLimitMap = Maps.newLinkedHashMap();
        speedMap.putAll(ImmutableMap.of("channel", 3, "byte", 1048576));
        errorLimitMap.putAll(ImmutableMap.of("record", 0, "percentage", 0.02));
        res.put("speed", speedMap);
        res.put("errorLimit", errorLimitMap);
        return res;
    }

    public Map<String, Object> buildContent(){
        Map<String, Object> res = Maps.newLinkedHashMap();
        res.put("reader", this.buildReader);
        res.put("writer", this.buildWriter);
        return res;
    }

    public Map<String, Object> buildReader(){
        return this.readerBuild.build(rdbmsReader);
    }

    public Map<String, Object> buildHiveWriter(){
        return this.writerBuild.buildHive(hiveWriter);
    }
}
