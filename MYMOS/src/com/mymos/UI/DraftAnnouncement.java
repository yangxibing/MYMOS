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

import com.mymos.Util.RequestAndResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class DraftAnnouncement extends Activity {
	private RequestAndResponse mRar;
	private Handler mHandler;
	
	private ListView mList;
	private List<Map<String,Object>> mListItems;
	private List<String> mAnnIdList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.draft_announcement);
		
		mRar = new RequestAndResponse();
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 0x111:
					String draftAnnouncement = msg.obj.toString();
					//草稿为空
					if(draftAnnouncement.equals("[]")){
						break;
					}
					try {
						Log.i("draft announcement",draftAnnouncement);
						final JSONArray jsonArray = new JSONArray(draftAnnouncement);
						mListItems = new ArrayList<Map<String,Object>>();
						mAnnIdList = new ArrayList<String>();
						for(int i = 0;i < jsonArray.length();i++){
							JSONObject json = jsonArray.getJSONObject(i);
							Map<String,Object> map = new HashMap<String, Object>();
							map.put("authorName", "作者:"+json.getString("authorName"));
							map.put("title", json.getString("title"));
							map.put("activeDate", "创建日期:" + json.getString("activeDate"));
							map.put("content", json.getString("content"));
							
							mListItems.add(map);
							mAnnIdList.add(json.getString("id"));
						}
						Log.i("mListItems add","success");
						SimpleAdapter adapter = new SimpleAdapter(DraftAnnouncement.this, mListItems, R.layout.announcemnet_list_item,
								new String[] { "authorName", "title" , "activeDate", "content"},
								new int[] { R.id.announcementAuthor, R.id.announcementTitle , R.id.announcementDate, R.id.announcementcontent });
						Log.i("SimpleAdapter ","success");
						mList = (ListView) findViewById(R.id.draft_announcementList);
						mList.setAdapter(adapter);
						
						mList.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								String announcementId = mAnnIdList.get(position);
								JSONObject jsonObject = null;
								try {
									jsonObject = jsonArray.getJSONObject(position);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								Log.i("announcementId", announcementId);
								Log.i("announcementInfor", jsonObject.toString());
								Intent intent = new Intent(DraftAnnouncement.this, NewAnnouncement.class);
								intent.putExtra("announcementId", announcementId);
								intent.putExtra("announcementInfor", jsonObject.toString());
								startActivity(intent);
							}
						});
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					break;
						
				}
			}
			
		};
		viewDraftAnnouncement();

	}
	
	public void viewDraftAnnouncement(){
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String draftAnnouncement = mRar.access("viewDraftAnnouncement", "");
					Message msg = new Message();
					msg.what = 0x111;
					msg.obj = draftAnnouncement;
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
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		viewDraftAnnouncement();
		super.onRestart();
	}
	
	public void add_announcement(View v) {
		Intent intent = new Intent();
		intent.setClass(this, NewAnnouncement.class);
		intent.putExtra("announcementId", "");
		intent.putExtra("announcementInfor", "");
		startActivity(intent);
	}
}
