package com.dataxSchedule.data.migration.datax;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.dataxSchedule.data.migration.pojo.Job;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dataxSchedule.data.migration.constant.DataXConstant.*;

/**
 * 生成DataX命令
 */
@Component
public class BuildCommand {
    protected static final Logger logger = LoggerFactory.getLogger(BuildCommand.class);

    @Value("${datax.home-path}")
    private String dataXHomePath;

    @Value("${datax.jvm-param}")
    private String jvmParam;

    @Value("${datax.json-path}")
    private String jsonPath;

    private String tmpFilePath;

    public String buildDataXExecutorCmd(Job job, String jobJson) {
        String tmpFilePath = generateTemJsonFile(jobJson);
        this.tmpFilePath = tmpFilePath;
        List<String> cmdArr = new ArrayList<>();
        cmdArr.add("python");
        String dataXPyPath = dataXHomePath + DEFAULT_DATAX_PY;
        cmdArr.add(dataXPyPath);
        String params = buildDataXParam(job);
        cmdArr.add(params);
//        if (StringUtils.isNotBlank(params)) {
//            cmdArr.add(params.replaceAll(SPLIT_SPACE, TRANSFORM_SPLIT_SPACE));
//        }
        cmdArr.add(tmpFilePath);
        return String.join(" ", cmdArr);
    }

    private String buildDataXParam(Job job) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(jvmParam)) {
            sb.append(JVM_CM).append(TRANSFORM_QUOTES).append(jvmParam).append(TRANSFORM_QUOTES);
        }
        Date incDate = DateUtils.addDays(new Date(),job.getIncDateVal());
        SimpleDateFormat sdf = new SimpleDateFormat(job.getIncDateType());
        String dtStartTime = sdf.format(incDate);
        String dtEndTime = sdf.format(DateUtils.addDays(incDate,1));

        sb.append(PARAMS_CM).append(TRANSFORM_QUOTES).append(String.format(PARAMS_CM_V_DATE, dtStartTime, dtEndTime));
        String partition = job.getHivePartitionField();
        if (StringUtils.isNotBlank(partition)) {
            if (sb.length() > 0) sb.append(SPLIT_SPACE);
            sb.append(String.format(PARAMS_CM_V_PT, dtStartTime)).append(TRANSFORM_QUOTES);
        }
        return sb.toString();
    }
    private String generateTemJsonFile(String jobJson) {
        String tmpFilePath;
        String tempJsonPath = jsonPath + DEFAULT_JSON;
        if (!FileUtil.exist(tempJsonPath)) {
            FileUtil.mkdir(tempJsonPath);
        }
        tmpFilePath = tempJsonPath + File.separator + "jobTmp-" + IdUtil.simpleUUID() + ".json";
        // 根据json写入到临时本地文件
        try (PrintWriter writer = new PrintWriter(tmpFilePath, "UTF-8")) {
            writer.println(jobJson);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            logger.error("JSON 临时文件写入异常：" + e.getMessage());
        }
        return tmpFilePath;
    }

    public String getTmpFilePath() {
        return tmpFilePath;
    }
}
