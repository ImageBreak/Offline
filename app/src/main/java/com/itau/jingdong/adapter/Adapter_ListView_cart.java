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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.itau.jingdong.Operaton;
import com.itau.jingdong.R;
import com.itau.jingdong.http.CU_VolleyTool;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter_ListView_cart extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
	private onCheckedChanged listener;
	public Bitmap bitmap;
	private HolderView holderView = null;

	public Adapter_ListView_cart(Context context, ArrayList<HashMap<String, Object>> arrayList) {

		this.context = context;
		this.arrayList = arrayList;
	}

	public Adapter_ListView_cart(Context context) {
		this.context = context;

	}

	@Override
	public int getCount() {
		return (arrayList != null && arrayList.size() == 0) ? 0: arrayList.size();
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
			currentView = LayoutInflater.from(context).inflate(R.layout.adapter_listview_cart, null);
			holderView.tv_name = (TextView) currentView.findViewById(R.id.tv_name);
			holderView.tv_num = (TextView) currentView.findViewById(R.id.tv_num);
			holderView.tv_type_color = (TextView) currentView.findViewById(R.id.tv_type_color);
			holderView.tv_price = (TextView) currentView.findViewById(R.id.tv_price);
			holderView.cb_choice = (CheckBox) currentView.findViewById(R.id.cb_choice);
			holderView.iv_pic = (ImageView) currentView.findViewById(R.id.iv_pic);
			currentView.setTag(holderView);
		} else {
			holderView = (HolderView) currentView.getTag();
		}
		if (arrayList.size() != 0  && !arrayList.get(position).get("pic").toString().equals("暂无图片")) {

			byte[] bitmapArray;
			bitmapArray = Base64.decode(arrayList.get(position).get("pic").toString(), Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
			holderView.iv_pic.setImageBitmap(bitmap);
			holderView.tv_num.setText("x" + arrayList.get(position).get("num"));
			holderView.tv_type_color.setText("类型:" + arrayList.get(position).get("type").toString() + "    颜色:" + arrayList.get(position).get("color").toString());
			holderView.tv_name.setText(arrayList.get(position).get("name").toString());
			holderView.tv_price.setText("￥" + arrayList.get(position).get("price").toString());
			holderView.cb_choice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean choice) {
					listener.getChoiceData(position, choice);
				}
			});

		
		}
		return currentView;
	}

	public class HolderView {

		private TextView tv_type_color;
		private TextView tv_num;
		private TextView tv_name;
		private TextView tv_price;
		private CheckBox cb_choice;
		private ImageView iv_pic;

	}
	
	public interface onCheckedChanged{
		
		public void getChoiceData(int position, boolean isChoice);
	}
	public void setOnCheckedChanged(onCheckedChanged listener){
		this.listener=listener;
	}

}
