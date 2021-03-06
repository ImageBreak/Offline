package com.itau.jingdong.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itau.jingdong.R;
import com.itau.jingdong.adapter.IndexGalleryAdapter;
import com.itau.jingdong.entity.IndexGalleryItemData;
import com.itau.jingdong.ui.base.BaseActivity;
import com.itau.jingdong.utils.CommonTools;
import com.itau.jingdong.widgets.HomeSearchBarPopupWindow;
import com.itau.jingdong.widgets.HomeSearchBarPopupWindow.onSearchBarItemClickListener;
import com.itau.jingdong.widgets.jazzviewpager.JazzyViewPager;
import com.itau.jingdong.widgets.jazzviewpager.JazzyViewPager.TransitionEffect;
import com.itau.jingdong.widgets.jazzviewpager.OutlineContainer;
import com.itau.jingdong.zxing.CaptureActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

public class IndexActivity extends BaseActivity implements OnClickListener,
		onSearchBarItemClickListener {
	public static final String TAG = IndexActivity.class.getSimpleName();

	//=============中部导航栏模块=====
	private ImageButton shake;
	private Intent mIntent;
	private Uri uri;
	
	// ============== 广告切换 ===================
	private JazzyViewPager mViewPager = null;
	/**
	 * 装指引的ImageView数组
	 */
	private ImageView[] mIndicators;

	/**
	 * 装ViewPager中ImageView的数组
	 */
	private ImageView[] mImageViews;
	private List<String> mImageUrls = new ArrayList<String>();
	private LinearLayout mIndicator = null;
	private String mImageUrl = null;
	private static final int MSG_CHANGE_PHOTO = 1;
	/** 图片自动切换时间 */
	private static final int PHOTO_CHANGE_TIME = 3000;
	// ============== 广告切换 ===================

	private Gallery mStormGallery = null;
	private Gallery mPromotionGallery = null;
	private IndexGalleryAdapter mStormAdapter = null;
	private IndexGalleryAdapter mPromotionAdapter = null;
	private List<IndexGalleryItemData> mStormListData = new ArrayList<IndexGalleryItemData>();
	private List<IndexGalleryItemData> mPromotionListData = new ArrayList<IndexGalleryItemData>();
	private IndexGalleryItemData mItemData = null;
	private HomeSearchBarPopupWindow mBarPopupWindow = null;
	private EditText mSearchBox = null;
	private ImageButton mCamerButton = null;
	private ImageButton tuijian,phoneFee,tuangou,caipiao,huoche,history,favourite;
	private LinearLayout mTopLayout = null;

	private FrameLayout code;
	private ImageView code_pic;
	private static int QR_WIDTH = 400,QR_HEIGHT = 400;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);

		mHandler = new Handler(getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case MSG_CHANGE_PHOTO:
					int index = mViewPager.getCurrentItem();
					if (index == mImageUrls.size() - 1) {
						index = -1;
					}
					mViewPager.setCurrentItem(index + 1);
					mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);
				}
			}
		};
		initData();
		findViewById();
		initView();

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

		mViewPager = (JazzyViewPager) findViewById(R.id.index_product_images_container);
		mIndicator = (LinearLayout) findViewById(R.id.index_product_images_indicator);

		mStormGallery = (Gallery) findViewById(R.id.index_jingqiu_gallery);
		mPromotionGallery = (Gallery) findViewById(R.id.index_tehui_gallery);

		mSearchBox = (EditText) findViewById(R.id.index_search_edit);
		mCamerButton = (ImageButton) findViewById(R.id.index_camer_button);
		mTopLayout = (LinearLayout) findViewById(R.id.index_top_layout);
		
		shake=(ImageButton)findViewById(R.id.index_shake);
		tuijian=(ImageButton)findViewById(R.id.index_promotion_btn);
		phoneFee=(ImageButton)findViewById(R.id.index_recharge_btn);
		tuangou=(ImageButton)findViewById(R.id.index_groupbuy_btn);
		caipiao=(ImageButton)findViewById(R.id.index_lottery_btn);
		huoche=(ImageButton)findViewById(R.id.index_order_btn);
		history=(ImageButton)findViewById(R.id.index_history_btn);
		favourite=(ImageButton)findViewById(R.id.index_collect_btn);

		code = (FrameLayout) findViewById(R.id.code);
		code_pic = (ImageView) findViewById(R.id.code_pic);
		//添加事件
		shake.setOnClickListener(indexClickListener);
		tuijian.setOnClickListener(indexClickListener);
		phoneFee.setOnClickListener(indexClickListener);
		tuangou.setOnClickListener(indexClickListener);
		caipiao.setOnClickListener(indexClickListener);
		huoche.setOnClickListener(indexClickListener);
		history.setOnClickListener(indexClickListener);
		favourite.setOnClickListener(indexClickListener);
	}

	
	private OnClickListener indexClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
			String u_name = sp.getString("u_name","");
			switch (v.getId()) {
				case R.id.index_promotion_btn:
					if(u_name.equals(""))
						Toast.makeText(IndexActivity.this, "尚未登陆", Toast.LENGTH_SHORT).show();
					else {
						mIntent = new Intent(IndexActivity.this, PersonalRecommend.class);
						startActivity(mIntent);
					}
					break;

				case R.id.index_recharge_btn:
					uri=Uri.parse("https://h5.m.taobao.com/app/cz/cost.html");
					mIntent=new Intent(Intent.ACTION_VIEW, uri);
					startActivity(mIntent);
					break;

				case R.id.index_groupbuy_btn:
					uri=Uri.parse("http://i.meituan.com");
					mIntent=new Intent(Intent.ACTION_VIEW, uri);
					startActivity(mIntent);
					break;

				case R.id.index_lottery_btn:
					uri=Uri.parse("https://caipiao.taobao.com");
					mIntent=new Intent(Intent.ACTION_VIEW, uri);
					startActivity(mIntent);
					break;

				case R.id.index_order_btn:
					uri=Uri.parse("https://h5.m.taobao.com/app/triphome/pages/home/index.html");
					mIntent=new Intent(Intent.ACTION_VIEW, uri);
					startActivity(mIntent);
					break;

				case R.id.index_history_btn:
					if(u_name.equals(""))
						Toast.makeText(IndexActivity.this, "尚未登陆", Toast.LENGTH_SHORT).show();
					else {
						mIntent = new Intent(IndexActivity.this, PersonalHistory.class);
						startActivity(mIntent);
					}
					break;

				case R.id.index_shake:
					mIntent=new Intent(IndexActivity.this, IndexShakeActivity.class);
					startActivity(mIntent);
					break;

				case R.id.index_collect_btn:
					if(u_name.equals(""))
						Toast.makeText(IndexActivity.this, "尚未登陆", Toast.LENGTH_SHORT).show();
					else {
						mIntent = new Intent(IndexActivity.this, PersonalFavouriteActivity.class);
						startActivity(mIntent);
					}
					break;

				default:
					break;
			}
			
		}
	};
	
	@Override
	protected void initView() {

		// ======= 初始化ViewPager ========
		mIndicators = new ImageView[mImageUrls.size()];
		if (mImageUrls.size() <= 1) {
			mIndicator.setVisibility(View.GONE);//设置控件隐藏
		}

		for (int i = 0; i < mIndicators.length; i++) {
			ImageView imageView = new ImageView(this);
			LayoutParams params = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f);
			if (i != 0) {
				params.leftMargin = 5;
			}
			imageView.setLayoutParams(params);
			mIndicators[i] = imageView;
			if (i == 0) {
				mIndicators[i]
						.setBackgroundResource(R.drawable.android_activities_cur);
			} else {
				mIndicators[i]
						.setBackgroundResource(R.drawable.android_activities_bg);
			}

			mIndicator.addView(imageView);
		}
		mImageViews = new ImageView[mImageUrls.size()];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			mImageViews[i] = imageView;
		}
		//以上为滑动广告指示的代码


		mViewPager.setTransitionEffect(TransitionEffect.Tablet);
		mViewPager.setCurrentItem(0);
		mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);//向主线程发送消息，完成图片切换

		mViewPager.setAdapter(new MyAdapter());
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (mImageUrls.size() == 0 || mImageUrls.size() == 1)
					return true;
				else
					return false;
			}
		});
		
		// ======= 初始化ViewPager ========

		mStormAdapter = new IndexGalleryAdapter(this,
				R.layout.activity_index_gallery_item, mStormListData,
				new int[] { R.id.index_gallery_item_image,
						R.id.index_gallery_item_text });

		mStormGallery.setAdapter(mStormAdapter);

		mPromotionAdapter = new IndexGalleryAdapter(this,
				R.layout.activity_index_gallery_item, mPromotionListData,
				new int[] { R.id.index_gallery_item_image,
						R.id.index_gallery_item_text });

		mPromotionGallery.setAdapter(mPromotionAdapter);

		mStormGallery.setSelection(3);
		mPromotionGallery.setSelection(3);

		mBarPopupWindow = new HomeSearchBarPopupWindow(this,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mBarPopupWindow.setOnSearchBarItemClickListener(this);

		mCamerButton.setOnClickListener(this);
		mSearchBox.setOnClickListener(this);

		mSearchBox.setInputType(InputType.TYPE_NULL);
	}

	private void initData() {
		mImageUrl = "drawable://" + R.drawable.image01;
		mImageUrls.add(mImageUrl);

		mImageUrl = "drawable://" + R.drawable.image02;
		mImageUrls.add(mImageUrl);

		mImageUrl = "drawable://" + R.drawable.image03;
		mImageUrls.add(mImageUrl);

		mImageUrl = "drawable://" + R.drawable.image04;
		mImageUrls.add(mImageUrl);

		mImageUrl = "drawable://" + R.drawable.image05;
		mImageUrls.add(mImageUrl);

		mImageUrl = "drawable://" + R.drawable.image06;
		mImageUrls.add(mImageUrl);

		mImageUrl = "drawable://" + R.drawable.image07;
		mImageUrls.add(mImageUrl);

		mImageUrl = "drawable://" + R.drawable.image08;
		mImageUrls.add(mImageUrl);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(1);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_01);
		mItemData.setPrice("￥79.00");
		mStormListData.add(mItemData);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(2);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_02);
		mItemData.setPrice("￥89.00");
		mStormListData.add(mItemData);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(3);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_03);
		mItemData.setPrice("￥99.00");
		mStormListData.add(mItemData);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(4);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_04);
		mItemData.setPrice("￥109.00");
		mStormListData.add(mItemData);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(5);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_05);
		mItemData.setPrice("￥119.00");
		mStormListData.add(mItemData);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(6);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_06);
		mItemData.setPrice("￥129.00");
		mStormListData.add(mItemData);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(7);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_07);
		mItemData.setPrice("￥139.00");
		mStormListData.add(mItemData);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(8);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_08);
		mItemData.setPrice("￥69.00");
		mPromotionListData.add(mItemData);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(9);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_09);
		mItemData.setPrice("￥99.00");
		mPromotionListData.add(mItemData);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(10);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_10);
		mItemData.setPrice("￥109.00");
		mPromotionListData.add(mItemData);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(11);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_11);
		mItemData.setPrice("￥119.00");
		mPromotionListData.add(mItemData);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(12);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_12);
		mItemData.setPrice("￥129.00");
		mPromotionListData.add(mItemData);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(13);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_13);
		mItemData.setPrice("￥139.00");
		mPromotionListData.add(mItemData);

		mItemData = new IndexGalleryItemData();
		mItemData.setId(14);
		mItemData.setImageUrl("drawable://" + R.drawable.index_gallery_14);
		mItemData.setPrice("￥149.00");
		mPromotionListData.add(mItemData);
	}

	
	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageViews.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mViewPager
					.findViewFromObject(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			ImageLoader.getInstance().displayImage(mImageUrls.get(position),
					mImageViews[position]);
			((ViewPager) container).addView(mImageViews[position], 0);
			mViewPager.setObjectForPosition(mImageViews[position], position);
			return mImageViews[position];
		}

	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			setImageBackground(position);
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 设置选中的tip的背景
	 */
	private void setImageBackground(int selectItemsIndex) {
		for (int i = 0; i < mIndicators.length; i++) {
			if (i == selectItemsIndex) {
				mIndicators[i]
						.setBackgroundResource(R.drawable.android_activities_cur);
			} else {
				mIndicators[i]
						.setBackgroundResource(R.drawable.android_activities_bg);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.index_camer_button:
			int height = mTopLayout.getHeight()
					+ CommonTools.getStatusBarHeight(this);
			mBarPopupWindow.showAtLocation(mTopLayout, Gravity.TOP, 0, height);
			break;

		case R.id.index_search_edit:
			openActivity(SearchActivity.class);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBarCodeButtonClick() {
		// TODO Auto-generated method stub
		mIntent=new Intent(IndexActivity.this, CaptureActivity.class);
		startActivity(mIntent);
	}

	@Override
	public void onColorButtonClick() {
		// TODO Auto-generated method stub
		//CommonTools.showShortToast(this, "二维码");
		if(!code.isShown()) {
			code.setVisibility(View.VISIBLE);
			createQRImage("addtrade.action");
		}
		else if(code.isShown())
			code.setVisibility(View.GONE);
	}

	public void createQRImage(String url)
	{
		try
		{
			//判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1)
			{
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			//图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			//下面这里按照二维码的算法，逐个生成二维码的图片，
			//两个for循环是图片横列扫描的结果
			for (int y = 0; y < QR_HEIGHT; y++)
			{
				for (int x = 0; x < QR_WIDTH; x++)
				{
					if (bitMatrix.get(x, y))
					{
						pixels[y * QR_WIDTH + x] = 0xff000000;
					}
					else
					{
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			//生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			//显示到一个ImageView上面
			code_pic.setImageBitmap(bitmap);
		}
		catch (WriterException e)
		{
			e.printStackTrace();
		}
	}
}
