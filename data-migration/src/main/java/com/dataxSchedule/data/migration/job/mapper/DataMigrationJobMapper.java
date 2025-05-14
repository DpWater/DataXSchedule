package com.dataxSchedule.data.migration.job.mapper;

import com.dataxSchedule.data.migration.job.entity.DataMigrationJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataMigrationJobMapper extends JpaRepository<DataMigrationJob,Long> {

}
