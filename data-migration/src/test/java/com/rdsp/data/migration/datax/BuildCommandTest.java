package com.rdsp.data.migration.datax;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BuildCommandTest {
    @Autowired
    private BuildCommand buildCommand;
    @Test
    void contextLoads() throws SQLException {
        buildCommand.buildDataXExecutorCmd(null,null);
    }
}