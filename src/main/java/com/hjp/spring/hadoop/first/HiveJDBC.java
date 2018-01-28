package com.hjp.spring.hadoop.first;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HiveJDBC {

	public static void main(String[] args) throws Exception {
		Class.forName("org.apache.hive.jdbc.HiveDriver");
		// default为数据库名
		//Connection conn = DriverManager.getConnection("jdbc:hive2://192.168.231.201:10000/mydb2");
		Connection con = DriverManager.getConnection("jdbc:hive2://master:10000/db_hive_test","centos","123456");
		Statement stmt = con.createStatement();
		String querySQL = "SELECT * FROM student";

		ResultSet res = stmt.executeQuery(querySQL);

		while (res.next()) {
			System.out.println(res.getString(1)+","+res.getString(2));
		}

	}
}
