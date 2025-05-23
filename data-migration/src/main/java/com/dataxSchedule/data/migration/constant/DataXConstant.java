package com.dataxSchedule.data.migration.constant;

/**
 * DataX启动参数
 *
 */
public class DataXConstant {

    public static final String SPLIT_SPACE = " ";

    public static final String TRANSFORM_SPLIT_SPACE = "\" \"";

    public static final String PERCENT ="%";

    public static final String TRANSFORM_QUOTES = "\"";

    public static final String JVM_CM = "-j";

    public static final String PARAMS_CM = "-p";

    public static final String PARAMS_NAME_PT = "partition";

    public static final String PARAMS_CM_V_PT = "-D" + PARAMS_NAME_PT+ "=%s";

    public static final String PARAMS_CM_V_DATE = "-DstartTime=%s -DendTime=%s";

    public static final String DEFAULT_JSON = "jsons";

    public static final String DEFAULT_DATAX_PY = "datax.py";

    public static final String TASK_START_TIME_SUFFIX = "任务启动时刻";
    public static final String TASK_END_TIME_SUFFIX = "任务结束时刻";

    public static final String TASK_TOTAL_TIME_SUFFIX = "任务总计耗时";
    public static final String TASK_AVERAGE_FLOW_SUFFIX = "任务平均流量";
    public static final String TASK_RECORD_WRITING_SPEED_SUFFIX = "记录写入速度";
    public static final String TASK_RECORD_READER_NUM_SUFFIX = "读出记录总数";
    public static final String TASK_RECORD_WRITING_NUM_SUFFIX = "读写失败总数";
    public static final String TASK_READER_PARAM_WHERE = "\"where\"";
}
