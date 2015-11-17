package com.itau.jingdong.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itau.jingdong.R;


public class RegisterBormalActivity extends Activity implements OnClickListener {
	private EditText Uname,Upay,Upass;
	private String U_name,U_pay,U_pass;
	private Button upload,login;
	private Intent mIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register_normal);
		findViewById();
	}

	protected void findViewById() {
		Uname=(EditText)findViewById(R.id.U_name);
		Upay=(EditText)findViewById(R.id.U_pay);
		Upass=(EditText)findViewById(R.id.U_pass);
		upload=(Button)findViewById(R.id.upload);
		login=(Button)findViewById(R.id.login);

		U_name=Uname.getText().toString();
		U_pay=Upay.getText().toString();
		U_pass=Upass.getText().toString();
		upload.setOnClickListener(this);
		login.setOnClickListener(this);

	}


	@Override
	public   void onClick(View v){
		switch (v.getId()) {
			case R.id.upload:
				/**实现上传照片*/
				Toast.makeText(RegisterBormalActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
				break;

			case R.id.login:
				/**注册完成*/
				Toast.makeText(RegisterBormalActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
				finish();
				break;
		}

	}
	
}
