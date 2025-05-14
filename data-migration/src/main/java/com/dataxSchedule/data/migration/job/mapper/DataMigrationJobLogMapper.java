package com.dataxSchedule.data.migration.job.mapper;

import com.dataxSchedule.data.migration.job.entity.DataMigrationJobLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataMigrationJobLogMapper extends JpaRepository<DataMigrationJobLog,Long> {
}
