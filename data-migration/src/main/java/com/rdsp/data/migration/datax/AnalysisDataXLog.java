package com.rdsp.data.migration.datax;
import static com.rdsp.data.migration.constant.DataXConstant.*;

import com.rdsp.data.migration.pojo.DataXLog;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 日志解析
 *
 */

public class AnalysisDataXLog {



    /**
     * 解析Log
     *
     * @param inputStream
     * @throws IOException
     */
    public static DataXLog analysisDataXLog(InputStream inputStream) throws IOException {

        DataXLog logStatistics = new DataXLog();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(TASK_START_TIME_SUFFIX)) {
                    logStatistics.setTaskStartTime(subResult(line));
                } else if (line.contains(TASK_END_TIME_SUFFIX)) {
                    logStatistics.setTaskEndTime(subResult(line));
                } else if (line.contains(TASK_TOTAL_TIME_SUFFIX)) {
                    logStatistics.setTaskTotalTime(subResult(line));
                } else if (line.contains(TASK_AVERAGE_FLOW_SUFFIX)) {
                    logStatistics.setTaskAverageFlow(subResult(line));
                } else if (line.contains(TASK_RECORD_WRITING_SPEED_SUFFIX)) {
                    logStatistics.setTaskRecordWritingSpeed(subResult(line));
                } else if (line.contains(TASK_RECORD_READER_NUM_SUFFIX)) {
                    logStatistics.setTaskRecordReaderNum(Integer.parseInt(subResult(line)));
                } else if (line.contains(TASK_RECORD_WRITING_NUM_SUFFIX)) {
                    logStatistics.setTaskRecordWriteFailNum(Integer.parseInt(subResult(line)));
                }
            }
            reader.close();
            inputStream = null;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return logStatistics;
    }

    private static String subResult(String line) {
        if (StringUtils.isBlank(line)) return "";
        int pos = line.indexOf(":");
        if (pos > 0) return line.substring(pos + 1).trim();
        return line.trim();
    }
}
