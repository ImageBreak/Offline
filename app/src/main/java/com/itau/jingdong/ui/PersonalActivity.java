package com.itau.jingdong.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itau.jingdong.AppManager;
import com.itau.jingdong.Operaton;
import com.itau.jingdong.R;
import com.itau.jingdong.ui.base.BaseActivity;
import com.itau.jingdong.utils.CommonTools;
import com.itau.jingdong.utils.ExitView;
import com.itau.jingdong.widgets.CustomScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;


public class PersonalActivity extends BaseActivity implements OnClickListener {

	private ImageView mBackgroundImageView = null,userpic;
	private Button mLoginButton,mMoreButton,mExitButton;
	private CustomScrollView mScrollView = null;
	private Intent mIntent=null;
	private ExitView exit;
	private LinearLayout Ly_login,Ly_Other,mHistory,mFavourite,mStore,mGood;
	private RelativeLayout Ly_personalInfo;
	private TextView username;
	private int LOGIN_CODE=100;
	public String u_name,u_pic;
	public Bitmap bitmap;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mBackgroundImageView = (ImageView) findViewById(R.id.personal_background_image);
		mLoginButton = (Button) findViewById(R.id.personal_login_button);
		mScrollView = (CustomScrollView) findViewById(R.id.personal_scrollView);
		mMoreButton=(Button)this.findViewById(R.id.personal_more_button);
		mExitButton=(Button)this.findViewById(R.id.personal_exit);

		mHistory=(LinearLayout)findViewById(R.id.mhistory);
		mFavourite=(LinearLayout)findViewById(R.id.mfavourite);
		mStore=(LinearLayout)findViewById(R.id.mstore);
		Ly_login=(LinearLayout)findViewById(R.id.login);
		Ly_personalInfo=(RelativeLayout)findViewById(R.id.personal);
		Ly_Other=(LinearLayout)findViewById(R.id.other_layout);
		username=(TextView)findViewById(R.id.username);
		userpic=(ImageView)findViewById(R.id.main_persionIcon);
		mGood=(LinearLayout)findViewById(R.id.manager);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mScrollView.setImageView(mBackgroundImageView);
		
		mLoginButton.setOnClickListener(this);
		mMoreButton.setOnClickListener(this);
		mExitButton.setOnClickListener(this);
		mHistory.setOnClickListener(this);
		mFavourite.setOnClickListener(this);
		mStore.setOnClickListener(this);
		mGood.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		//CommonTools.showShortToast(PersonalActivity.this, "稍后开放");
		SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
		u_name = sp.getString("u_name","");
		switch (v.getId()) {
		case R.id.personal_login_button:
			mIntent=new Intent(PersonalActivity.this, LoginActivity.class);
			
			startActivityForResult(mIntent, LOGIN_CODE);
			break;

		case R.id.personal_more_button:
			mIntent = new Intent(PersonalActivity.this, MoreActivity.class);
			startActivity(mIntent);

			break;

		case R.id.mhistory:
			if(u_name.equals(""))
				Toast.makeText(PersonalActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
			else {
				mIntent = new Intent(PersonalActivity.this, PersonalHistory.class);
				startActivity(mIntent);
			}
			break;

		case R.id.mstore:
			if(u_name.equals(""))
				Toast.makeText(PersonalActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
			else {
				mIntent = new Intent(PersonalActivity.this, PersonalStore.class);
				startActivity(mIntent);
			}
			break;

		case R.id.mfavourite:
			if(u_name.equals(""))
				Toast.makeText(PersonalActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
			else {
				mIntent = new Intent(PersonalActivity.this, PersonalFavouriteActivity.class);
				startActivity(mIntent);
			}
			break;

			case R.id.manager:
				if(u_name.equals(""))
					Toast.makeText(PersonalActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
				else {
					mIntent = new Intent(PersonalActivity.this, PersonalGoodManager.class);
					startActivity(mIntent);
				}
				break;

		case R.id.personal_exit:
			
			//实例化SelectPicPopupWindow
			exit = new ExitView(PersonalActivity.this, itemsOnClick);
			//显示窗口
			exit.showAtLocation(PersonalActivity.this.findViewById(R.id.layout_personal), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置

			break;
			
		default:
			break;
		}
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if(resultCode==20){
			System.out.println("进入结算操作");
			String name=data.getExtras().getString("name");
//			Log.i("name", name);
			username.setText(name);
			SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
			u_pic = sp.getString("u_pic","");
			new Thread(new Runnable() {

				public void run() {
					Operaton operaton = new Operaton();
					//将user对象写出json形式字符串
					bitmap = operaton.getHttpBitmap(u_pic,1);
					if(bitmap != null) {
						Message msg = new Message();
						msg.obj = "1";
						handler.sendMessage(msg);
					}
				}
			}).start();
			if(Ly_login.isShown()){
				Ly_personalInfo.setVisibility(View.VISIBLE);
				Ly_login.setVisibility(View.GONE);
				Ly_Other.setVisibility(View.VISIBLE);
			}
			Ly_personalInfo.setVisibility(View.VISIBLE);
			Ly_login.setVisibility(View.GONE);
			Ly_Other.setVisibility(View.VISIBLE);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	Handler handler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String msgobj=msg.obj.toString();
			if(msgobj.equals("1")) {
				userpic.setImageBitmap(bitmap);
			}
		}

	};
	//为弹出窗口实现监听类
    private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.btn_exit:
				AppManager.getInstance().AppExit(getApplicationContext());
				ImageLoader.getInstance().clearMemoryCache();
				
				break;
			case R.id.btn_cancel:
				PersonalActivity.this.dismissDialog(R.id.btn_cancel);
				
				break;
			default:
				break;
			}
		}
    };
	
}
