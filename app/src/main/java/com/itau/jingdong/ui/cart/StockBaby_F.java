package com.itau.jingdong.ui.cart;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itau.jingdong.R;


/**
 * 购物车界面中的库存界面
 *
 */
public class StockBaby_F extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= LayoutInflater.from(getActivity()).inflate(R.layout.cart_stock_f, null);
		initView(view);
		return view;
	}
	
	private void initView(View view){
		
		
		
	}

}
