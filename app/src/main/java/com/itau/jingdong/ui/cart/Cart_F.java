package com.itau.jingdong.ui.cart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itau.jingdong.Data.Data;
import com.itau.jingdong.R;

import java.util.HashMap;

/**
 * 购物车主界面
 *
 */
public class Cart_F extends FragmentActivity implements OnClickListener {
	private TextView bt_cart_all, bt_cart_low, bt_cart_stock, bt_cart_edit;

	private View show_cart_all, show_cart_low, show_cart_stock;

	private AllBaby_F allBaby_F;
	private LowBaby_F lowBaby_F;
	private StockBaby_F stockBaby_F;
	private boolean isDel=true;
	@Override
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_f);
		SharedPreferences sp = getSharedPreferences("SAVE_CART", MODE_PRIVATE);
		SharedPreferences.Editor ed = sp.edit();
		ed.clear();
		ed.commit();
		return ;
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
		SharedPreferences sp = getSharedPreferences("SAVE_CART", MODE_PRIVATE);
		int size = sp.getInt("ArrayCart_size", 0);
		System.out.println(size);
		for (int i = 0; i < size; i++) {
			hashMap.put("type", sp.getString("ArrayCart_type_" + i, ""));
			hashMap.put("color", sp.getString("ArrayCart_color_" + i, ""));
			hashMap.put("num", sp.getString("ArrayCart_num_" + i, ""));
			hashMap.put("name", sp.getString("ArrayCart_name_" + i, ""));
			hashMap.put("price", sp.getString("ArrayCart_price_" + i, ""));
			hashMap.put("pic", sp.getString("ArrayCart_pic_" + i, ""));
			hashMap.put("g_id", sp.getString("ArrayCart_g_id_" + i, ""));
			hashMap.put("u_name",sp.getString("ArrayCart_u_name_"+i, ""));
			Data.arrayList_cart.add(hashMap);
		}
	}

	private void initView() {
		((TextView)findViewById(R.id.tv_top_txtTitle)).setText("我的购物车");

		bt_cart_all = (TextView) findViewById(R.id.bt_cart_all);
		bt_cart_low = (TextView) findViewById(R.id.bt_cart_low);
		bt_cart_stock = (TextView) findViewById(R.id.bt_cart_stock);
		bt_cart_edit = (TextView) findViewById(R.id.tv_top_edit);

		show_cart_all = findViewById(R.id.show_cart_all);
		show_cart_low = findViewById(R.id.show_cart_low);
		show_cart_stock = findViewById(R.id.show_cart_stock);

		bt_cart_all.setOnClickListener(this);
		bt_cart_low.setOnClickListener(this);
		bt_cart_stock.setOnClickListener(this);
		bt_cart_edit.setOnClickListener(this);

		allBaby_F = new AllBaby_F();
		addFragment(allBaby_F);
		showFragment(allBaby_F);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_top_edit:
			if (allBaby_F!=null&&isDel) {
				removeFragment(allBaby_F);
				allBaby_F=null;
				allBaby_F=new AllBaby_F("删除");
				addFragment(allBaby_F);
				showFragment(allBaby_F);
				isDel=false;
				bt_cart_edit.setText("完成");
				Data.Allprice_cart=0;
				
			}else if (!isDel&&allBaby_F!=null) {
				removeFragment(allBaby_F);
				allBaby_F=null;
				allBaby_F=new AllBaby_F();
				addFragment(allBaby_F);
				showFragment(allBaby_F);
				isDel=true;
				Data.Allprice_cart=0;
				bt_cart_edit.setText("编辑");
			}
			break;
		case R.id.bt_cart_all:
			if (allBaby_F == null) {
				allBaby_F = new AllBaby_F();
				addFragment(allBaby_F);
				showFragment(allBaby_F);
			} else {
				showFragment(allBaby_F);
			}
			show_cart_all.setBackgroundColor(getResources().getColor(R.color.bg_Black));
			show_cart_low.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_stock.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			break;
		case R.id.bt_cart_low:
			if (lowBaby_F == null) {
				lowBaby_F = new LowBaby_F();
				addFragment(lowBaby_F);
				showFragment(lowBaby_F);
			} else {
				showFragment(lowBaby_F);
			}
			show_cart_low.setBackgroundColor(getResources().getColor(R.color.bg_Black));
			show_cart_all.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_stock.setBackgroundColor(getResources().getColor(R.color.bg_Gray));

			break;
		case R.id.bt_cart_stock:
			if (stockBaby_F == null) {
				stockBaby_F = new StockBaby_F();
				addFragment(stockBaby_F);
				showFragment(stockBaby_F);
			} else {
				showFragment(stockBaby_F);
			}
			show_cart_stock.setBackgroundColor(getResources().getColor(R.color.bg_Black));
			show_cart_all.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
			show_cart_low.setBackgroundColor(getResources().getColor(R.color.bg_Gray));

			break;

		default:
			break;
		}
	}

	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.add(R.id.show_cart_view, fragment);
		ft.commitAllowingStateLoss();
	}
	/** 删除Fragment **/
	public void removeFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.remove(fragment);
		ft.commitAllowingStateLoss();
	}

	/** 显示Fragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		if (allBaby_F != null) {
			ft.hide(allBaby_F);
		}
		if (lowBaby_F != null) {
			ft.hide(lowBaby_F);
		}
		if (stockBaby_F != null) {
			ft.hide(stockBaby_F);
		}

		ft.show(fragment);
		ft.commitAllowingStateLoss();

	}

}
