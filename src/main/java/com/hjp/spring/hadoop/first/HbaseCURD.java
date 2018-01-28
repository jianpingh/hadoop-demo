package com.hjp.spring.hadoop.first;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Name;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;

public class HbaseCURD {

	// 与HBase数据库的连接对象
	Connection conn;
	@Before
	public void setUp() throws Exception {

		// 取得一个数据库连接的配置参数对象
		Configuration conf = HBaseConfiguration.create();
		// 设置连接参数：HBase数据库所在的主机IP
		conf.set("hbase.zookeeper.quorum", "master,node1,node2");
		// 设置连接参数：HBase数据库使用的端口
		conf.set("hbase.zookeeper.property.clientPort", "2181");

		// 取得一个数据库连接对象
		conn = ConnectionFactory.createConnection(conf);
	}

	@Test
	public void put() throws Exception {
		
		// 通过连接工厂创建连接对象
		//conn = ConnectionFactory.createConnection(conf);
		// 通过连接查询tableName对象
		TableName tname = TableName.valueOf("ns1:t1");
		// 获得table
		Table table = conn.getTable(tname);

		// 通过bytes工具类创建字节数组(将字符串)
		byte[] rowid = Bytes.toBytes("row3");

		// 创建put对象
		Put put = new Put(rowid);

		byte[] f1 = Bytes.toBytes("f1");
		byte[] id = Bytes.toBytes("id");
		byte[] value = Bytes.toBytes(102);
		put.addColumn(f1, id, value);

		// 执行插入
		table.put(put);
	}

	@Test
	public void bigInsert() throws Exception {

		DecimalFormat format = new DecimalFormat();
		format.applyPattern("0000");

		long start = System.currentTimeMillis();
		TableName tname = TableName.valueOf("ns1:t1");
		HTable table = (HTable) conn.getTable(tname);
		// 不要自动清理缓冲区
		table.setAutoFlush(false);

		for (int i = 1; i < 10000; i++) {
			Put put = new Put(Bytes.toBytes("row" + format.format(i)));
			// 关闭写前日志
			put.setWriteToWAL(false);
			put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("id"), Bytes.toBytes(i));
			put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("name"), Bytes.toBytes("tom" + i));
			put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("age"), Bytes.toBytes(i % 100));
			table.put(put);

			if (i % 2000 == 0) {
				table.flushCommits();
			}
		}
		//
		table.flushCommits();
		System.out.println(System.currentTimeMillis() - start);
	}
	
	/**
     * 遍历
     */
    @Test
    public void scan() throws IOException {
        TableName tname = TableName.valueOf("ns1:t1");
        Table table = conn.getTable(tname);
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes("row5000"));
        scan.setStopRow(Bytes.toBytes("row8000"));
        ResultScanner rs = table.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
            System.out.println(Bytes.toString(name));
        }
    }
    
    /**
     * 动态遍历
     */
    @Test
    public void scan2() throws IOException {
        //Configuration conf = HBaseConfiguration.create();
        //Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t1");
        Table table = conn.getTable(tname);
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes("row5000"));
        scan.setStopRow(Bytes.toBytes("row8000"));
        ResultScanner rs = table.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            Map<byte[],byte[]> map = r.getFamilyMap(Bytes.toBytes("f1"));
            for(Map.Entry<byte[],byte[]> entrySet : map.entrySet()){
                String col = Bytes.toString(entrySet.getKey());
                String val = Bytes.toString(entrySet.getValue());
                System.out.print(col + ":" + val + ",");
            }

            System.out.println();
        }
    }
}
