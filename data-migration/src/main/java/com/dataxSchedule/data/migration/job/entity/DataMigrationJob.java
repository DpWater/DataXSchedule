package com.dataxSchedule.data.migration.job.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class DataMigrationJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobName; //任务名称
    private String jobGroup; //任务分组
    private String cronExpression; //cron表达式
    private String beanClass; //任务执行类
    private String jobDataMap; //任务参数 json 例：{"username":"hangman"}
    private String createUserId;
    private LocalDateTime createDate;
    private String updateUserId;
    private LocalDateTime updateDate;
    private String remarks;
    private String status;//任务状态
    private Long readDataSourceId; //读取数据源ID
    private Long writeDataSourceId; //写入数据源ID
    private String dbName; //库名
    private String tableName; //表名
    private String incDateField; //增量日期字段
    private String incDateType; //增量日期格式
    private Integer incDateVal; //增量日期范围（天）：距本日前多少天
    private String whereParam; //数据查询条件
    private String hivePartitionField; //Hive分区字段
}
