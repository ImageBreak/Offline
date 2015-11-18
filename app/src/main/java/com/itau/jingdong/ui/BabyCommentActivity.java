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

public class BabyCommentActivity extends Activity implements OnClickListener  {

    private EditText Gtext;
    private Button confirm;
    private String G_text;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_comment);
        findViewById();
    }

    protected void findViewById() {
        Gtext=(EditText)findViewById(R.id.G_text);
        confirm=(Button)findViewById(R.id.confirm);

        G_text=Gtext.getText().toString();

        confirm.setOnClickListener(this);

    }

    @Override
    public   void onClick(View v){
        switch (v.getId()) {
            case R.id.confirm:
                /**评论完成*/
                Toast.makeText(BabyCommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

}
