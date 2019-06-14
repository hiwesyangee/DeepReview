package com.hiwes.cores.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import utils.ServerConfigs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaHBaseUtils {
    /**
     * ThreadLocal为变量在每个线程中都创建了一个副本，那么每个线程可以访问自己内部的副本变量。
     */
    ThreadLocal<List<Put>> threadLocal = new ThreadLocal<>();
    HBaseAdmin admin = null;
    Connection conn = null;

    // 单例模式，直接进行HBase操作
    private JavaHBaseUtils() {
        Configuration configuration = new Configuration();
        configuration.set("hbase.zookeeper.quorum", ServerConfigs.ZK);
        configuration.set("hbase.rootdir", "hdfs://hiwes:8020/hbase");

        try {
            conn = ConnectionFactory.createConnection(configuration);
            admin = (HBaseAdmin) conn.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JavaHBaseUtils instance = null;

    public static synchronized JavaHBaseUtils getInstance() {
        if (null == instance) {
            instance = new JavaHBaseUtils();
        }
        return instance;
    }

    /**
     * 根据表名获取到HTable实例
     */
    public Table getTable(String tableName) {
        Table table = null;
        try {
            final TableName tname = TableName.valueOf(tableName);
            table = conn.getTable(tname);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

    /**
     * 创建HBase表
     *
     * @param tableName 表名
     * @param cfs       列族数组
     * @return
     */
    public static boolean createTable(String tableName, String[] cfs) {
        try (HBaseAdmin admin = JavaHBaseUtils.getInstance().admin) {
            if (admin.tableExists((tableName))) {
                return false;
            }
            HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            Arrays.stream(cfs).forEach(cf -> {
                HColumnDescriptor columnDescriptor = new HColumnDescriptor(cf);
                columnDescriptor.setMaxVersions(1);
                tableDescriptor.addFamily(columnDescriptor);
            });
            admin.createTable(tableDescriptor);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }

    /**
     * 删除hbase表.
     *
     * @param tableName 表名
     * @return 是否删除成功
     */
    public static boolean deleteTable(String tableName) {
        try (HBaseAdmin admin = JavaHBaseUtils.getInstance().admin) {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 插入Hbase表数据
     *
     * @param tableName 表名
     * @param rowKey    唯一标识
     * @param cfName    列族名
     * @param qualifier 列标识
     * @param data      数据
     * @return 是否插入成功
     */
    public static boolean putRow(String tableName, String rowKey, String cfName, String qualifier, String data) {
        try (Table table = JavaHBaseUtils.getInstance().getTable(tableName)) {
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(cfName), Bytes.toBytes(qualifier), Bytes.toBytes(data));
            table.put(put);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }

    /**
     * 批量插入Hbase数据
     *
     * @param tableName 表名
     * @param puts
     * @return 是否插入成功
     */
    public static boolean putRows(String tableName, List<Put> puts) {
        try (Table table = JavaHBaseUtils.getInstance().getTable(tableName)) {
            table.put(puts);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }

    /**
     * 批量添加记录到HBase表，同一线程要保证对相同表进行添加操作！
     *
     * @param tableName HBase表名
     * @param rowkey    HBase表的rowkey
     * @param cf        HBase表的columnfamily
     * @param column    HBase表的列key
     * @param value     写入HBase表的值value
     */
    public void bulkput(String tableName, String rowkey, String cf, String column, String value) {
        try {
            List<Put> list = threadLocal.get();
            if (list == null) {
                list = new ArrayList<>();
            }
            Put put = new Put(Bytes.toBytes(rowkey));
            put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(column), Bytes.toBytes(value));
            list.add(put);
            if (list.size() >= ServerConfigs.CACHE_LIST_SIZE) {
                Table table = getTable(tableName);
                table.put(list);
                list.clear();
            } else {
                threadLocal.set(list);
            }
            // table.flushCommits();  // 手动控制
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取单元格数据
     *
     * @param tableName
     * @param rowKey
     * @param cf
     * @param column
     * @return
     */
    public static String getValue(String tableName, String rowKey, String cf, String column) {
        String result1 = "";
        try (Table table = JavaHBaseUtils.getInstance().getTable(tableName)) {
            Get g = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(g);
            byte[] value = result.getValue(Bytes.toBytes(cf), Bytes.toBytes(column));
            result1 = Bytes.toString(value);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return result1;
    }

    /**
     * 查询HBase单条数据
     *
     * @param tableName 表名
     * @param rowkey    唯一标识
     * @return 查询结果
     */
    public static Result getRow(String tableName, String rowkey) {
        try (Table table = JavaHBaseUtils.getInstance().getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowkey));
            return table.get(get);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    /**
     * 通过过滤查询数据
     *
     * @param tableName  表名
     * @param rowKey     唯一标识
     * @param filterList 过滤条件
     * @return 返回结果
     */
    public static Result getRow(String tableName, String rowKey, FilterList filterList) {
        try (Table table = JavaHBaseUtils.getInstance().getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowKey));
            get.setFilter(filterList);
            return table.get(get);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    /**
     * 全表扫描
     *
     * @param tableName 表名
     * @return
     */
    public static ResultScanner getScanner(String tableName) {
        try (Table table = JavaHBaseUtils.getInstance().getTable(tableName)) {
            Scan scan = new Scan();
            scan.setCaching(1000);
            return table.getScanner(scan);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    /**
     * 批量检索数据
     *
     * @param tableName 表名
     * @param startRow  起始标识
     * @param endRow    终止标识
     * @return ResultScanner实例
     */
    public static ResultScanner getScanner(String tableName, String startRow, String endRow) {
        try (Table table = JavaHBaseUtils.getInstance().getTable(tableName)) {
            Scan scan = new Scan();
            scan.setStartRow(Bytes.toBytes(startRow));
            scan.setStopRow(Bytes.toBytes(endRow));
            scan.setCaching(1000);
            return table.getScanner(scan);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }


    /**
     * 设置过滤器，批量检索数据
     *
     * @param tableName  表名
     * @param startRow   起始标识
     * @param endRow     终止标识
     * @param filterList 过滤器
     * @return ResultScanner实例
     */
    public static ResultScanner getScanner(String tableName, String startRow, String endRow, FilterList filterList) {
        try (Table table = JavaHBaseUtils.getInstance().getTable(tableName)) {
            Scan scan = new Scan();
            scan.setStartRow(Bytes.toBytes(startRow));
            scan.setStopRow(Bytes.toBytes(endRow));
            scan.setFilter(filterList);
            scan.setCaching(1000);
            return table.getScanner(scan);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    /**
     * 删除HBase一行记录
     *
     * @param tableName 表名
     * @param rowKey    唯一标识
     * @return 是否删除成功
     */
    public static boolean deleteRow(String tableName, String rowKey) {
        try (Table table = JavaHBaseUtils.getInstance().getTable(tableName)) {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }

    /**
     * 删除HBase表指定列族
     *
     * @param tableName 表名
     * @param cfName    列族名
     * @return 删除是否成功
     */
    public static boolean deleteColumnFamily(String tableName, String cfName) {
        try (HBaseAdmin admin = JavaHBaseUtils.getInstance().admin) {
            admin.deleteColumn(tableName, cfName);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }

    /**
     * 删除HBase表指定列
     *
     * @param tableName 表名
     * @param rowKey    唯一标识
     * @param cfName    列族名
     * @param qualifier 列名
     * @return 删除是否成功
     */
    public static boolean deleteQualifier(String tableName, String rowKey, String cfName, String qualifier) {
        try (Table table = JavaHBaseUtils.getInstance().getTable(tableName)) {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumn(Bytes.toBytes(cfName), Bytes.toBytes(qualifier));
            table.delete(delete);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }


    /**
     * 多线程加批处理进行数据存储
     */
    @Test
    public static void main(String[] args) {
        String[] cfs = {"f1"};

        String tableName = "t1";
        String cf = "f1";
        String column = "id";
        // String rowkey = "1";
        JavaHBaseUtils.getInstance().createTable("t1", cfs);

        long start = System.currentTimeMillis();
        System.out.println("开始第一次写入数据: ");
        /** 此处接入100k条数据，直接进行批处理进行操作 */
        for (int i = 0; i < 1000000; i++) {
            JavaHBaseUtils.getInstance().bulkput(tableName, i + "", cf, column, String.valueOf(100321 + i));
        }
        System.out.println(System.currentTimeMillis() - start);

        try {
            new Thread().sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long start2 = System.currentTimeMillis();
        System.out.println("开始第二次写入数据: ");
        /** 此处接入新的100k条数据，使用多线程加批处理的方式写入 */
        new Thread(new Runnable() {
            public void run() {
                for (int i = 1000000; i < 2000000; i++) {
                    JavaHBaseUtils.getInstance().bulkput(tableName, i + "", cf, column, String.valueOf(100321 + i));
                }
            }
        }).start();

        // 注意：在这里，只是创建了一个线程，并且是在一个线程里面进行的100k条数据的写入，为何速度会这么快？
        System.out.println(System.currentTimeMillis() - start2);


        /**
         * 接下来开始测试HBase的高性能读取数据
         */
        long start3 = System.currentTimeMillis();
        System.out.println("开始第一次读取数据: ");
        for (int i = 0; i < 100000; i++) {
            JavaHBaseUtils.getValue(tableName, i + "", cf, column);
        }
        System.out.println(System.currentTimeMillis() - start3);

        try {
            new Thread().sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long start4 = System.currentTimeMillis();
        System.out.println("开始第二次读取数据: ");
        new Thread(new Runnable() {
            public void run() {
                for (int i = 100000; i < 200000; i++) {
                    JavaHBaseUtils.getValue(tableName, i + "", cf, column);
                }
            }
        }).start();
        System.out.println(System.currentTimeMillis() - start4);

        long start5 = System.currentTimeMillis();
        System.out.println("开始第三次读取数据: ");
        JavaHBaseUtils.getScanner(tableName, "0", "999999");
        System.out.println(System.currentTimeMillis() - start5);

        try {
            new Thread().sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long start6 = System.currentTimeMillis();
        System.out.println("开始第四次读取数据: ");
        new Thread(new Runnable() {
            public void run() {
                JavaHBaseUtils.getScanner(tableName, "1000000", "1999999");
            }
        }).start();
        System.out.println(System.currentTimeMillis() - start6);

    }


}
