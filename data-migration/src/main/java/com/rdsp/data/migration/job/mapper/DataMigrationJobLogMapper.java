package com.rdsp.data.migration.job.mapper;

import com.rdsp.data.migration.job.entity.DataMigrationJobLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataMigrationJobLogMapper extends JpaRepository<DataMigrationJobLog,Long> {
}
