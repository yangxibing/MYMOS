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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class NewsType extends Activity {

	private ListView mNewsTypeListView;
	private Button mAddNewsType;
	private Button mTurnToNewsList;
	private RequestAndResponse mRar;
	private Handler mHandler;
	private List<String> newsTypeNamelist;
	private List<String> newsTypeIdList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_type);

		mNewsTypeListView = (ListView) findViewById(R.id.lv_NewsType);
		mAddNewsType = (Button) findViewById(R.id.btn_AddNewsType);
		mTurnToNewsList = (Button) findViewById(R.id.btn_turnToNews);
		newsTypeIdList = new ArrayList<String>();
		newsTypeNamelist = new ArrayList<String>();
		mRar = new RequestAndResponse();

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				//加载新闻类型列表
				case 0x801: {
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
							newsTypeNamelist.add(newsTypeName);
							newsTypeIdList.add(newsTypeId);
						}

						// 将list包装为ArrayAdapter
						ArrayAdapter<String> newsTypeAdapter = new ArrayAdapter<String>(
								NewsType.this, R.layout.listview_item_style1,
								newsTypeNamelist);
						mNewsTypeListView.setAdapter(newsTypeAdapter);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					break;

				default:
					break;
				}
			}

		};

		loadNewsTye();
		// 新增新闻类型
		mAddNewsType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent turnToNewsIntent = new Intent(NewsType.this,
						AddNewsType.class);
				startActivity(turnToNewsIntent);
			}
		});

		// 跳转到新闻页面
		mTurnToNewsList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent turnToNewsIntent = new Intent(NewsType.this,NewsMainActivity.class);
				NewsType.this.finish();
				//startActivity(turnToNewsIntent);
			}
		});
	}

	private void loadNewsTye() {

		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					// 访问新闻类型页面
					String resultNewsType = mRar.access("viewNewsType", "");
					Message msg = new Message();
					msg.what = 0x801;
					msg.obj = resultNewsType;
					mHandler.sendMessage(msg);

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
}
