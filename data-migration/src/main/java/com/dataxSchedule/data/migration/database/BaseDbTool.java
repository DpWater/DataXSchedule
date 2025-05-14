package com.dataxSchedule.data.migration.database;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import com.dataxSchedule.data.migration.database.table.TableColumn;
import com.dataxSchedule.data.migration.utils.LocalCacheUtils;
import com.dataxSchedule.data.migration.pojo.JobDatasource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作工具基类
 */
public class BaseDbTool {
    protected static final Logger logger = LoggerFactory.getLogger(BaseDbTool.class);

    protected DataSource datasource;

    protected Connection connection;

    public BaseDbTool(JobDatasource jobDatasource) throws SQLException {
        if (LocalCacheUtils.get(jobDatasource.getDatasourceName()) == null) {
            getDataSource(jobDatasource);
        } else {
            this.connection = (Connection) LocalCacheUtils.get(jobDatasource.getDatasourceName());
            if (!this.connection.isValid(500)) {
                LocalCacheUtils.remove(jobDatasource.getDatasourceName());
                getDataSource(jobDatasource);
            }
        }
        LocalCacheUtils.set(jobDatasource.getDatasourceName(), this.connection, 4 * 60 * 60 * 1000);
    }
    private void getDataSource(JobDatasource jobDatasource) throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(jobDatasource.getJdbcUsername());
        druidDataSource.setPassword(jobDatasource.getJdbcPassword());
        druidDataSource.setUrl(jobDatasource.getJdbcUrl());
        druidDataSource.setDriverClassName(jobDatasource.getJdbcDriverClass());
        this.datasource = druidDataSource;
        this.connection = this.datasource.getConnection();
    }

    public List<Map<String, Object>> executeQuery(String sql, List<Object> parameters)
            throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = this.connection.prepareStatement(sql);

            setParameters(stmt, parameters);

            rs = stmt.executeQuery();

            ResultSetMetaData rsMeta = rs.getMetaData();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<String, Object>();

                for (int i = 0, size = rsMeta.getColumnCount(); i < size; ++i) {
                    String columName = rsMeta.getColumnLabel(i + 1);
                    Object value = rs.getObject(i + 1);
                    row.put(columName, value);
                }

                rows.add(row);
            }
        } finally {
            close(rs);
            close(stmt);
        }

        return rows;
    }

    public int executeUpdate(String sql, List<Object> parameters) throws SQLException {
        PreparedStatement stmt = null;

        int updateCount;
        try {
            stmt = this.connection.prepareStatement(sql);

            setParameters(stmt, parameters);

            updateCount = stmt.executeUpdate();
        } finally {
            close(stmt);
        }

        return updateCount;
    }
    public void execute(String sql, List<Object> parameters) throws SQLException {
        PreparedStatement stmt = null;

        try {
            stmt = this.connection.prepareStatement(sql);

            setParameters(stmt, parameters);

            stmt.execute();
        } finally {
            close(stmt);
        }
    }
    public void execute(String sql) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = this.connection.prepareStatement(sql);
            stmt.execute();
        } finally {
            close(stmt);
        }
    }
    private void setParameters(PreparedStatement stmt, List<Object> parameters) throws SQLException {
        for (int i = 0, size = parameters.size(); i < size; ++i) {
            Object param = parameters.get(i);
            stmt.setObject(i + 1, param);
        }
    }

    public void close(Statement x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (Exception e) {
            logger.debug("close statement error", e);
        }
    }

    public void close(ResultSet x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (Exception e) {
            logger.debug("close result set error", e);
        }
    }

    public List<TableColumn> getColumns(String tableName) {
        List<TableColumn> res = Lists.newArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //获取查询指定表所有字段的sql语句
            String querySql = "select * from "+ tableName + " where 1=0 ";
            //获取所有字段
            stmt = connection.createStatement();
            rs = stmt.executeQuery(querySql);
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                String columnType = metaData.getColumnTypeName(i);
                TableColumn hiveTableColumn = new TableColumn();
                if (columnName.contains(".")) {
                    hiveTableColumn.setColumnName(columnName.substring(columnName.indexOf(".") + 1));
                } else {
                    hiveTableColumn.setColumnName(columnName);
                }
                hiveTableColumn.setDataType(columnType);
                res.add(hiveTableColumn);
            }
        } catch (SQLException e) {
            logger.error("[getColumnNames Exception] --> "
                    + "the exception message is:" + e.getMessage());
        } finally {
            close(rs);
            close(stmt);
        }
        return res;
    }

    public Boolean isExistTable(String tableName){
        boolean flag = false;
        try {
            DatabaseMetaData meta = connection.getMetaData();
            String[] type = {"TABLE"};
            ResultSet rs = meta.getTables(null, null, tableName, type);
            flag = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public DataSource getDatasource() {
        return datasource;
    }

    public Connection getConnection() {
        return connection;
    }
}
