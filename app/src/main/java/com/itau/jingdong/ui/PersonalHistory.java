package com.itau.jingdong.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.itau.jingdong.Data.Data;
import com.itau.jingdong.R;

import java.util.HashMap;

public class PersonalHistory extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_history);
    }

    /**实现数据的刷新*/
    @Override
    protected void onResume() {
        super.onResume();
        getSaveData();
        initView();
    }

    /** 得到保存的购物车数据 */
    private void getSaveData() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        SharedPreferences sp = getSharedPreferences("SAVE_CART", Context.MODE_PRIVATE);
        int size = sp.getInt("ArrayCart_size", 0);
        for (int i = 0; i < size; i++) {
            hashMap.put("type", sp.getString("ArrayCart_type_" + i, ""));
            hashMap.put("color", sp.getString("ArrayCart_color_" + i, ""));
            hashMap.put("num", sp.getString("ArrayCart_num_" + i, ""));
            Data.arrayList_cart.add(hashMap);
        }
    }

    private void initView(){

    }
}
