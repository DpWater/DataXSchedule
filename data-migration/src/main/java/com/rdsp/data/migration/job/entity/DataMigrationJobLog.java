package com.rdsp.data.migration.job.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class DataMigrationJobLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long jobId;
    /**
     * 任务启动时刻
     */
    private String taskStartTime;

    /**
     * 任务结束时刻
     */
    private String taskEndTime;

    /**
     * 任务总计耗时
     */
    private String taskTotalTime;

    /**
     * 读出记录总数
     */
    private int taskRecordReaderNum;
    /**
     * 读写失败总数
     */
    private int taskRecordWriteFailNum;

    /**
     * 删除数量
     */
    private int taskRecordDeleteNum;
}
