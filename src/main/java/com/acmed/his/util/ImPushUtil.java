package com.acmed.his.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 消息推送
 * @author Eric
 * @version 2016-09-21
 */
@Service
public class ImPushUtil {
	private static Logger logger = LoggerFactory.getLogger(ImPushUtil.class);
	/**get请求*/
    public static String http_get(String url) {
		try {
	    	logger.info("消息推送GET "+url);
			return excute("GET",url,null);
		} catch (Exception e) {
	    	logger.error("消息推送 error is "+e.getMessage());
			return e.getMessage();
		}
    }  
	/**post请求*/
    public static String http_post(String url,String jsondata) {
		try {
	    	logger.info("消息推送POST "+url);
	        logger.info("===================requestBody:"+jsondata);
			return excute("POST",url,jsondata);
		} catch (Exception e) {
	    	logger.error("消息推送 error is "+e.getMessage());
	    	return e.getMessage();
		}
    }  
	public static String excute(String method,String url,String jsondata) throws IOException {      // 定义URL实例
		URL uri = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		// 设置从主机读取数据超时
		conn.setReadTimeout(60*60*1000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Type","application/json");
		// 创建一个新的数据输出流，将数据写入指定基础输出流
		if(jsondata!=null){
			DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
			// 写入到输出流中
			outStream.write(jsondata.getBytes("utf-8"));
			// 刷新发送数据
			outStream.flush();
		}
		// 得到响应码
		int res = conn.getResponseCode();
		InputStream in = null;
		// 上传成功返回200
		if (res == 200) {
			in = conn.getInputStream();
			int ch;
			StringBuilder sb2 = new StringBuilder();
			// 保存数据
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
			String c = new String(sb2.toString().getBytes(), "utf-8");
	        logger.info("===================responseBody:"+c);
	        return c;
		} else {
	        logger.error("===================responseBody:"+conn.getResponseMessage());
	        return conn.getResponseMessage();
		}
	}
}
