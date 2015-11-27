package com.itau.jingdong.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itau.jingdong.Data.Data;
import com.itau.jingdong.MyView.BabyPopWindow;
import com.itau.jingdong.MyView.BabyPopWindow.OnItemClickListener;
import com.itau.jingdong.Operaton;
import com.itau.jingdong.R;
import com.itau.jingdong.adapter.Adapter_ListView_detail;
import com.itau.jingdong.ScaleView.HackyViewPager;
import com.itau.jingdong.bean.Evaluation;
import com.itau.jingdong.bean.Favorite;
import com.itau.jingdong.bean.Good;
import com.itau.jingdong.bean.Trade;
import com.itau.jingdong.json.WriteJson;
import com.itau.jingdong.ui.BabyCommentActivity;
//import com.zdp.aseo.content.AseoZdpAseo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 单个商品详情界面
 * 
 */
public class BabyActivity extends FragmentActivity implements OnItemClickListener, OnClickListener {

	NfcAdapter nfcAdapter;

	private HackyViewPager viewPager;
	private ArrayList<View> allListView;
	private ListView listView;
	private ImageView iv_baby_collection,iv_baby;
	/**弹出商品订单信息详情*/
	private BabyPopWindow popWindow;
	/** 用于设置背景暗淡 */
	private LinearLayout all_choice_layout = null;
	/**是否添加收藏*/
	private boolean isCollection=false;
	/**ViewPager当前显示页的下标*/
	private int position=0;
	
	public  String u_name,jsonString = null,temp1,g_pic,g_name,g_price;
	public  int g_id;
	private Operaton operaton = new Operaton();
	public JSONObject temp;
	private TextView iv_baby_des,iv_baby_price;
	public List<Evaluation> eva = new ArrayList<Evaluation>();
	Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.babydetail_a);
		//得到保存的收藏信息
		SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
		u_name=sp.getString("u_name", "");
		g_id=sp.getInt("g_id", -1);
		getSaveCollection();
		popWindow = new BabyPopWindow(this);
		popWindow.setOnItemClickListener(this);
	}

	/**实现数据的刷新*/
	@Override
	protected void onResume() {
		super.onResume();
		initView();
	}

	@SuppressLint("NewApi")
	private void initView() {
		// 获取默认的NFC控制器
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (nfcAdapter == null) {
			Toast.makeText(this, "该设备不支持NFC", Toast.LENGTH_SHORT).show();
		}
		(findViewById(R.id.iv_back)).setOnClickListener(this);
		(findViewById(R.id.put_in)).setOnClickListener(this);
		(findViewById(R.id.buy_now)).setOnClickListener(this);
		iv_baby_collection=(ImageView) findViewById(R.id.iv_baby_collection);
		iv_baby_collection.setOnClickListener(this);
		all_choice_layout = (LinearLayout) findViewById(R.id.all_choice_layout);
		iv_baby_des = (TextView)findViewById(R.id.iv_baby_des);
		iv_baby_price = (TextView)findViewById(R.id.iv_baby_price);
		iv_baby = (ImageView)findViewById(R.id.iv_baby);
		/**以下为商品评论相关，目前只是默认填充*/
		SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
		if(g_id != -1){
			System.out.println(g_id);
			g_pic = sp.getString("g_pic","");
			g_name = sp.getString("g_name","");
			g_price = sp.getString("g_price","");
			iv_baby_des.setText(g_name);
			iv_baby_price.setText("￥" + g_price + "元");
		/*	new Thread(new Runnable() {

				public void run() {
					//将trade对象写出json形式字符串
					bitmap = operaton.getHttpBitmap(g_pic);
					if(bitmap != null) {
						Message msg = new Message();
						try {
							msg.obj = "1";
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						handler5.sendMessage(msg);
					}
				}
			}).start();*/
		}
		jsonString= "{\"g_id\":"+g_id+"}";
		System.out.println(jsonString);
		new Thread(new Runnable() {

			public void run() {
				//将trade对象写出json形式字符串
				temp1 = operaton.GetData("evaluationlist.action", jsonString);
				Message msg = new Message();
				if(temp1 != null) {
					msg.obj = "1";
				}
				else if(temp1 == null){
					msg.obj = "0";
				}
				handler4.sendMessage(msg);
			}
		}).start();
		
		if (isCollection) {
			//如果已经收藏，则显示收藏后的效果
			iv_baby_collection.setImageResource(R.drawable.second_2_collection);
		}
	}

	Handler handler4=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String msgobj=msg.obj.toString();
			if(msgobj.equals("1")){
				Gson gson = new Gson();
				listView = (ListView) findViewById(R.id.listView_Detail);
				listView.setFocusable(false);
				listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
				eva = gson.fromJson(temp1, new TypeToken<List<Evaluation>>() {}.getType());
				listView.setAdapter(new Adapter_ListView_detail(BabyActivity.this, eva));
			}
			else if(msgobj.equals("0")){
				listView = (ListView) findViewById(R.id.listView_Detail);
				listView = null;
			}
			super.handleMessage(msg);
		}
	};

	Handler handler5=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String msgobj=msg.obj.toString();
			if(msgobj.equals("1")){
				iv_baby.setImageBitmap(bitmap);
			}
			super.handleMessage(msg);
		}
	};
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_back:
			//返回
			finish();
			break;
		case R.id.iv_baby_collection:
			//收藏
			if (isCollection) {
				//提示是否取消收藏
				cancelCollection();
			}else {
				if(u_name.equals(""))
					Toast.makeText(BabyActivity.this, "尚未登陆", Toast.LENGTH_SHORT).show();
				else {
					new Thread(new Runnable() {

						public void run() {
							Favorite fav = new Favorite(g_id, u_name);
							WriteJson writeJson = new WriteJson();
							//将user对象写出json形式字符串
							jsonString = writeJson.getJsonData(fav);
							System.out.println(jsonString);
							temp = operaton.UpData("addfavorite.action", jsonString);
							if(temp != null) {
								Message msg = new Message();
								try {
									msg.obj = temp.getString("result").toString();
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
								handler2.sendMessage(msg);
							}
						}
					}).start();
				}
			}
			break;
		case R.id.put_in:
			//添加购物车
				setBackgroundBlack(all_choice_layout, 0);
				popWindow.showAsDropDown(view);
			break;
		case R.id.buy_now:
			//添加评论

			if(u_name.equals(""))
				Toast.makeText(BabyActivity.this, "请先登陆", Toast.LENGTH_SHORT).show();
			else {
				Trade trade = new Trade();
				trade.setG_id(g_id);
				trade.setU_name(u_name);
				WriteJson writeJson=new WriteJson();
				jsonString= writeJson.getJsonData(trade);
				System.out.println(jsonString);
				new Thread(new Runnable() {

					public void run() {
						Operaton operaton = new Operaton();
						//将trade对象写出json形式字符串
						temp = operaton.UpData("checktrade.action", jsonString);
						if(temp != null) {
							Message msg = new Message();
							try {
								msg.obj = temp.getString("result").toString();
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
							handler3.sendMessage(msg);
						}
					}
				}).start();
				//Intent intent = new Intent(BabyActivity.this, BabyCommentActivity.class);
				//startActivity(intent);
				break;
			}
		}
	}

	Handler handler2=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String msgobj=msg.obj.toString();
			if(msgobj.equals("1")){
				isCollection=true;
				//如果已经收藏，则显示收藏后的效果
				iv_baby_collection.setImageResource(R.drawable.second_2_collection);
				Toast.makeText(BabyActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(BabyActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}
	};

	Handler handler3=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String msgobj=msg.obj.toString();
			if(msgobj.equals("1")){
				Intent intent = new Intent(BabyActivity.this, BabyCommentActivity.class);
				startActivity(intent);
			}
			else
				Toast.makeText(BabyActivity.this, "未购买不能评论哟", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}
	};

	//点击弹窗的确认按钮的响应
	@Override
	public void onClickOKPop() {
		setBackgroundBlack(all_choice_layout, 1);

	}

	/** 控制背景变暗 0变暗 1变亮 */
	public void setBackgroundBlack(View view, int what) {
		switch (what) {
		case 0:
			view.setVisibility(View.VISIBLE);
			break;
		case 1:
			view.setVisibility(View.GONE);
			break;
		}
	}

	/**检查收藏标记*/
	private void getSaveCollection(){
		if(g_id == -1 || u_name.equals(""))
			isCollection = false;
		else{
			new Thread(new Runnable() {

				public void run() {
					Favorite fav = new Favorite(g_id,u_name);
					WriteJson writeJson=new WriteJson();
					//将user对象写出json形式字符串
					jsonString= writeJson.getJsonData(fav);
					System.out.println(jsonString+"  checkfav");
					temp = operaton.UpData( "checkfavorite.action",jsonString);
					if(temp != null) {
						Message msg = new Message();
						try {
							msg.obj = temp.getString("result").toString();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						handler.sendMessage(msg);
					}
				}
			}).start();
		}
	}

	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String msgobj=msg.obj.toString();
			if(msgobj.equals("1")) {
				isCollection = true;
				iv_baby_collection.setImageResource(R.drawable.second_2_collection);
			}
			super.handleMessage(msg);
		}
	};
	/**取消收藏*/
	private  void cancelCollection(){
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle("是否取消收藏");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				new Thread(new Runnable() {

					public void run() {

						Favorite fav = new Favorite(g_id,u_name);
						WriteJson writeJson=new WriteJson();
						//将user对象写出json形式字符串
						jsonString= writeJson.getJsonData(fav);
						System.out.println(jsonString);
						temp = operaton.UpData( "cancelfavorite.action", jsonString);
						if(temp != null) {
							Message msg = new Message();
							try {
								msg.obj = temp.getString("result").toString();
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
							handler1.sendMessage(msg);
						}
					}
				}).start();
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.create().show();
		
	}
	Handler handler1=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String msgobj=msg.obj.toString();
			if(msgobj.equals("1")){
				isCollection=false;
				//如果取消收藏，则显示取消收藏后的效果
				iv_baby_collection.setImageResource(R.drawable.second_2);
				Toast.makeText(BabyActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(BabyActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}
	};
}
