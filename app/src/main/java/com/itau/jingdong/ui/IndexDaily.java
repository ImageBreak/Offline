package com.itau.jingdong.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itau.jingdong.Operaton;
import com.itau.jingdong.bean.Good;
import com.itau.jingdong.home.BabyActivity;
import com.itau.jingdong.http.CU_JSONResolve;
import com.itau.jingdong.http.GetHttp;
import com.itau.jingdong.R;
import com.itau.jingdong.adapter.Adapter_ListView_ware;
import com.itau.jingdong.json.WriteJson;
import com.itau.jingdong.xListview.XListView;
import com.itau.jingdong.xListview.XListView.IXListViewListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class IndexDaily extends Activity implements  IXListViewListener {

	private String jsonString=null;
	private TextView cate;
	public List<Good> good = new ArrayList<Good>();
	//显示所有商品的列表
	private XListView listView;

	public String temp;
	public String g_type;
	public Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_index_daily);
		initView();
		//请求网络数据
		new WareTask().execute();
	}

	protected void initView() {
		cate = (TextView)findViewById(R.id.cate);

		listView = (XListView) findViewById(R.id.listView_ware);
		listView.setXListViewListener(this);
		// 设置可以进行下拉加载的功能
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
		g_type = sp.getString("cate", "");
		cate.setText(g_type);

	}


	private class WareTask extends AsyncTask<Void, Void, List<Good>> {
		ProgressDialog dialog=null;
		@Override
		protected void onPreExecute() {
			if (dialog==null) {
				dialog= ProgressDialog.show(IndexDaily.this, "", "正在加载...");
				dialog.show();
			}
		}

		@Override
		protected List<Good> doInBackground(Void... arg0) {
			jsonString= "{\"g_type\":\""+g_type+"\"}";
			System.out.println(jsonString);
			Operaton operaton = new Operaton();
			//将trade对象写出json形式字符串
			temp = operaton.GetData("goodlistoftype.action", jsonString);
			if(temp != null) {
				Gson gson = new Gson();
				good = gson.fromJson(temp, new TypeToken<List<Good>>() {
				}.getType());
				for(int i = 0;i < good.size();i++){
					bitmap = operaton.getHttpBitmap(good.get(i).getG_pic(),2);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					byte[] appicon = baos.toByteArray();// 转为byte数组
					String tp = Base64.encodeToString(appicon, Base64.DEFAULT);
					good.get(i).setG_pic(tp);
					System.out.println("转换成功！");
				}
			}
			else
				good = null;
			return good;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(final List<Good> result) {

			if (dialog!=null&&dialog.isShowing()) {
				dialog.dismiss();
				dialog=null;
			}

			//如果网络数据请求失败，那么显示默认的数据
			if (result != null ) {
				//得到data字段的数据
				Adapter_ListView_ware myAdapter = new Adapter_ListView_ware(IndexDaily.this, result);
				listView.setAdapter(myAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
						SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
						SharedPreferences.Editor ed = sp.edit();
						ed.remove("g_id");
						ed.putInt("g_id", result.get(position-1).getG_id());
						ed.remove("g_pic");
						ed.putString("g_pic", result.get(position-1).getG_pic());
						ed.remove("g_name");
						ed.putString("g_name", result.get(position-1).getG_name());
						ed.remove("g_price");
						ed.putString("g_price", result.get(position-1).getG_price());
						ed.remove("g_amount");
						ed.putInt("g_amount", result.get(position-1).getG_amount());
						ed.commit();
						Intent intent = new Intent(IndexDaily.this, BabyActivity.class);
						startActivity(intent);
					}
				});

			}else {
				listView.setAdapter(new Adapter_ListView_ware(IndexDaily.this));
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
						Intent intent = new Intent(IndexDaily.this, BabyActivity.class);
						startActivity(intent);
					}
				});
			}
			// 停止刷新和加载
			onLoad();
		}
	}
	/** 刷新 */
	@Override
	public void onRefresh() {
		// 刷新数据
		//pageIndex = 0;
		good.clear();
		new WareTask().execute();
		// 停止刷新和加载
		onLoad();
	}
	/** 加载更多 */
	@Override
	public void onLoadMore() {
		 /*pageIndex += 1;
        if (pageIndex >= 4) {
            Toast.makeText(this, "已经最后一页了", Toast.LENGTH_SHORT).show();
            onLoad();
            return;
        }*/
		Toast.makeText(this, "已经最后一页了", Toast.LENGTH_SHORT).show();
		onLoad();
		return;
		//new WareTask().execute();
	}

	/** 停止加载和刷新 */
	private void onLoad() {
		listView.stopRefresh();
		// 停止加载更多
		listView.stopLoadMore();
		// 设置最后一次刷新时间
		listView.setRefreshTime(getCurrentTime(System.currentTimeMillis()));
	}

	/** 简单的时间格式 */
	public static SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

	public static String getCurrentTime(long time) {
		if (0 == time) {
			return "";
		}
		return mDateFormat.format(new Date(time));
	}
}
