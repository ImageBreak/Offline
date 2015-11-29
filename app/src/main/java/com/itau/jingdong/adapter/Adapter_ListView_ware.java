package com.itau.jingdong.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.itau.jingdong.Operaton;
import com.itau.jingdong.bean.Good;
import com.itau.jingdong.http.CU_VolleyTool;
import com.itau.jingdong.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adapter_ListView_ware extends BaseAdapter {
	private Context context;
	@SuppressWarnings("unused")
	private List<Good> arrayList = new ArrayList<Good>();
	private Bitmap bitmap;
	private HolderView holderView=null;
	public Adapter_ListView_ware(Context context, List<Good> arrayList) {

		this.context = context;
		this.arrayList = arrayList;
	}

	public Adapter_ListView_ware(Context context) {
		this.context = context;

	}

	@Override
	public int getCount() {
		return (arrayList != null && arrayList.size() != 0) ? arrayList.size():0;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View currentView, ViewGroup arg2) {
		if (currentView == null) {
			holderView = new HolderView();
			currentView = LayoutInflater.from(context).inflate(R.layout.adapter_listview_ware, null);
			holderView.iv_pic = (ImageView) currentView.findViewById(R.id.iv_adapter_list_pic);
			holderView.tv_name = (TextView) currentView.findViewById(R.id.name);
			holderView.tv_price = (TextView) currentView.findViewById(R.id.price);
			currentView.setTag(holderView);
		} else {
			holderView = (HolderView) currentView.getTag();
		}
		if (arrayList.size() != 0 && !arrayList.get(position).getG_pic().equals("暂无图片")) {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(arrayList.get(position).getG_pic(), Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
			holderView.iv_pic.setImageBitmap(bitmap);
			holderView.tv_name.setText(arrayList.get(position).getG_name());
			holderView.tv_price.setText("￥" + arrayList.get(position).getG_price()+ "元");

		}
		return currentView;
	}


	public class HolderView {

		private ImageView iv_pic;
		private TextView tv_name;
		private TextView tv_price;

	}

}
