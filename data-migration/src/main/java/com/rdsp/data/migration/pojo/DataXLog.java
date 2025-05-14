package com.rdsp.data.migration.pojo;

import lombok.Data;

import java.io.Serializable;
@Data
public class DataXLog implements Serializable {

    /**
     * DataX任务启动时刻
     */
    private String taskStartTime;

    /**
     * DataX任务结束时刻
     */
    private String taskEndTime;

    /**
     * DataX任务总计耗时
     */
    private String taskTotalTime;

    /**
     * DataX任务平均流量
     */
    private String taskAverageFlow;

    /**
     * DataX记录写入速度
     */
    private String taskRecordWritingSpeed;

    /**
     * DataX读出记录总数
     */
    private int taskRecordReaderNum;
    /**
     * DataX读写失败总数
     */
    private int taskRecordWriteFailNum;

}
