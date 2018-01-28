package com.hjp.spring.hadoop.first;

import java.util.List;

import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

public class ZooKeeperTest {

	@Test
	public void ls() throws Exception {
		ZooKeeper zk = new ZooKeeper("node1:2181,node2:2181,node3:2181", 5000, null);
		List<String> list = zk.getChildren("/", null);
		for (String s : list) {
			System.out.println(s);
		}
	}

	@Test
	public void lsAll() throws Exception {
		ls("/");
	}

	/**
	 * 列出指定path下的孩子
	 */
	public void ls(String path) throws Exception {
		System.out.println(path);
		ZooKeeper zk = new ZooKeeper("master:node1:2181,node2:2181", 5000, null);
		List<String> list = zk.getChildren(path, null);
		if (list == null || list.isEmpty()) {
			return;
		}
		for (String s : list) {
			// 先输出孩子
			if (path.equals("/")) {
				ls(path + s);
			} else {
				ls(path + "/" + s);
			}
		}
	}
}
