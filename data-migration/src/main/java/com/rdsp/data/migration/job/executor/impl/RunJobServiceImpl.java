package com.rdsp.data.migration.job.executor.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.rdsp.data.migration.database.BaseDbTool;
import com.rdsp.data.migration.database.HiveDbTool;
import com.rdsp.data.migration.database.hive.HivePgDataTypeMapping;
import com.rdsp.data.migration.database.table.TableColumn;
import com.rdsp.data.migration.database.table.TableModel;
import com.rdsp.data.migration.datax.BuildCommand;
import com.rdsp.data.migration.datax.DataXJsonBuildTool;
import com.rdsp.data.migration.constant.DataXConstant;
import com.rdsp.data.migration.job.entity.DataMigrationJob;
import com.rdsp.data.migration.job.entity.DataMigrationJobLog;
import com.rdsp.data.migration.job.executor.RunJobService;
import com.rdsp.data.migration.job.mapper.DataMigrationJobLogMapper;
import com.rdsp.data.migration.job.mapper.DataMigrationJobMapper;
import com.rdsp.data.migration.job.service.JobDatasourceService;
import com.rdsp.data.migration.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;

import static com.rdsp.data.migration.constant.DataXConstant.*;

@Service
public class RunJobServiceImpl implements RunJobService {
    protected static final Logger logger = LoggerFactory.getLogger(RunJobServiceImpl.class);

    @Autowired
    private JobDatasourceService jobDatasourceService;

    @Autowired
    private BuildCommand buildCommand;

    @Autowired
    private DataMigrationJobMapper dataMigrationJobMapper;

    @Autowired
    private DataMigrationJobLogMapper dataMigrationJobLogMapper;

    /**
     * 执行迁移任务
     * @param jobId 任务Id
     */
    @Override
    public void runDataxJob(Long jobId) {
        DataMigrationJobLog dataMigrationJobLog = new DataMigrationJobLog();
        dataMigrationJobLog.setJobId(jobId);
        try{
            DataMigrationJob dataMigrationJob = dataMigrationJobMapper.findById(jobId).get();
            Job job = new Job();
            BeanUtils.copyProperties(dataMigrationJob,job);
            job.setReadDataSource(jobDatasourceService.getReaderDsById(job.getReadDataSourceId()));
            job.setWriteDataSource(jobDatasourceService.getWriterDsById(job.getWriteDataSourceId()));
            //在Hive中创建库

            //在Hive中创建表
            createHiveTable(job);
            //生成DataX执行命令
            String json = buildDataxJson(job);
            String command =  buildCommand.buildDataXExecutorCmd(job,json);
            String whereParam = null;

            // 执行DataX任务
            // 使用ProcessBuilder创建进程
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", command);
            processBuilder.redirectErrorStream(true);
            try {
                // 启动进程
                Process process = processBuilder.start();
                // 读取进程的输出信息
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                //StringBuilder logs = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    if (line.contains(TASK_START_TIME_SUFFIX)) {
                        dataMigrationJobLog.setTaskStartTime(subResult(line));
                    } else if (line.contains(TASK_END_TIME_SUFFIX)) {
                        dataMigrationJobLog.setTaskEndTime(subResult(line));
                    } else if (line.contains(TASK_TOTAL_TIME_SUFFIX)) {
                        dataMigrationJobLog.setTaskTotalTime(subResult(line));
                    } else if (line.contains(TASK_RECORD_READER_NUM_SUFFIX)) {
                        dataMigrationJobLog.setTaskRecordReaderNum(Integer.parseInt(subResult(line)));
                    } else if (line.contains(TASK_RECORD_WRITING_NUM_SUFFIX)) {
                        dataMigrationJobLog.setTaskRecordWriteFailNum(Integer.parseInt(subResult(line)));
                    } else if (line.contains(TASK_READER_PARAM_WHERE)){
                        whereParam = subResult(line);
                    }
                    //logs.append(line);
                    logger.info(line);
                }
                //DataX读取写入完成后，执行删除任务
                if (dataMigrationJobLog.getTaskStartTime() != null && dataMigrationJobLog.getTaskRecordReaderNum() > 0){
                    //dataMigrationJobLog.setTaskRecordDeleteNum(deleteSourceData(job,whereParam,dataMigrationJobLog));
                    //刷新Hive分区
                    if (StringUtils.isNotBlank(job.getHivePartitionField())){
                        repairHivePartition(job);
                    }
                }


                // 等待进程执行完成
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    logger.info("DataX 任务执行完成");
                } else {
                    logger.info("DataX 任务执行失败，错误代码：" + exitCode);
                }
                reader.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }finally {
                //  删除临时文件
                if (FileUtil.exist(buildCommand.getTmpFilePath())) {
                    FileUtil.del(new File(buildCommand.getTmpFilePath()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        dataMigrationJobLogMapper.save(dataMigrationJobLog);
    }
    /**
     * 按源数据表结构在Hive中创建表
     * @param job 任务
     */
    public void createHiveTable(Job job) throws SQLException {
        JobDatasource readJobDatasource = job.getReadDataSource();
        JobDatasource writeJobDatasource = job.getWriteDataSource();
        String tableName = job.getTableName();
        String sql = "SELECT *,\n" +
                " CASE\n" +
                "   WHEN data_type = 'numeric' THEN \n" +
                "    CASE WHEN data_length2 > '38' THEN \n" +
                "     REPLACE(data_length1,data_length2,'38') \n" +
                "    ELSE data_length1 END \n" +
                " ELSE\n" +
                "   data_length1\n" +
                " END data_length\n" +
                "FROM\n" +
                "(\n" +
                "SELECT \n" +
                " CAST ( obj_description ( C.oid ) AS VARCHAR ) AS table_description,\n" +
                " A.attname AS column_name,\n" +
                " T.typname data_type,\n" +
                "  CASE\n" +
                "  WHEN T.typname = 'timestamp' THEN ''  \n" +
                "  ELSE SUBSTRING ( format_type ( A.atttypid, A.atttypmod ) FROM '\\(.*\\)' )\n" +
                " END data_length1,\n" +
                " SUBSTRING(format_type ( A.atttypid, A.atttypmod ) from '(\\d+)') data_length2,\n" +
                "  A.attnum,\n" +
                " d.description AS column_description\n" +
                "FROM\n" +
                " pg_attribute\n" +
                " A LEFT JOIN pg_description d ON d.objoid = A.attrelid \n" +
                " AND d.objsubid = A.attnum\n" +
                " LEFT JOIN pg_class C ON A.attrelid = C.oid\n" +
                " LEFT JOIN pg_type T ON A.atttypid = T.oid \n" +
                "WHERE\n" +
                " A.attnum >= 0 \n" +
                " AND C.relname ILIKE  ? \n" +
                ") a\n" +
                "where data_type is not null\n" +
                "ORDER BY attnum ASC" ;
        BaseDbTool readDbTool= new BaseDbTool(readJobDatasource);
        HiveDbTool writeDbTool = new HiveDbTool(writeJobDatasource);
        writeDbTool.createDb(job.getDbName()); //创建库
        List<Map<String, Object>> res = readDbTool.executeQuery(sql, ImmutableList.of(tableName));
        if (res != null){
            TableModel model = new TableModel();
            List<TableColumn> columns = new ArrayList<>();
            model.setDbName(job.getDbName());
            model.setTableName(tableName);
            res.forEach(c ->{
                model.setComment((String) c.get("table_description"));
                String pgDataType = (String) c.get("data_type");
                String hiveDataType = HivePgDataTypeMapping.getHiveTypeByPgType(pgDataType);
                String dataLength = (String) c.get("data_length");
                TableColumn nameColumn = new TableColumn();
                nameColumn.setColumnName((String) c.get("column_name"));
                nameColumn.setDataType(hiveDataType + (dataLength != null ? dataLength : ""));
                nameColumn.setComment((String) c.get("column_description"));
                columns.add(nameColumn);
            });
            if (!writeDbTool.isExistTable(tableName)){
                //创建表
                model.setColumns(columns);
                model.setPartitionColumn(job.getHivePartitionField());
                model.setPartitionType("string");
                writeDbTool.createTable(model);
            }/*else{
                //比较Hive表中是否有缺失的字段，如果缺少进行字段增加
                //事实证明，不能随意增加字段，会导致字段对应不上，数据出现问题
                List<TableColumn> writeColumns = writeDbTool.getColumns(tableName);
                List<TableColumn> addColumns = new ArrayList<>();
                for(TableColumn column : columns){
                    if (!writeColumns.contains(column)){
                        addColumns.add(column);
                    }
                }
                if (addColumns.size() > 0){
                    writeDbTool.addTableColumns(tableName,addColumns);
                }*/
        }
    }
    /**
     * 生成DataX执行命令
     * @param job 任务
     */
    public String buildDataxJson(Job job) throws SQLException {
        DataXJsonBuild dataXJsonBuild = new DataXJsonBuild();
        RdbmsReader rdbmsReader = new RdbmsReader();
        HiveWriter hiveWriter = new HiveWriter();
        JobDatasource readJobDatasource = job.getReadDataSource();
        JobHiveDatasource writeJobDatasource = job.getWriteDataSource();
        String incDateField = job.getIncDateField();
        rdbmsReader.setJobDatasource(readJobDatasource);
        rdbmsReader.setTables(new ArrayList<String>(){{this.add(job.getTableName());}});

        BaseDbTool readDbTool = new BaseDbTool(readJobDatasource);
        List<TableColumn> readTableColumnList = readDbTool.getColumns(job.getTableName());
        HiveDbTool hiveDbTool = new HiveDbTool(writeJobDatasource);
        List<TableColumn> hiveTableColumnList = hiveDbTool.getColumns(job.getTableName());
        listSort(readTableColumnList,hiveTableColumnList); //按hive字段顺序排序，否则写入会有问题
        rdbmsReader.setRdbmsColumns(new ArrayList<String>(){{
            for (TableColumn readTableColumn : readTableColumnList) {
                this.add(readTableColumn.getColumnName());
            }
        }});
        if (!StringUtils.isBlank(job.getIncDateField())){
            String whereParam = incDateField + " >= '${startTime}' and " + incDateField +" < '${endTime}'";
            job.setWhereParam(whereParam);
        }
        rdbmsReader.setWhereParam(job.getWhereParam());
        dataXJsonBuild.setRdbmsReader(rdbmsReader);

        hiveWriter.setColumns(new ArrayList<Map<String, Object>>(){{
            for (TableColumn tableColumn : hiveTableColumnList) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("name",tableColumn.getColumnName());
                map.put("type",tableColumn.getDataType());
                this.add(map);
            }
        }});
        String hivePartitionField = job.getHivePartitionField();
        hiveWriter.setWriterDefaultFS(writeJobDatasource.getHiveDefaultFS());
        hiveWriter.setWriterPath(writeJobDatasource.getHivePath() +  "/" + job.getTableName()  + (StringUtils.isBlank(hivePartitionField) ? "" : "/" + hivePartitionField + "=${" + DataXConstant.PARAMS_NAME_PT + "}"));
        hiveWriter.setWriterFileType(writeJobDatasource.getHiveFileType());
        hiveWriter.setWriterFileName(job.getTableName());
        hiveWriter.setWriteMode("truncate");
        hiveWriter.setWriteFieldDelimiter(writeJobDatasource.getHiveFieldDelimiter());
        dataXJsonBuild.setHiveWriter(hiveWriter);

        DataXJsonBuildTool dataxJsonBuildTool = new DataXJsonBuildTool();
        dataxJsonBuildTool.initReader(dataXJsonBuild);
        dataxJsonBuildTool.initWriter(dataXJsonBuild);
        return JSON.toJSONString(dataxJsonBuildTool.buildJob(),true);
    }
    /**
     * 删除已导入数据
     * @param job 任务
     */
    public Integer deleteSourceData(Job job,String whereParam,DataMigrationJobLog dataMigrationJobLog) {
        int result = 0;
        try{
            BaseDbTool readDbTool= new BaseDbTool(job.getReadDataSource());
            whereParam = whereParam.replace("\"","").replace(",","");
            String sql = "select count(*) cnt from " + job.getTableName() +" where " + whereParam;
            List<Map<String, Object>> res = readDbTool.executeQuery(sql,new ArrayList<>());

            Long selectCount =  (Long)res.get(0).get("cnt");
            if (selectCount == dataMigrationJobLog.getTaskRecordReaderNum() && dataMigrationJobLog.getTaskRecordWriteFailNum() ==0){
                sql = "delete from " + job.getTableName() + " where " + whereParam;
                result = readDbTool.executeUpdate(sql, new ArrayList<>());
            }
            logger.info("删除 任务执行完成");
        }catch (Exception e){
           e.printStackTrace();
        }
        return result;
    }

    /**
     * 刷新表Hive分区
     * @param job 任务
     */
    public void repairHivePartition(Job job) {
        try{
            BaseDbTool writeDbTool= new BaseDbTool(job.getWriteDataSource());
            writeDbTool.execute("MSCK REPAIR TABLE " + job.getTableName());
            logger.info("刷新Hive分区 任务执行完成");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private static String subResult(String line) {
        if (StringUtils.isBlank(line)) return "";
        int pos = line.indexOf(":");
        if (pos > 0) return line.substring(pos + 1).trim();
        return line.trim();
    }

    private static void listSort(List<TableColumn> targetList, List<TableColumn> sourceList) {
        targetList.sort(((o1, o2) -> {
            int io1 = sourceList.indexOf(o1);
            int io2 = sourceList.indexOf(o2);
            return io1 - io2;
        }));
    }
}
