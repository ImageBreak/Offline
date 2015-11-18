package com.itau.jingdong.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.itau.jingdong.home.BabyActivity;
import com.itau.jingdong.http.CU_JSONResolve;
import com.itau.jingdong.http.GetHttp;
import com.itau.jingdong.R;
import com.itau.jingdong.adapter.Adapter_ListView_ware;
import com.itau.jingdong.xListview.XListView;
import com.itau.jingdong.xListview.XListView.IXListViewListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

public class PersonalFavouriteActivity extends Activity implements  IXListViewListener {

    //显示所有商品的列表
    private XListView listView;

    private int pageIndex = 0;
    /**存储网络返回的数据*/
    private HashMap<String, Object> hashMap;
    /**存储网络返回的数据中的data字段*/
    private ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_favourite);
        initView();
        //请求网络数据
        new WareTask().execute();
    }

    protected void initView() {

        listView = (XListView) findViewById(R.id.listView_ware);
        listView.setXListViewListener(this);
        // 设置可以进行下拉加载的功能
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
    }


    private class WareTask extends AsyncTask<Void, Void, HashMap<String, Object>> {
        ProgressDialog dialog=null;
        @Override
        protected void onPreExecute() {
            if (dialog==null) {
                dialog= ProgressDialog.show(PersonalFavouriteActivity.this, "", "正在加载...");
                dialog.show();
            }
        }

        @Override
        protected HashMap<String, Object> doInBackground(Void... arg0) {
            String url = "";
            if (pageIndex == 0) {
                url = "http://192.168.0.111:3000/taoBaoQuery";
            } else {
                url = "http://192.168.0.111:3000/taoBaoQuery?pageIndex=" + pageIndex;
            }
            //请求数据，返回json
            String json = GetHttp.RequstGetHttp(url);
            //第一层的数组类型字段
            String[] LIST1_field = { "data" };
            //第二层的对象类型字段
            String[] STR2_field = { "id", "name", "price", "pic" };
            ArrayList<String[]> aL_STR2_field = new ArrayList<String[]>();
            //第二层的对象类型字段放入第一层的数组类型字段中
            aL_STR2_field.add(STR2_field);
            //解析返回的json
            hashMap = CU_JSONResolve.parseHashMap2(json, null, LIST1_field, aL_STR2_field);
            return hashMap;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(HashMap<String, Object> result) {

            if (dialog!=null&&dialog.isShowing()) {
                dialog.dismiss();
                dialog=null;
            }

            //如果网络数据请求失败，那么显示默认的数据
            if (result != null && result.get("data") != null) {
                //得到data字段的数据
                arrayList.addAll((Collection<? extends HashMap<String, Object>>) result.get("data"));
                listView.setAdapter(new Adapter_ListView_ware(PersonalFavouriteActivity.this, arrayList));
                listView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Intent intent = new Intent(PersonalFavouriteActivity.this, BabyActivity.class);
                        startActivity(intent);
                    }
                });

            }else {
                listView.setAdapter(new Adapter_ListView_ware(PersonalFavouriteActivity.this));
                listView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Intent intent = new Intent(PersonalFavouriteActivity.this, BabyActivity.class);
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
        pageIndex = 0;
        arrayList.clear();
        new WareTask().execute();
        // 停止刷新和加载
        onLoad();
    }
    /** 加载更多 */
    @Override
    public void onLoadMore() {
        pageIndex += 1;
        if (pageIndex >= 4) {
            Toast.makeText(this, "已经最后一页了", Toast.LENGTH_SHORT).show();
            onLoad();
            return;
        }
        new WareTask().execute();
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