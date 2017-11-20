package com.acmed.his.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 腾讯调用
 * @author Administrator
 *
 */
public class TencentCallUtil {
	private static Logger logger = LoggerFactory.getLogger(TencentCallUtil.class);

	/**post请求*/
    public static String http_post(String url,String jsondata) {
		try {
	    	logger.info("消息推送POST "+url);
	        logger.info("===================requestBody:"+jsondata);
	        return excuteform("POST",url,jsondata);
		} catch (Exception e) {
	    	logger.error("消息推送 error is "+e.getMessage());
	    	return "tencent call error";
		}
    }  
	public static String excuteform(String method,String url,String jsondata) throws IOException {      // 定义URL实例
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
			outStream.close();
		}
		// 得到响应码
		int res = conn.getResponseCode();
		InputStream in = null;
		String c = "";
		// 上传成功返回200
		if (res == 200) {
			in = conn.getInputStream();
			int ch;
			StringBuilder sb2 = new StringBuilder();
			// 保存数据
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
			c = new String(sb2.toString().getBytes(), "utf-8");
	        logger.info("===================responseBody:"+c);
		} else {
	        logger.error("===================responseBody:"+conn.getResponseMessage());
		}
		return c;
	}

}
