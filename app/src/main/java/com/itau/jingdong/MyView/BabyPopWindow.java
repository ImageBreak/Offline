package com.itau.jingdong.MyView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.itau.jingdong.Data.Data;
import com.itau.jingdong.Operaton;
import com.itau.jingdong.R;

import java.util.HashMap;


/**
 * 宝贝详情界面的弹窗
 *
 */
@SuppressLint("CommitPrefEdits")
public class BabyPopWindow implements OnDismissListener, OnClickListener {
	private TextView pop_choice_16g,pop_choice_32g,pop_choice_16m,pop_choice_32m,pop_choice_black,pop_choice_white,pop_add,pop_reduce,pop_num,pop_ok,pop_price,pop_amount;
	private ImageView pop_del,pic;
	
	private PopupWindow popupWindow;
	private OnItemClickListener listener;
	private final int ADDORREDUCE=1;
	private Context context;
	/**保存选择的颜色的数据*/
	private String str_color="";
	/**保存选择的类型的数据*/
	private String str_type="";
	public String g_pic,g_price;
	public int g_amount;
	private Operaton operaton = new Operaton();
	Bitmap bitmap;

	public BabyPopWindow(Context context) {
		this.context=context;
		View view= LayoutInflater.from(context).inflate(R.layout.adapter_popwindow, null);
		pop_choice_16g=(TextView) view.findViewById(R.id.pop_choice_16g);
		pop_choice_32g=(TextView) view.findViewById(R.id.pop_choice_32g);
		pop_choice_16m=(TextView) view.findViewById(R.id.pop_choice_16m);
		pop_choice_32m=(TextView) view.findViewById(R.id.pop_choice_32m);
		pop_choice_black=(TextView) view.findViewById(R.id.pop_choice_black);
		pop_choice_white=(TextView) view.findViewById(R.id.pop_choice_white);
		pop_add=(TextView) view.findViewById(R.id.pop_add);
		pop_reduce=(TextView) view.findViewById(R.id.pop_reduce);
		pop_num=(TextView) view.findViewById(R.id.pop_num);
		pop_ok=(TextView) view.findViewById(R.id.pop_ok);
		pop_price=(TextView) view.findViewById(R.id.pop_price);
		pop_amount=(TextView) view.findViewById(R.id.pop_amount);
		pop_del=(ImageView) view.findViewById(R.id.pop_del);
		pic=(ImageView) view.findViewById(R.id.iv_adapter_grid_pic);
		
		pop_choice_16g.setOnClickListener(this);
		pop_choice_32g.setOnClickListener(this);
		pop_choice_16m.setOnClickListener(this);
		pop_choice_32m.setOnClickListener(this);
		pop_choice_black.setOnClickListener(this);
		pop_choice_white.setOnClickListener(this);
		pop_add.setOnClickListener(this);
		pop_reduce.setOnClickListener(this);
		pop_ok.setOnClickListener(this);
		pop_del.setOnClickListener(this);

		SharedPreferences sp = context.getSharedPreferences("info", context.MODE_PRIVATE);
		int g_id = sp.getInt("g_id",-1);
		if(g_id != -1){
			g_pic = sp.getString("g_pic","");
			g_price = sp.getString("g_price","");
			g_amount = sp.getInt("g_amount", -1);
			pop_price.setText(g_price);
			pop_amount.setText(String.valueOf(g_amount));
			byte[] bitmapArray;
			bitmapArray = Base64.decode(g_pic, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
			pic.setImageBitmap(bitmap);
		}
		popupWindow=new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//设置popwindow的动画效果
		popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
	}

	
	public interface OnItemClickListener{
		/** 设置点击确认按钮时监听接口 */
		public void onClickOKPop();
	}

	/**设置监听*/
	public void setOnItemClickListener(OnItemClickListener listener){
		this.listener=listener;
	}
	
	
	// 当popWindow消失时响应
	@Override
	public void onDismiss() {
		
	}
	
	/**弹窗显示的位置*/  
	public void showAsDropDown(View parent){
		popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
	}
	
	/**消除弹窗*/
	public void dissmiss(){
		popupWindow.dismiss();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_choice_16g:
			
			pop_choice_16g.setBackgroundResource(R.drawable.yuanjiao_choice);
			pop_choice_32g.setBackgroundResource(R.drawable.yuanjiao);
			pop_choice_16m.setBackgroundResource(R.drawable.yuanjiao);
			pop_choice_32m.setBackgroundResource(R.drawable.yuanjiao);
			str_type=pop_choice_16g.getText().toString();
			break;
		case R.id.pop_choice_32g:
			pop_choice_16g.setBackgroundResource(R.drawable.yuanjiao);
			pop_choice_32g.setBackgroundResource(R.drawable.yuanjiao_choice);
			pop_choice_16m.setBackgroundResource(R.drawable.yuanjiao);
			pop_choice_32m.setBackgroundResource(R.drawable.yuanjiao);
			
			str_type=pop_choice_32g.getText().toString();
			break;
		case R.id.pop_choice_16m:
			
			pop_choice_16g.setBackgroundResource(R.drawable.yuanjiao);
			pop_choice_32g.setBackgroundResource(R.drawable.yuanjiao);
			pop_choice_16m.setBackgroundResource(R.drawable.yuanjiao_choice);
			pop_choice_32m.setBackgroundResource(R.drawable.yuanjiao);
			str_type=pop_choice_16m.getText().toString();
			break;
		case R.id.pop_choice_32m:
			
			pop_choice_16g.setBackgroundResource(R.drawable.yuanjiao);
			pop_choice_32g.setBackgroundResource(R.drawable.yuanjiao);
			pop_choice_16m.setBackgroundResource(R.drawable.yuanjiao);
			pop_choice_32m.setBackgroundResource(R.drawable.yuanjiao_choice);
			
			str_type=pop_choice_32m.getText().toString();
			
			break;
		case R.id.pop_choice_black:
			
			pop_choice_black.setBackgroundResource(R.drawable.yuanjiao_choice);
			pop_choice_white.setBackgroundResource(R.drawable.yuanjiao);
			
			str_color=pop_choice_black.getText().toString();
			break;
		case R.id.pop_choice_white:
			
			pop_choice_black.setBackgroundResource(R.drawable.yuanjiao);
			pop_choice_white.setBackgroundResource(R.drawable.yuanjiao_choice);
			
			str_color=pop_choice_white.getText().toString();
			break;
		case R.id.pop_add:
			if (!pop_num.getText().toString().equals(pop_amount.getText().toString())) {
				
				String num_add= Integer.valueOf(pop_num.getText().toString())+ADDORREDUCE+"";
				pop_num.setText(num_add);
			}else {
				Toast.makeText(context, "不能超过最大产品数量", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.pop_reduce:
			if (!pop_num.getText().toString().equals("1")) {
				String num_reduce= Integer.valueOf(pop_num.getText().toString())-ADDORREDUCE+"";
				pop_num.setText(num_reduce);
			}else {
				Toast.makeText(context, "购买数量不能低于1件", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.pop_del:
			listener.onClickOKPop();
			dissmiss();
			
			break;
		case R.id.pop_ok:
			listener.onClickOKPop();
			if (str_color.equals("")) {
				Toast.makeText(context, "亲，你还没有选择颜色哟~", Toast.LENGTH_SHORT).show();
			}else if (str_type.equals("")) {
				Toast.makeText(context, "亲，你还没有选择类型哟~", Toast.LENGTH_SHORT).show();
			}else {
				SharedPreferences sp1 = context.getSharedPreferences("info", context.MODE_PRIVATE);
				String pic = sp1.getString("g_pic", "");
				String name = sp1.getString("g_name", "");
				String g_id = String.valueOf(sp1.getInt("g_id", -1));
				String u_name = sp1.getString("u_name","");
				if(u_name.equals(""))
					Toast.makeText(context,"请先登录",Toast.LENGTH_SHORT).show();
				else {
					HashMap<String, Object> allHashMap = new HashMap<String, Object>();

					allHashMap.put("color", str_color);
					allHashMap.put("type", str_type);
					allHashMap.put("num", pop_num.getText().toString());
					allHashMap.put("id", Data.arrayList_cart_id += 1);
					allHashMap.put("price", pop_price.getText().toString());
					allHashMap.put("g_id", g_id);
					allHashMap.put("pic", pic);
					allHashMap.put("name", name);
					allHashMap.put("u_name", u_name);

					Data.arrayList_cart.add(allHashMap);
					setSaveData();
					dissmiss();
					Toast.makeText(context, "添加到购物车成功", Toast.LENGTH_SHORT).show();
				}
			}
			break;

		default:
			break;
		}
	}
	/**保存购物车的数据*/
	private void setSaveData(){
		SharedPreferences sp=context.getSharedPreferences("SAVE_CART", Context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.clear();
		editor.putInt("ArrayCart_size", Data.arrayList_cart.size());
		for (int i = 0; i < Data.arrayList_cart.size(); i++) {

			editor.putString("ArrayCart_type_" + i, Data.arrayList_cart.get(i).get("type").toString());
			editor.putString("ArrayCart_color_"+i, Data.arrayList_cart.get(i).get("color").toString());
			editor.putString("ArrayCart_num_" + i, Data.arrayList_cart.get(i).get("num").toString());
			editor.putString("ArrayCart_name_"+i, Data.arrayList_cart.get(i).get("name").toString());
			editor.putString("ArrayCart_price_"+i, Data.arrayList_cart.get(i).get("price").toString());
			editor.putString("ArrayCart_pic_"+i, Data.arrayList_cart.get(i).get("pic").toString());
			editor.putString("ArrayCart_g_id_" + i, Data.arrayList_cart.get(i).get("g_id").toString());
			editor.putString("ArrayCart_u_name_" + i, Data.arrayList_cart.get(i).get("u_name").toString());
			
		}
		
		
	}
	
}
