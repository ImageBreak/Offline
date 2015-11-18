package com.itau.jingdong.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itau.jingdong.R;

public class PersonalStore extends Activity implements OnClickListener {
    private Button upload,confirm;
    private EditText Sname,Sprice,Samount;
    private String G_name,G_price,G_amount;
    private Intent mIntent;
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

        upload.setOnClickListener(this);
        confirm.setOnClickListener(this);
        /**获得输入的文本*/
        G_name = Sname.getText().toString();
        G_price = Sprice.getText().toString();
        G_amount = Samount.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload:
                /**实现上传照片*/
                Toast.makeText(PersonalStore.this, "上传成功", Toast.LENGTH_SHORT).show();
                break;

            case R.id.confirm:
                /**商品添加完成*/
                Toast.makeText(PersonalStore.this, "提交成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
        }

}
