package com.rdsp.data.migration.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.rdsp.data.migration.job.mapper.DataMigrationJobMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DruidDataSourceConfigTest {
//注入数据源

    @Autowired
    DataMigrationJobMapper mapper;
    @Test
    void contextLoads() throws SQLException {
        System.out.println(mapper.findAll());
    }
}