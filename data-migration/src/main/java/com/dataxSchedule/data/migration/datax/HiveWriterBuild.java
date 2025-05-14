package com.dataxSchedule.data.migration.datax;

import com.google.common.collect.Maps;
import com.dataxSchedule.data.migration.pojo.HiveWriter;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class HiveWriterBuild extends BaseWriterBuild {
    public HiveWriterBuild() {
        setName("hdfswriter");
    }
    @Override
    public Map<String, Object> buildHive(HiveWriter hiveWriter) {
        Map<String, Object> writerObj = Maps.newLinkedHashMap();
        writerObj.put("name", getName());
        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
        parameterObj.put("defaultFS", hiveWriter.getWriterDefaultFS());
        parameterObj.put("fileType", hiveWriter.getWriterFileType());
        parameterObj.put("path", hiveWriter.getWriterPath());
        parameterObj.put("fileName", hiveWriter.getWriterFileName());
        parameterObj.put("writeMode", hiveWriter.getWriteMode());
        parameterObj.put("fieldDelimiter", hiveWriter.getWriteFieldDelimiter());
        parameterObj.put("column", hiveWriter.getColumns());
        if (StringUtils.isNotBlank(hiveWriter.getCompress()))
            parameterObj.put("compress", hiveWriter.getCompress());
        if (hiveWriter.getHadoopConfig()!=null)
            parameterObj.put("hadoopConfig", hiveWriter.getHadoopConfig());
        writerObj.put("parameter", parameterObj);
        return writerObj;
    }
}
