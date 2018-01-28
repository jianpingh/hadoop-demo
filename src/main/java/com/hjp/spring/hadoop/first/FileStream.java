package com.hjp.spring.hadoop.first;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class FileStream {

	public static void main(String[] args) throws IOException {
		// 节点流FileOutputStream直接以A.txt作为数据源操作
		FileOutputStream fileOutputStream = new FileOutputStream("classpath:sample.txt");
		// 过滤流BufferedOutputStream进一步装饰节点流，提供缓冲写
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
		// 过滤流DataOutputStream进一步装饰过滤流，使其提供基本数据类型的写
		DataOutputStream out = new DataOutputStream(bufferedOutputStream);
		out.writeInt(3);
		out.writeBoolean(true);
		out.flush();
		out.close();
		// 此处输入节点流，过滤流正好跟上边输出对应，读者可举一反三
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream("classpath:sample.txt")));
		System.out.println(in.readInt());
		System.out.println(in.readBoolean());
		in.close();
	}
	
	@Test
	public void printFile() throws IOException{
		 
        //BufferedReader是可以按行读取文件  
        FileInputStream inputStream = new FileInputStream("classpath:sample.txt");  
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));  
              
        String str = null;  
        while((str = bufferedReader.readLine()) != null)  
        {  
            System.out.println(str);  
        }  
              
        //close  
        inputStream.close();  
        bufferedReader.close();  
	}
	
	@Test
	public void printLine() throws IOException{
		 for (String line : Files.readLines(new File("classpath:sample.txt"), Charsets.UTF_8)) {
	            System.out.println(line);
	        }
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
