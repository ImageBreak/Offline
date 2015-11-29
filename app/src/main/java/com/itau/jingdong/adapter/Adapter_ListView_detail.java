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
import com.itau.jingdong.Operaton;
import com.itau.jingdong.R;
import com.itau.jingdong.bean.Evaluation;
import com.itau.jingdong.http.CU_VolleyTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adapter_ListView_detail extends BaseAdapter {
	private Context context;
	@SuppressWarnings("unused")
	private List<Evaluation> arrayList = new ArrayList<Evaluation>();
	private Bitmap bitmap;
	private HolderView holderView=null;
	
	@SuppressWarnings("unchecked")
	public Adapter_ListView_detail(Context context,List<Evaluation> arrayList){
		this.context=context;
		this.arrayList=arrayList;
	}
	
	public Adapter_ListView_detail(Context context){
		this.context=context;
		
	}
	
	@Override
	public int getCount() {
		return (arrayList != null && arrayList.size() != 0) ?  arrayList.size() : 0;
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

		if (currentView==null) {
			holderView=new HolderView();
			currentView= LayoutInflater.from(context).inflate(R.layout.adapter_listview_detail, null);
			holderView.tv_name = (TextView)currentView.findViewById(R.id.name_user);
			holderView.tv_evalu = (TextView)currentView.findViewById(R.id.content);
			holderView.e_time = (TextView)currentView.findViewById(R.id.time);
			holderView.iv_pic = (ImageView)currentView.findViewById(R.id.icon);
			currentView.setTag(holderView);
		}else {
			holderView=(HolderView) currentView.getTag();
		}
		if (arrayList != null && arrayList.size() != 0  && !arrayList.get(position).getU_pic().equals("暂无图片")) {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(arrayList.get(position).getU_pic(), Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
			holderView.iv_pic.setImageBitmap(bitmap);
			holderView.tv_name.setText(arrayList.get(position).getU_name());
			holderView.tv_evalu.setText(arrayList.get(position).getE_text());
			holderView.e_time.setText(arrayList.get(position).getE_time());
		}
		
		return currentView;
	}

	@SuppressWarnings("unused")
	public class HolderView {
		
		private ImageView iv_pic;
		private TextView tv_name;
		private TextView tv_evalu;
		private TextView e_time;
		
	}

}
