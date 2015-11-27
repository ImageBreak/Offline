package com.itau.jingdong.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itau.jingdong.Operaton;
import com.itau.jingdong.R;
import com.itau.jingdong.adapter.Adapter_ListView_history;
import com.itau.jingdong.adapter.Adapter_ListView_ware;
import com.itau.jingdong.bean.Trade;
import com.itau.jingdong.home.BabyActivity;
import com.itau.jingdong.xListview.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonalHistory extends Activity  implements XListView.IXListViewListener {

    private String jsonString=null;
    public String temp;
    public List<Trade> trades = new ArrayList<Trade>();
    //显示所有商品的列表
    private XListView listView;

   // private int pageIndex = 0;
    /**存储网络返回的数据*/
    //private HashMap<String, Object> hashMap;
    /**存储网络返回的数据中的data字段*/
    //private ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_history);
    }

    /**实现数据的刷新*/
    @Override
    protected void onResume() {
        super.onResume();
        initView();
        //请求网络数据
        new WareTask().execute();
    }


    private void initView(){

        listView = (XListView) findViewById(R.id.listView_ware);
        listView.setXListViewListener(this);
        // 设置可以进行下拉加载的功能
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);

    }

    private class WareTask extends AsyncTask<Void, Void, List<Trade>> {
        ProgressDialog dialog=null;
        @Override
        protected void onPreExecute() {
            if (dialog==null) {
                dialog= ProgressDialog.show(PersonalHistory.this, "", "正在加载...");
                dialog.show();
            }
        }

        @Override
        protected List<Trade> doInBackground(Void... arg0) {
            SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
            String u_name = sp.getString("u_name","");
            jsonString = "{\"u_name\":\""+u_name+"\"}";
            System.out.println(jsonString);
            Operaton operaton = new Operaton();
            //将trade对象写出json形式字符串
            temp = operaton.GetData("tradelist.action", jsonString);
            if(temp != null) {
                Gson gson = new Gson();
                trades = gson.fromJson(temp, new TypeToken<List<Trade>>() {
                }.getType());
            }
            else
                trades = null;
            return trades;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(final List<Trade> result) {

            if (dialog!=null&&dialog.isShowing()) {
                dialog.dismiss();
                dialog=null;
            }

            //如果网络数据请求失败，那么显示默认的数据
            if (result != null ) {
                //得到data字段的数据
                listView.setAdapter(new Adapter_ListView_history(PersonalHistory.this, result));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
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
                        Intent intent = new Intent(PersonalHistory.this, BabyActivity.class);
                        startActivity(intent);
                    }
                });

            }else {
                listView.setAdapter(new Adapter_ListView_ware(PersonalHistory.this));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Intent intent = new Intent(PersonalHistory.this, BabyActivity.class);
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
        trades.clear();
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
