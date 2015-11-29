package com.itau.jingdong;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.itau.jingdong.network.ConnNet;
import com.itau.jingdong.ui.LoginActivity;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
				if(responseData.equals("null"))
					return null;
				else
					return responseData;
			}
			else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**从服务器获取图片*/
	public static Bitmap getHttpBitmap(String fileName,int type) {
		InputStream is = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		Bitmap bitmap = null;
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		ConnNet connNet = new ConnNet();
		HttpPost httpPost = connNet.gethttPost("download");
		MultipartEntity postEntity = new MultipartEntity();
		ContentBody cbFileName,cbType;
		try {
			cbFileName = new StringBody(fileName);
			// 把上面创建的这些Body全部加到Entity里面去。
			// 注意他们的key，这些key在Struts2服务器端Action的代码里必须保持一致！！
			postEntity.addPart("imagepath", cbFileName);
			System.out.println(fileName+"等待下载");
			if (type == 1) {
				cbType = new StringBody("user");
				postEntity.addPart("type",cbType);

			}
			else if (type == 2) {
				cbType = new StringBody("good");
				postEntity.addPart("type",cbType);
			}
			httpPost.setEntity(postEntity);
			// 下面这句话就把数据提交到服务器去了
			HttpResponse response = httpClient.execute(httpPost);
			System.out.println("链接成功");
			Header[] headers = response.getAllHeaders();
			long size = 0;//文件大小
			String suff = "";//文件后缀名
			for(Header h : headers) {
				if ("Content-Length".equals(h.getName())) {
					size = Long.valueOf(h.getValue());
					Log.i("janken", size + "");
				}
			}

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();//获得文件的输入流
				bis = new BufferedInputStream(is);
				File newFile = new File("/sdcard/Pictures/"+fileName);
				fos = new FileOutputStream(newFile);
				bos = new BufferedOutputStream(fos);

				byte[] bytes = new byte [4096];
				int len = 0;//最后一次的长度可能不足4096
				while((len = bis.read(bytes)) > 0) {
					bos.write(bytes,0,len);
				}
				bos.flush();
				System.out.println("get picture success!");
				if(bis != null)bis.close();
				if(bos != null)bos.close();
				if(fos != null)fos.close();
			}
			else
				System.out.println("服务器没响应");
		}
		catch (Exception e){
			return null;
		}
		bitmap = BitmapFactory.decodeFile("/sdcard/Pictures/"+fileName);
		return bitmap;
	}

	/**上传图片至服务器*/
	public JSONObject addpic(String filepath,String urlString,int type) {
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		ConnNet connNet = new ConnNet();
		HttpPost httpPost = connNet.gethttPost(urlString);
		MultipartEntity postEntity = new MultipartEntity();
		// 字符用StringBody
		String[] Array = filepath.split("/");
		String fileName = Array[Array.length-1];
		System.out.println("filename :" + fileName);
		ContentBody cbFileName,cbType;
		try {
			cbFileName = new StringBody(fileName);
			// 文件用FileBody，并指定文件类型
			File file = new File(filepath);
			ContentBody cbFileData = new FileBody(file, "image/jpg");

			// 把上面创建的这些Body全部加到Entity里面去。
			// 注意他们的key，这些key在Struts2服务器端Action的代码里必须保持一致！！
			postEntity.addPart("imagename", cbFileName);
			if (type == 1) {
				postEntity.addPart("imagedata", cbFileData);
				cbType = new StringBody("user");
				postEntity.addPart("type",cbType);

			}
			else if (type == 2) {
				postEntity.addPart("imagedata", cbFileData);
				cbType = new StringBody("good");
				postEntity.addPart("type",cbType);
			}
			httpPost.setEntity(postEntity);
			// 下面这句话就把数据提交到服务器去了
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"UTF-8"));
				String retData = null;
				String responseData = "";
				while((retData = in.readLine()) != null)
				{
					responseData += retData;
				}
				System.out.println("result of upload picture"+ responseData);
				JSONObject jsonObject = new JSONObject(responseData);
				return jsonObject;
			}
		} catch (Exception e) {
			throw null;
		}
		return null;
	}
}
