package com.itau.jingdong;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.itau.jingdong.network.ConnNet;
import com.itau.jingdong.ui.LoginActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Operaton 
{
	public JSONObject UpData(String uripath,String jsonString)
	{
		ConnNet connNet=new ConnNet();
		HttpURLConnection conn=connNet.getConn(uripath);
		try {
			OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes());
			os.close();
			int code = conn.getResponseCode();
			if (code==200)
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
				String retData = null;
				String responseData = "";
				while((retData = in.readLine()) != null)
				{
					responseData += retData;
				}
				System.out.println(responseData);
				JSONObject jsonObject = new JSONObject(responseData);
				return jsonObject;
			}
			else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String GetData(String uripath,String jsonString)
	{
		ConnNet connNet=new ConnNet();
		HttpURLConnection conn=connNet.getConn(uripath);
		try {
			OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes());
			os.close();
			int code = conn.getResponseCode();
			if (code==200)
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
				String retData = null;
				String responseData = "";
				while((retData = in.readLine()) != null)
				{
					responseData += retData;
				}
				System.out.println(responseData);
				return responseData;
			}
			else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**上传图片至服务器*/
	public String uploadFile(File file,String urlString,int type)
	{
		final String TAG = "uploadFile";
		final int TIME_OUT = 10*1000;   //超时时间
		final String CHARSET = "utf-8"; //设置编码
		String result = null;
		String BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
		String PREFIX = "--" , LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data";   //内容类型

		try {
			ConnNet connNet=new ConnNet();
		    HttpURLConnection conn	=connNet.getConn(urlString);
			conn.setReadTimeout(TIME_OUT);
//			conn.setConnectTimeout(TIME_OUT);
//			conn.setDoInput(true);  //允许输入流
//			conn.setDoOutput(true); //允许输出流
//			conn.setUseCaches(false);  //不允许使用缓存
//			conn.setRequestMethod("POST");  //请求方式
			conn.setRequestProperty("Charset", CHARSET);  //设置编码
			conn.setRequestProperty("connection", "keep-alive");   
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY); 

			if(file!=null)
			{
				/**
				 * 当文件不为空，把文件包装并且上传
				 */
				DataOutputStream dos = new DataOutputStream( conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意：
				 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名的   比如:abc.png  
				 */
				if(type == 1)
					sb.append("Content-Disposition: form-data; name=\"u_pic\"; filename=\""+file.getName()+"\""+LINE_END);
				else if(type == 2)
					sb.append("Content-Disposition: form-data; name=\"g_pic\"; filename=\""+file.getName()+"\""+LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while((len=is.read(bytes))!=-1)
				{
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码  200=成功
				 * 当响应成功，获取响应的流  
				 */
				int res = conn.getResponseCode();  
				Log.e(TAG, "response code:" + res);
				//                if(res==200)
				//                {
				Log.e(TAG, "request success");
				InputStream input =  conn.getInputStream();
				StringBuffer sb1= new StringBuffer();
				int ss ;
				while((ss=input.read())!=-1)
				{
					sb1.append((char)ss);
				}
				result = sb1.toString();
				Log.e(TAG, "result : " + result);
				//                }
				//                else{
				//                    Log.e(TAG, "request error");
				//                }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**从服务器获取图片*/
	public static Bitmap getHttpBitmap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setConnectTimeout(0);
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}


	public String addpic(File file,String urlString,int type) {
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		ConnNet connNet = new ConnNet();
		HttpPost httpPost = connNet.gethttPost(urlString);
		MultipartEntity postEntity = new MultipartEntity();

		// 字符用StringBody
		//String fileName = "2.jpg";

		//ContentBody cbFileName;
		try {
			//cbFileName = new StringBody(fileName);

			// 文件用FileBody，并指定文件类型

			//File file = new File("D:\\FTP\\2.jpg");
			ContentBody cbFileData = new FileBody(file, "image/jpg");


			// 把上面创建的这些Body全部加到Entity里面去。
			// 注意他们的key，这些key在Struts2服务器端Action的代码里必须保持一致！！
			//postEntity.addPart("fileName", cbFileName);
			if (type == 1)
				postEntity.addPart("u_pic", cbFileData);
			else if (type == 2)
				postEntity.addPart("g_pic", cbFileData);

			httpPost.setEntity(postEntity);
			// 下面这句话就把数据提交到服务器去了
			HttpResponse response = httpClient.execute(httpPost);
		} catch (Exception e) {
			throw null;
		}
		return "ok";
	}
}
