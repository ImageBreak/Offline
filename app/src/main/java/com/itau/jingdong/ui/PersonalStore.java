package com.itau.jingdong.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.itau.jingdong.Operaton;
import com.itau.jingdong.R;
import com.itau.jingdong.bean.Good;
import com.itau.jingdong.json.WriteJson;
import com.itau.jingdong.task.ExDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PersonalStore extends Activity implements OnClickListener {
    private Button upload,confirm;
    private ImageView imgphoto;
    private EditText Sname,Sprice,Samount;
    private String g_name,g_price,g_pic,g_type,gamount,u_name;
    private int g_amount;
    private RadioButton daily,ebook,cloth,digital;
    private Intent mIntent;

    private String filepath=null,jsonString=null;
    private static final int REQUEST_EX = 1;
    public JSONObject temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_store);
        findViewById();
    }

    public void  findViewById(){
        upload = (Button)findViewById(R.id.upload);
        confirm = (Button)findViewById(R.id.confirm);
        Sname = (EditText)findViewById(R.id.S_name);
        Sprice = (EditText)findViewById(R.id.S_price);
        Samount = (EditText)findViewById(R.id.S_amount);
        daily = (RadioButton)findViewById(R.id.ckdaily);
        ebook = (RadioButton)findViewById(R.id.ckebook);
        cloth = (RadioButton)findViewById(R.id.ckcloth);
        digital = (RadioButton)findViewById(R.id.ckdigital);
        imgphoto = (ImageView)findViewById(R.id.imgphoto);

        upload.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /**获得输入的文本*/
        g_name = Sname.getText().toString();
        g_price = Sprice.getText().toString();
        gamount = Samount.getText().toString();
        g_amount = Integer.parseInt(gamount);
        if(daily.isChecked())
            g_type = "家电";
        if(ebook.isChecked())
            g_type = "图书";
        if(cloth.isChecked())
            g_type = "衣服";
        if(digital.isChecked())
            g_type = "数码";
        switch (v.getId()) {
            case R.id.upload:
                /**实现上传照片*/
                mIntent = new Intent();
                mIntent.putExtra("explorer_title", getString(R.string.dialog_read_from_dir));
                mIntent.setDataAndType(Uri.fromFile(new File("/sdcard")), "*/*");
                mIntent.setClass(PersonalStore.this, ExDialog.class);
                startActivityForResult(mIntent, REQUEST_EX);
                //Toast.makeText(PersonalStore.this, "上传成功", Toast.LENGTH_SHORT).show();
                break;

            case R.id.confirm:
                /**商品添加完成*/
                SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
                u_name = sp.getString("u_name","");
                new Thread(new Runnable() {

                    public void run() {

                        Operaton operaton=new Operaton();
                        Message msg = new Message();
                        if(filepath != null) {
                            if(filepath != null) {
                                try {
                                    temp = operaton.addpic(filepath, "upload", 2);
                                    if (temp != null && temp.getString("result").toString().equals("1"))
                                        g_pic = temp.getString("imagepath").toString();
                                    else
                                        msg.obj = "-1";
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        //先进行图片上传的操作，然后服务器返回图片保存在服务器的路径，
                        Good good=new Good(g_name, g_price, g_pic, g_amount, g_type,u_name);
                        //构造一个user对象
                        WriteJson writeJson=new WriteJson();
                        //将user对象写出json形式字符串
                        jsonString= writeJson.getJsonData(good);
                        System.out.println(jsonString);
                        temp = operaton.UpData("addgood.action", jsonString);
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
                //Toast.makeText(PersonalStore.this, "提交成功", Toast.LENGTH_SHORT).show();
                finish();
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
                Toast.makeText(PersonalStore.this, "提交成功", Toast.LENGTH_SHORT).show();
                PersonalStore.this.finish();
            }
            else if(msgobj.equals("0")){
                Toast.makeText(PersonalStore.this, "提交失败", Toast.LENGTH_SHORT).show();
            }
            else if(msgobj.equals("-1"))
                Toast.makeText(PersonalStore.this, "图片上传失败", Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            Uri uri = intent.getData();
            filepath=uri.toString().substring(6);
            System.out.println(filepath);
            //商品的头像是不是图片格式
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
                confirm.setClickable(true);
            }
            else
            {
                confirm.setClickable(false);
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
