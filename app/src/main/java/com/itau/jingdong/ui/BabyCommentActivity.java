package com.itau.jingdong.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itau.jingdong.Operaton;
import com.itau.jingdong.R;
import com.itau.jingdong.bean.Evaluation;
import com.itau.jingdong.json.WriteJson;

import org.json.JSONObject;

public class BabyCommentActivity extends Activity implements OnClickListener  {

    private EditText Gtext;
    private Button confirm;
    private String G_text;
    private Intent mIntent;
    public String jsonString;
    public JSONObject temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_comment);
        findViewById();
    }

    protected void findViewById() {
        Gtext=(EditText)findViewById(R.id.G_text);
        confirm=(Button)findViewById(R.id.confirm);

        confirm.setOnClickListener(this);

    }

    @Override
    public   void onClick(View v){
        G_text=Gtext.getText().toString();
        SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
        String u_name = sp.getString("u_name", "");
        int g_id = sp.getInt("g_id",-1);
        switch (v.getId()) {
            case R.id.confirm:
                /**评论完成*/
                Evaluation eva = new Evaluation(g_id,u_name,G_text);
                WriteJson writeJson=new WriteJson();
                jsonString= writeJson.getJsonData(eva);
                System.out.println(jsonString);
                new Thread(new Runnable() {

                    public void run() {
                        Operaton operaton = new Operaton();
                        //将trade对象写出json形式字符串
                        temp = operaton.UpData("addevaluation.action", jsonString);
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
                finish();
                break;
        }
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String msgobj=msg.obj.toString();
            if(msgobj.equals("1"))
                Toast.makeText(BabyCommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
            else if(msgobj.equals("0"))
                Toast.makeText(BabyCommentActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
        }
    };
}
