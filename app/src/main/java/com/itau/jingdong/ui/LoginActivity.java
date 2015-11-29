package com.itau.jingdong.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.itau.jingdong.R;
import com.itau.jingdong.bean.User;
import com.itau.jingdong.json.WriteJson;
import com.itau.jingdong.ui.base.BaseActivity;
import com.itau.jingdong.Operaton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements OnClickListener {
	
	private static final String Tag="LoginActivity";
	private EditText loginaccount,loginpassword;
	private ToggleButton isShowPassword;
	private Button loginBtn,register;
	private Intent mIntent;
	String u_name;
	String u_pass;
	public String jsonString;
	public JSONObject temp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findViewById();
		initView();
	}
	
	@Override
	protected void findViewById() {
		loginaccount=(EditText)this.findViewById(R.id.loginaccount);
		loginpassword=(EditText)this.findViewById(R.id.loginpassword);
		isShowPassword=(ToggleButton)this.findViewById(R.id.isShowPassword);
		loginBtn=(Button)this.findViewById(R.id.login);
		register=(Button)this.findViewById(R.id.register);

	}

	@Override
	protected void initView() {

		register.setOnClickListener(this);
		
		isShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				Log.i(Tag, "开关按钮状态="+isChecked);
			
				if(isChecked){
					//隐藏
					loginpassword.setInputType(0x90);
				}else{
					//明文显示	
					loginpassword.setInputType(0x81);
				}
				Log.i("togglebutton", ""+isChecked);
			}
		});
		loginBtn.setOnClickListener(this);
	
	}

	@Override
	public void onClick(View v) {
	switch (v.getId()) {
	case R.id.register:
		mIntent=new Intent(LoginActivity.this, RegisterBormalActivity.class);
		startActivity(mIntent);
		break;

		
	case R.id.login:
		userlogin();
		break;
		
	default:
		break;
	}
		
	}

	private void userlogin() {
		u_name=loginaccount.getText().toString().trim();
		u_pass=loginpassword.getText().toString().trim();
		
		if(u_name.equals("")){
			DisplayToast("用户名不能为空!");
		}
		else if(u_pass.equals("")){
			DisplayToast("密码不能为空!");
		}
		
		else if(u_name.equals("test")&&u_pass.equals("123")){
			DisplayToast("登陆成功!");
			Intent data=new Intent();
			SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
			SharedPreferences.Editor ed;
			ed = sp.edit();
			ed.putString("u_name", u_name);
			ed.commit();
            data.putExtra("name", u_name);
//            data.putExtra("values", 100);
            setResult(20, data);
			LoginActivity.this.finish();
		}
		else {
			new Thread(new Runnable() {

				public void run() {
					Operaton operaton = new Operaton();
					User user=new User(u_name,u_pass);
					WriteJson writeJson=new WriteJson();
					//将user对象写出json形式字符串
					jsonString= writeJson.getJsonData(user);
					System.out.println(jsonString);
					temp = operaton.UpData("login.action", jsonString);
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
			SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
			SharedPreferences.Editor ed;
			String result=(String) msg.obj;
			if(result.equals("1")) {
				ed = sp.edit();
				ed.putString("u_name", u_name);
				try {
					ed.putString("u_pic", temp.getString("u_pic").toString());
				}
				catch (Exception e){
					throw new RuntimeException(e);
				}
				ed.commit();
				mIntent = new Intent();
				mIntent.putExtra("name", u_name);
				setResult(20, mIntent);
				LoginActivity.this.finish();
			}
			else if(result.equals("0")){
				try {
					Toast.makeText(LoginActivity.this, temp.getString("error").toString(), Toast.LENGTH_SHORT).show();
				}
				catch (Exception e){
					throw new RuntimeException(e);
				}
			}
			super.handleMessage(msg);
		}
	};

}
