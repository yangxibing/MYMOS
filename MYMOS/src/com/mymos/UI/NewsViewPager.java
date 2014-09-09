package com.mymos.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mymos.Entity.News;
import com.mymos.Util.Bitmap2byteTransfer;
import com.mymos.Util.RequestAndResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NewsViewPager extends Activity {

	private ListView listview;
	public List<String> list;
	public List<News> mNewsList;
	private RequestAndResponse mRar;
	private Handler mHandler;
	private ArrayAdapter<String> contenAdapter;
	private String selectNewsType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_content);
		mNewsList = new ArrayList<News>();
		listview = (ListView) findViewById(R.id.lv2);
		mRar = new RequestAndResponse();
		list = new ArrayList<String>();

		Intent intent = this.getIntent();
		selectNewsType = intent.getStringExtra("selectNewsType");
		Log.i("--selectNewsType", selectNewsType);

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				/* 加载新闻 */
				if (msg.what == 0x111) {
					String newsMsg = msg.obj.toString();

					try {
						JSONArray json = new JSONArray(newsMsg);
						Log.i("news--------------", newsMsg);
						for (int i = 0; i < json.length(); i++) {
							JSONObject jsonObj = json.getJSONObject(i);
							String newsTitle = jsonObj.getString("title");

							Log.i("news  title", newsTitle);
							list.add(newsTitle); // 添加到listview

							String newsId = jsonObj.getString("id");
							String newsContent = jsonObj.getString("content");
							String newsImg = jsonObj.getString("imgUrl");
							Boolean newsIsTop = jsonObj.getBoolean("isTop");
							Boolean newsIsComment = jsonObj
									.getBoolean("isComment");
                            
							News news = new News();
							news.setNewsId(newsId);
							news.setTitle(newsTitle);
							news.setContent(newsContent);
							news.setImgUrl(newsImg);
							news.setIsComment(newsIsComment);
							news.setIsTop(newsIsTop);

							mNewsList.add(news);
						}
						// // 显示新增的新闻
						// News n = new News();
						//
						// n.setNewsId(NewsViewPager.this.getIntent().getStringArrayExtra("AddNewsSuccess")[0]);
						// n.setTitle(NewsViewPager.this.getIntent().getStringArrayExtra("AddNewsSuccess")[1]);
						// n.setContent(NewsViewPager.this.getIntent().getStringArrayExtra("AddNewsSuccess")[2]);
						// n.setImgUrl(NewsViewPager.this.getIntent().getStringArrayExtra("AddNewsSuccess")[0]);
						// n.setIsTop(Boolean.parseBoolean(NewsViewPager.this.getIntent().getStringArrayExtra(
						// "AddNewsSuccess")[4]));
						// n.setIsComment(Boolean.parseBoolean(NewsViewPager.this.getIntent().getStringArrayExtra(
						// "AddNewsSuccess")[5]));
						// n.setId(NewsViewPager.this.getIntent().getStringArrayExtra("AddNewsSuccess")[6]);
						//
						// mNewsList.add(n); // 将新增新闻添加进mNewsList
						//
						// list.add(n.getTitle()); // 将新增新闻标题添加进list
						contenAdapter = new ArrayAdapter<String>(
								NewsViewPager.this,
								R.layout.listview_item_style1, list);
						listview.setAdapter(contenAdapter);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				/* 删除 */
				if (msg.what == 0x001) {
					int index = Integer.parseInt(msg.obj.toString());
					// 删除新闻后，加载
					list.remove(index);
					mNewsList.remove(index);
					Log.i("list---size", list.size() + "  " + mNewsList.size());
					Log.i("move---position", "  " + index);

					contenAdapter = new ArrayAdapter<String>(
							NewsViewPager.this, R.layout.listview_item_style1,
							list);
					listview.setAdapter(contenAdapter);

					Toast.makeText(NewsViewPager.this, "删除新闻成功", 2000).show();
				}
			}
		};

		/* 加载新闻列表 */
		loadNewsList();

		/* 长按删除新闻事件 */
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final News n = mNewsList.get(position);
				final int index = position; // 获得所选项index

				// 删除新闻提示框
				AlertDialog.Builder builder = new AlertDialog.Builder(
						NewsViewPager.this).setTitle("删除新闻").setMessage(
						"删除该条新闻？");
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						Map<String, String> map = new HashMap<String, String>();
						map.put("id", n.getNewsId());
						final JSONObject jsonObj = new JSONObject(map);
						new Thread() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									String deleteResult = mRar.access(
											"deleteNews", jsonObj.toString());
									Message msg = new Message();
									msg.what = 0x001;
									if (deleteResult
											.equals("delete news success")) {
										msg.obj = index;
										mHandler.sendMessage(msg);
									}
								} catch (HttpException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}.start();
					}
				});

				builder.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
				builder.create().show();
				return false;
			}
		});

		/* 点击查看新闻事件 */
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				News n = mNewsList.get(position);
				Intent intent = new Intent(NewsViewPager.this,
						NewsDetails.class);
				Log.i("图片-----", n.getImgUrl());
				String newsDetail[] = new String[] { n.getNewsId(),
						n.getTitle(), n.getContent(), n.getImgUrl(),
						n.getIsTop() + "", n.getIsComment() + "" };
				intent.putExtra("newsMsg", newsDetail); // 传数据，cs2相当于变量名，在接受类中可以获得cs2的内容
				startActivity(intent);
			}
		});
	}

	/* 加载新闻列表 */
	private void loadNewsList() {

		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					// 所选择新闻类型
					String newsDetail = mRar.access("viewNewsListByType",
							selectNewsType);
					Message msg = new Message();
					msg.what = 0x111;
					msg.obj = newsDetail;
					mHandler.sendMessage(msg);

					Log.i("newsListByType", newsDetail);
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		loadNewsList();

		Log.i("restart----", "5555555555555555555");
		super.onRestart();
	}

}