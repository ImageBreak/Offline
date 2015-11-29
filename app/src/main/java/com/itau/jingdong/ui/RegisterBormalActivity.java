package com.itau.jingdong.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.itau.jingdong.R;
import com.itau.jingdong.task.ExDialog;
import com.itau.jingdong.Operaton;
import com.itau.jingdong.bean.User;
import com.itau.jingdong.json.WriteJson;
import com.itau.jingdong.json.JsonUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class RegisterBormalActivity extends Activity implements OnClickListener {
	private EditText Uname,Upay,Upass,Urepass;
	private ImageView imgphoto;
	private String u_name,u_pay,u_pass,u_repass,u_pic,str;
	private Button upload,login;
	private Intent mIntent;
	private String filepath=null,jsonString=null;
	private static final int REQUEST_EX = 1;
	public JSONObject temp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register_normal);
		findViewById();
		//Uname.setOnFocusChangeListener(new EtusernameOnFocusChange());
	}

	protected void findViewById() {
		Uname=(EditText)findViewById(R.id.U_name);
		Upay=(EditText)findViewById(R.id.U_pay);
		Upass=(EditText)findViewById(R.id.U_pass);
		Urepass=(EditText)findViewById(R.id.U_repass);
		upload=(Button)findViewById(R.id.upload);
		login=(Button)findViewById(R.id.login);
		imgphoto=(ImageView)findViewById(R.id.imgphoto);

		upload.setOnClickListener(this);
		login.setOnClickListener(this);

	}


	@Override
	public   void onClick(View v){
		u_name=Uname.getText().toString();
		u_pay=Upay.getText().toString();
		u_pass=Upass.getText().toString();
		u_repass=Urepass.getText().toString();
		switch (v.getId()) {
			case R.id.upload:
				/**实现上传照片*/
				mIntent = new Intent();
				mIntent.putExtra("explorer_title", getString(R.string.dialog_read_from_dir));
				mIntent.setDataAndType(Uri.fromFile(new File("/sdcard")), "*/*");
				mIntent.setClass(RegisterBormalActivity.this, ExDialog.class);
				startActivityForResult(mIntent, REQUEST_EX);
				//Toast.makeText(RegisterBormalActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
				break;

			case R.id.login:
				/**注册完成*/
				new Thread(new Runnable() {

					public void run() {
						Operaton operaton=new Operaton();
						Message msg = new Message();
						if(filepath != null) {
							try {
								temp = operaton.addpic(filepath, "upload", 1);
								if(temp != null && temp.getString("result").toString().equals("1"))
									u_pic = temp.getString("imagepath").toString();
								else
									msg.obj = "-1";
							}
							catch(Exception e){
								throw new RuntimeException(e);
							}
							//先进行图片上传的操作，然后服务器返回图片保存在服务器的路径，
						}
						User user=new User(u_name,u_pic,u_pay,u_pass,u_repass);
						//构造一个user对象
						WriteJson writeJson=new WriteJson();
						//将user对象写出json形式字符串
						jsonString= writeJson.getJsonData(user);
						System.out.println(jsonString);
						temp= operaton.UpData("register.action", jsonString);
						if(temp != null) {
							try {
								msg.obj = temp.getString("result").toString();
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
							handler1.sendMessage(msg);
						}
					}
				}).start();
				//Toast.makeText(RegisterBormalActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
				//finish();
				break;
		}

	}

	Handler handler1=new Handler()
	{
		@Override
		public void handleMessage(Message msg) {

			String msgobj=msg.obj.toString();
			if(msgobj.equals("1"))
			{
				Toast.makeText(RegisterBormalActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
				RegisterBormalActivity.this.finish();
			}
			else if(msgobj.equals("0")){
				try {
					Toast.makeText(RegisterBormalActivity.this, temp.getString("error").toString(), Toast.LENGTH_SHORT).show();
				}
				catch (Exception e){
					throw new RuntimeException(e);
				}
			}
			else if(msgobj.equals("-1"))
				Toast.makeText(RegisterBormalActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {

			Uri uri = intent.getData();
			filepath=uri.toString().substring(6);
			System.out.println(filepath);
			//用户的头像是不是图片格式
			if(filepath.endsWith("jpg")||filepath.endsWith("png"))
			{
				File file=new File(filepath);
				try {
					InputStream inputStream=new FileInputStream(file);
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					imgphoto.setImageBitmap(bitmap);//如果是就将图片显示出来
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				login.setClickable(true);
			}
			else
			{
				login.setClickable(false);
				alert();
			}

		}
	}
	private void alert()
	{
		Dialog dialog = new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								filepath = null;
							}
						})
				.create();
		dialog.show();
	}

}


