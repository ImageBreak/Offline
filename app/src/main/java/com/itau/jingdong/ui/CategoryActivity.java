package com.itau.jingdong.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itau.jingdong.R;
import com.itau.jingdong.ui.base.BaseActivity;


public class CategoryActivity extends BaseActivity {

	private ListView catergory_listview;
	private LayoutInflater layoutInflater;
	private Intent mIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_category);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		catergory_listview=(ListView)this.findViewById(R.id.catergory_listview);

		catergory_listview.setAdapter(new CatergorAdapter());
		catergory_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int parent,
					long id) {
				//Toast.makeText(CategoryActivity.this, "你点击了第"+id+"项",1 ).show();
				//Intent mIntent = new Intent(CategoryActivity.this, WareActivity.class);
				//startActivity(mIntent);
				switch ((int)id){
					case 0:
						mIntent = new Intent(CategoryActivity.this, IndexDaily.class);
						startActivity(mIntent);
						break;
					case 1:
						mIntent = new Intent(CategoryActivity.this, IndexEbookActivity.class);
						startActivity(mIntent);
						break;
					case 2:
						mIntent = new Intent(CategoryActivity.this, IndexClothingActivity.class);
						startActivity(mIntent);
						break;
					case 3:
						mIntent = new Intent(CategoryActivity.this, IndexDigtalActivity.class);
						startActivity(mIntent);
						break;
					default:
						break;
				}
			}
		});
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}
	
	private class CatergorAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mImageIds.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressWarnings("null")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder=new ViewHolder();
			layoutInflater=LayoutInflater.from(CategoryActivity.this);
			
			//组装数据
			if(convertView==null){
				convertView=layoutInflater.inflate(R.layout.activity_category_item, null);
				holder.image=(ImageView) convertView.findViewById(R.id.catergory_image);
				holder.title=(TextView) convertView.findViewById(R.id.catergoryitem_title);
				holder.content=(TextView) convertView.findViewById(R.id.catergoryitem_content);
				//使用tag存储数据
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.image.setImageResource(mImageIds[position]);
			holder.title.setText(mTitleValues[position]);
			holder.content.setText(mContentValues[position]);
		//	holder.title.setText(array[position]);
			return convertView;
		}
	}
	// 适配显示的图片数组
				private Integer[] mImageIds = {R.drawable.catergory_appliance,R.drawable.catergory_book,R.drawable.catergory_deskbook,
						R.drawable.catergory_digtcamer
						 };
				//给照片添加文字显示(Title)
				private String[] mTitleValues = { "家电", "图书", "衣服", "数码"};
				
				private String[] mContentValues={"家电/生活电器/厨房电器", "电子书/图书/小说","男装/女装/童装", "摄影摄像/数码配件",};
			

		 public static class ViewHolder {
				ImageView image;
				TextView title;
				TextView content;
			}
	
	

}
