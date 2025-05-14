import com.dataxSchedule.data.migration.database.table.TableColumn;
import org.apache.commons.lang.time.DateUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class test1 {
    public static void main(String[] args) throws SQLException {
//        DataXJsonBuild dataXJsonBuild = new DataXJsonBuild();
//
//        RdbmsReader rdbmsReader = new RdbmsReader();
//        JobDatasource readJobDatasource = new JobDatasource();
//        readJobDatasource.setDatasource("postgresql");
//        readJobDatasource.setDatasourceName("dataX");
//        readJobDatasource.setJdbcDriverClass("org.postgresql.Driver");
//        readJobDatasource.setJdbcUsername("postgres");
//        readJobDatasource.setJdbcPassword("postgres");
//        readJobDatasource.setJdbcUrl("jdbc:postgresql://192.168.137.201:5432/dataX");
//        rdbmsReader.setJobDatasource(readJobDatasource);
//        rdbmsReader.setTables(new ArrayList<String>(){{this.add("student");}});
//        rdbmsReader.setRdbmsColumns(new ArrayList<String>(){{this.add("*");}});
//        rdbmsReader.setWhereParam("id = 100");
//        dataXJsonBuild.setRdbmsReader(rdbmsReader);
//
//        HiveWriter hiveWriter = new HiveWriter();
//        JobDatasource writeJobDatasource = new JobDatasource();
//        writeJobDatasource.setDatasource("hive");
//        writeJobDatasource.setDatasourceName("db_hive1");
//        writeJobDatasource.setJdbcDriverClass("org.apache.hive.jdbc.HiveDriver");
//        writeJobDatasource.setJdbcUsername("root");
//        writeJobDatasource.setJdbcPassword("1234");
//        writeJobDatasource.setJdbcUrl("jdbc:hive2://192.168.137.201:10000/db_hive1");
//        hiveWriter.setJdbcDatasource(writeJobDatasource);
//        HiveDbTool hiveDbTool = new HiveDbTool(writeJobDatasource);
//        List<HiveTableColumn> hiveTableColumnList = hiveDbTool.getColumns("student1");
//
//        hiveWriter.setColumns(new ArrayList<Map<String, Object>>(){{
//            for (HiveTableColumn hiveTableColumn : hiveTableColumnList) {
//                HashMap<String, Object> map = new HashMap<>();
//                map.put("name",hiveTableColumn.getColumnName());
//                map.put("type",hiveTableColumn.getDataType());
//                this.add(map);
//            }
//        }});
//        hiveWriter.setWriterDefaultFS("hdfs://192.168.137.201:9000");
//        hiveWriter.setWriterPath("/hive/warehouse/db_hive1.db/student1");
//        hiveWriter.setWriterFileType("text");
//        hiveWriter.setWriterFileName("student1");
//        hiveWriter.setWriteMode("append");
//        hiveWriter.setWriteFieldDelimiter("\t");
//        dataXJsonBuild.setHiveWriter(hiveWriter);
//
//        DataXJsonBuildTool dataxJsonBuildTool = new DataXJsonBuildTool();
//        dataxJsonBuildTool.initReader(dataXJsonBuild);
//        dataxJsonBuildTool.initWriter(dataXJsonBuild);
//
//        System.out.println(JSON.toJSONString(dataxJsonBuildTool.buildJob(),true));
//        Date incDate = DateUtils.addDays(new Date(),-30);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dtStartTime = sdf.format(incDate);
//        System.out.println(dtStartTime);
        List<TableColumn> tableColumns1= new ArrayList<>();
        TableColumn tableColumn1 = new TableColumn();
        tableColumn1.setColumnName("a");
        TableColumn tableColumn2 = new TableColumn();
        tableColumn2.setColumnName("b");
        tableColumns1.add(tableColumn1);
        tableColumns1.add(tableColumn2);

        List<TableColumn> tableColumns2= new ArrayList<>();
        TableColumn tableColumn3 = new TableColumn();
        tableColumn3.setColumnName("b");
        TableColumn tableColumn4 = new TableColumn();
        tableColumn4.setColumnName("a");
        TableColumn tableColumn5 = new TableColumn();
        tableColumn5.setColumnName("c");


        tableColumns2.add(tableColumn3);
        tableColumns2.add(tableColumn4);
        System.out.println(tableColumns2.contains(tableColumn1));

        sort1(tableColumns1,tableColumns2);
        System.out.println("sucess");
    }

    private static void sort1(List<TableColumn> targetList, List<TableColumn> sourceList) {
        targetList.sort(((o1, o2) -> {
            int io1 = sourceList.indexOf(o1);
            int io2 = sourceList.indexOf(o2);
            return io1 - io2;
        }));
    }
}
