package com.mymos.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mymos.Entity.User;
import com.mymos.Util.RequestAndResponse;
import com.mymos.Util.UserDao;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AnnouncementActivity extends Activity {
	private RequestAndResponse mRar;
	private Handler mHanler;
	private ListView mList;
	private List<Map<String,Object>> mListItems;
	private List<String> mAnnIdList;
	private Resources r;
	
	private int mPriority;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.announcement_manage);
		
		UserDao userDao = new UserDao(this);
		List<User> list = userDao.queryAll();
		if(!list.toString().equals("[]")){
			mPriority = list.get(0).getPriority();
		}
		
		if(mPriority == 1){
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.issue_announcement);
			rl.setVisibility(View.GONE);
		}
		
		mRar = new RequestAndResponse();
		mHanler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what == 0x111){
					String annoList = msg.obj.toString();
					try {
						JSONArray jsonArray = new JSONArray(annoList);
						
						Log.i("annoList",jsonArray.toString());
						
						mListItems = new ArrayList<Map<String,Object>>();
						mAnnIdList = new ArrayList<String>();
						for(int i =0; i < jsonArray.length();i++){
							JSONObject json = jsonArray.getJSONObject(i);
							
							System.out.println(json.toString());
							
							mAnnIdList.add(json.getString("id"));
							
							Map<String,Object> list = new HashMap<String, Object>();
							list.put("authorName", "发布者:" + json.getString("authorName"));
							list.put("title", json.getString("title"));
							list.put("activeDate", "发布日期:" + json.getString("activeDate"));
							list.put("content", json.getString("content"));
							list.put("visterNumber", "浏览次数:" + json.getString("readCount")+"次");
							mListItems.add(list);
						}
						//RelativeLayout r =(RelativeLayout) getLayoutInflater().inflate(R.layout.announcemnet_list_item, null);
						SimpleAdapter simpleAdapter = new SimpleAdapter(AnnouncementActivity.this, mListItems,
							R.layout.announcemnet_list_item, 
							new String[] { "authorName", "title" , "activeDate", "content","visterNumber"},
							new int[] { R.id.announcementAuthor, R.id.announcementTitle , R.id.announcementDate, R.id.announcementcontent,R.id.visterNumber });
		                
						mList = (ListView) findViewById(R.id.announcementList);
						mList.setAdapter(simpleAdapter);
						
						mList.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								//传递公告信息以及id
								JSONObject jsonObject = new JSONObject(mListItems.get(position));
								String annInfor = jsonObject.toString();
								Intent intent = new Intent(AnnouncementActivity.this,AnnouncementDetail.class);
								intent.putExtra("annInfor", annInfor);
								String annId = mAnnIdList.get(position);
								intent.putExtra("annId", annId);
								startActivity(intent);
							}
						});
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		};
		
		viewAnnouncement();
			
	}

	public void viewAnnouncement(){
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String announcementList = mRar.access("viewAnnouncementList", "");
					Message msg = new Message();
					msg.what = 0x111;
					msg.obj = announcementList;
					mHanler.sendMessage(msg);
					Log.i("announcemntList",announcementList);
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
		viewAnnouncement();
		super.onRestart();
	}

	public void issue_announcement(View v) {
		Intent intent = new Intent();
		intent.setClass(this, DraftAnnouncement.class);
		startActivity(intent);
	}			
   
	

}
