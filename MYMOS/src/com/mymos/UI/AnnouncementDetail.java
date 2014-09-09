package com.mymos.UI;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.mymos.Entity.User;
import com.mymos.Util.RequestAndResponse;
import com.mymos.Util.UserDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AnnouncementDetail extends Activity {
	private RequestAndResponse mRar;
	private Handler mHandler;
	
	private JSONObject mJson;
	private int mPriority = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.announcement_detail);
		
		UserDao userDao = new UserDao(this);
		List<User> list = userDao.queryAll();
		if(!list.toString().equals("[]")){
			mPriority = list.get(0).getPriority();
		}
		
		if(mPriority == 1){
			Button btn1 = (Button) findViewById(R.id.button1);
			Button btn2 = (Button) findViewById(R.id.button2);
			btn1.setVisibility(View.GONE);
			btn2.setVisibility(View.GONE);
		}
		TextView textview = (TextView)findViewById(R.id.content);
		textview.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		Intent intent = this.getIntent();
		String annInfor = intent.getStringExtra("annInfor");
		try {
			mJson = new JSONObject(annInfor);
			String annTitle = mJson.getString("title");
			String authorName = mJson.getString("authorName");
			String activeDate = mJson.getString("activeDate");
			String content = mJson.getString("content");
			String readCount = mJson.getString("visterNumber");
			
			TextView tvTitle = (TextView) findViewById(R.id.title);
			TextView tvAuthorName = (TextView) findViewById(R.id.authorName);
			TextView tvReadCount = (TextView) findViewById(R.id.readCount);
			TextView tvContent = (TextView) findViewById(R.id.content);
			TextView tvDate = (TextView) findViewById(R.id.issue_date);
			
			tvTitle.setText(annTitle);
			tvAuthorName.setText(authorName);
			tvReadCount.setText(readCount);
			tvContent.setText(content);
			tvDate.setText(activeDate);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		mRar = new RequestAndResponse();
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 0x111:
					Toast.makeText(AnnouncementDetail.this, "该公告已终止", 2000).show();
					break;
				case 0x112:
					Toast.makeText(AnnouncementDetail.this, "该公告已删除", 2000).show();
					break;
				default:
					break;
				}
				
			}
			
		};
		
		final String annId = intent.getStringExtra("annId");
		
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mRar.access("addReadCount", annId);
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
	
	public void stopAnnouncement(View v){
		Intent intent = this.getIntent();
		final String id = intent.getStringExtra("annId");
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mRar.access("stopAnnouncement", id);
					Message msg = new Message();
					msg.what = 0x111;
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
	
	public void deleteAnnouncement(View v){
		Intent intent = this.getIntent();
		final String id = intent.getStringExtra("annId");
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mRar.access("deleteAnnouncement", id);
					Message msg = new Message();
					msg.what = 0x112;
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
