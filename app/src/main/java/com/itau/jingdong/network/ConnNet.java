package com.itau.jingdong.network;

import android.widget.Toast;

import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ConnNet 
{
	private static final String HOME="http://192.168.1.103:8080/blank/";
	//将路径定义为一个常量，修改的时候也好更改
	//通过url获取网络连接  connection 
	public HttpURLConnection getConn(String urlpath)
	{
		String finalurl=HOME+urlpath;
		HttpURLConnection connection = null;
		try {
			URL url=new URL(finalurl);
			connection=(HttpURLConnection) url.openConnection();
			connection.setDoInput(true);  //允许输入流
            connection.setDoOutput(true); //允许输出流
            connection.setUseCaches(false);  //不允许使用缓存
            connection.setRequestMethod("POST");  //请求方式
			connection.setRequestProperty("User-Agent", "Fiddler");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Charset", "UTF-8");
			//connection.setConnectTimeout(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(finalurl);
		return connection;

	}
	public HttpPost gethttPost(String uripath)
	{	
		HttpPost httpPost=new HttpPost(HOME+uripath);
	
		System.out.println(HOME+uripath);
		return httpPost;
	}

}
