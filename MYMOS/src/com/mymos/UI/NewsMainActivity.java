package com.mymos.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mymos.Util.RequestAndResponse;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

public class NewsMainActivity extends Activity {

	@SuppressWarnings("deprecation")
	private LocalActivityManager manager = null;
	private ViewPager pager = null;
	// private TabHost tabHost =null;

	private int offset = 0; // 动画图片偏移量
	private int currIndex = 0; // 当前页卡编号 
	private int bmpw; // 动画页面宽度
	private ImageView cursor; // 动画图片
	private List<String> list;
	private List<String> idList;  //储存类型的id
	private GridView gridView;
	private RequestAndResponse mRar;
	private Handler mHander;
	private Button mBtn_addNews;
	private Button mBtn_TurnToNewsType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_title);
		cursor = (ImageView) findViewById(R.id.img_Slider);
		list = new ArrayList<String>();
		idList = new ArrayList<String>();
		gridView = (GridView) findViewById(R.id.gridView);
		/* 初始化PageViewer */
		pager = (ViewPager) findViewById(R.id.vp);
		mBtn_addNews = (Button) findViewById(R.id.btn_AddNews);
		mBtn_TurnToNewsType =(Button) findViewById(R.id.btn_turnToType);
		
		/*添加新闻*/
		mBtn_addNews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent addNewsIntent = new Intent(NewsMainActivity.this,
						AddNewsActivity.class);
				//向AddNewsActivity传递所有新闻类型
				addNewsIntent.putStringArrayListExtra("AllNewsTypeId", (ArrayList<String>) idList);
				addNewsIntent.putStringArrayListExtra("AllNewsTypeName", (ArrayList<String>) list);
				startActivity(addNewsIntent);
			}
		});
		
       /*跳转到新闻类型*/
		mBtn_TurnToNewsType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent turnToNewsTypeIntent=new Intent(NewsMainActivity.this, NewsType.class);
				// NewsMainActivity.this.finish();
				 startActivity(turnToNewsTypeIntent);
			}
		});
		
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);

		mRar = new RequestAndResponse();
		
		try {
			initialTitle(gridView); // 初始化新闻类型
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*处理服务器访问*/
		mHander = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x123) {
					String newsType = msg.obj.toString();
                   
					Log.i("handler newsTypeName", newsType);
					try {
						JSONArray json = new JSONArray(newsType);

						// 通过查询数据库，将新闻载入list
 
						for (int i = 0; i < json.length(); i++) {
							JSONObject jsonObject = json.getJSONObject(i);
							String newsTypeName = jsonObject
									.getString("newsTypeName");

							Log.i("newsTypeName", newsTypeName);

							String newsTypeId = jsonObject.getString("id");
							list.add(newsTypeName);
							idList.add(newsTypeId);
							
						}

						// 将list包装为ArrayAdapter
			 			ArrayAdapter<String> contenAdapter = new ArrayAdapter<String>(
								NewsMainActivity.this,
								R.layout.listview_item_style1, list);
						
						gridView.setAdapter(contenAdapter);
						 
						final ArrayList<View> list = new ArrayList<View>();
												
						Log.i("gridview count",gridView.getCount()+"");
						for (int i = 0; i < gridView.getCount(); i++) {
                               //传递参数
							Intent intent = new Intent(NewsMainActivity.this,NewsViewPager.class);

							intent.putExtra("selectNewsType", gridView.getItemAtPosition(i).toString());
							
							list.add(getView(String.valueOf(i), intent));
							Log.i("for循环", gridView.getItemAtPosition(i).toString());
						}
						
						pager.setAdapter(new MyPagerAdapter(list));
						pager.setCurrentItem(0); 
						// 加载新闻
						pager.setOnPageChangeListener(new MyOnPageChangeListener());
						
						// 点击标题跳转
						gridView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
								// TODO Auto-generated method stub
								pager.setCurrentItem(position);
							}
						});
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		initial();
	}

	/**
	 * 初始化分页
	 */
	private void initial() {
		/* 初始化ImageView */
		
		bmpw = BitmapFactory.decodeResource(getResources(), R.drawable.curse)
				.getWidth(); // 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screeW = dm.widthPixels; // 获取分辨率宽度
		offset = (screeW / 6 - bmpw) / 2; // 计算偏移量

		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix); // 设置动画初始位置
		
		//首次加载时载入第0项item所对应的新闻
//		intent.putExtra("selectNewsType", gridView
//				.getItemAtPosition(0).toString());
//		startActivity(intent);
	}

	/**
	 * 通过Activity获取视图
	 * 
	 * @param id
	 * @param intent
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	public class MyPagerAdapter extends PagerAdapter {
		List<View> list = new ArrayList<View>();

		public MyPagerAdapter(ArrayList<View> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(list.get(arg1));
			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
			// TODO Auto-generated method stub
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View container) {
			// TODO Auto-generated method stub
		}
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		int one = offset * 2 + bmpw; // 页卡1 -〉页卡2偏移量
		int two = 2 * one;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Animation animation = null;
			animation = new TranslateAnimation(currIndex * one, arg0 * one, 0,
					0);

			currIndex = arg0;

			/* 向显示页面传递参数gridView.getItemAtPosition(arg0).toString()， */
			Log.i("滑动", gridView.getItemAtPosition(arg0).toString());

			animation.setFillAfter(true);
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * 初始化导航栏gridview
	 * 
	 * @throws JSONException
	 */
	private void initialTitle(GridView g) throws JSONException {

		JSONObject json = new JSONObject();
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String mResult = "";
				try {
					mResult = mRar.access("viewNewsType", "");

					Log.i("newsTypeInfor", mResult);
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Message mMsg = new Message();
				mMsg.what = 0x123;
				mMsg.obj = mResult;
				mHander.sendMessage(mMsg);
			}
		}.start();
	}
}
