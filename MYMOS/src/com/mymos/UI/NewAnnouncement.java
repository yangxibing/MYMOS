package com.mymos.UI;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.mymos.Entity.User;
import com.mymos.Util.RequestAndResponse;
import com.mymos.Util.UserDao;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewAnnouncement extends Activity {
	private Spinner mSpinner;
	private String mAnnouncementId = null;
	
	private RequestAndResponse mRar;
	private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.new_announcement);
		
		mRar = new RequestAndResponse();
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what == 0x111){
					mDialog.dismiss();
					Toast.makeText(NewAnnouncement.this, "发布成功", 2000).show();
				}
				if(msg.what == 0x112){
					Toast.makeText(NewAnnouncement.this, "已保存至草稿", 2000).show();
				}
			}
			
		};
		
		Intent intent = this.getIntent();
		String id = intent.getStringExtra("announcementId");
		String announcementInfor = intent.getStringExtra("announcementInfor");
		if(!id.equals("")&&!announcementInfor.equals("")){
			try {
				JSONObject jsonObject = new JSONObject(announcementInfor);
				mAnnouncementId = id;
				String title = jsonObject.getString("title");
				String content = jsonObject.getString("content");
				EditText etTitle = (EditText) findViewById(R.id.announcement_title);
				EditText etContent = (EditText) findViewById(R.id.content);
				etTitle.setText(title);
				etContent.setText(content);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		mSpinner = (Spinner) findViewById(R.id.announcement_scope);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,new String[]{"管理员","用户"});
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
	}

	public void cancelClicked(View v){
		finish();
	}
	
	public void okClicked(View v){
		//判断是否有空没填
		if(!isFilled()){
			return;
		}
		//判断是否为草稿
		RadioButton rbIsRelease = (RadioButton) findViewById(R.id.issue);
		
		Log.i("attr",rbIsRelease.isChecked()+"");
		if(rbIsRelease.isChecked()){
			//发布
			final String infor = getAnnouncementInfor();
			
			//发送给服务器
			new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						if(mAnnouncementId != null){
							mRar.access("deleteAnnouncement", mAnnouncementId);
						}					
						mRar.access("addAnnouncement", infor);						
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
			showDialog();
			//发送短信给相应发布范围的角色
		}else{
			//草稿
			final String infor = getAnnouncementInfor();
			
			//发送给服务器
			new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						if(mAnnouncementId != null){
							mRar.access("deleteAnnouncement", mAnnouncementId);
						}	
						mRar.access("addDraftAnnouncement", infor);
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
	
	public String getAnnouncementInfor(){
		EditText etTitle = (EditText) findViewById(R.id.announcement_title);
		//RadioGroup rgAttribute = (RadioGroup) findViewById(R.id.attibute);
		TextView tvEndDate = (TextView) findViewById(R.id.end_date);
		//RadioGroup rgRemindWay = (RadioGroup) findViewById(R.id.remind_way);
		EditText etContent = (EditText) findViewById(R.id.content);
		
		String title = etTitle.getText().toString();
		String scope = mSpinner.getSelectedItem().toString();
		//int attr = rgAttribute.getCheckedRadioButtonId();
		String endDate = tvEndDate.getText().toString();
		//int remindWay = rgRemindWay.getCheckedRadioButtonId();
		String content = etContent.getText().toString();
		
		JSONObject json = new JSONObject();
		try {
			json.put("title", title);
			json.put("scope", scope);
			json.put("endDate", endDate);
			json.put("content", content);
			
			UserDao userDao = new UserDao(NewAnnouncement.this);
			List<User> list = userDao.queryAll();
			
			if(list != null&&!list.toString().equals("[]")){
				json.put("authorId", list.get(0).getId());
			}else{
				json.put("authorId", "");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return json.toString();
	}
	
	public boolean isFilled(){
		EditText etTitle = (EditText) findViewById(R.id.announcement_title);
		TextView tvEndDate = (TextView) findViewById(R.id.end_date);
		EditText etContent = (EditText) findViewById(R.id.content);
		
		if(etTitle.getText().toString().equals("")){
			Toast.makeText(this, "请输入公告标题", 2000).show();
			return false;
		}
		if(tvEndDate.getText().toString().equals("")){
			Toast.makeText(this, "请输入终止日期", 2000).show();
			return false;
		}
		if(etContent.getText().toString().equals("")){
			Toast.makeText(this, "请输入公告内容", 2000).show();
			return false;
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date endDate = new Date();
		try {
			endDate = sdf.parse(tvEndDate.getText().toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(date.after(endDate)){
			Toast.makeText(this, "您输入的终止日期已过", 2000).show();
			return false;
		}
		return true;
	}
	
	private Dialog mDialog = null;

	private void showDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在发布...");
		mDialog.show();
		// Intent intent = new Intent();
		// intent.setClass(this, indexActivity.class);
		// startActivity(intent);

	}
	public void set_end_date(View v)
	{Calendar c = Calendar.getInstance();
	
	new DatePickerDialog(NewAnnouncement.this,
		
		new DatePickerDialog.OnDateSetListener()
		{
			@Override
			public void onDateSet(DatePicker dp, int year,
				int month, int dayOfMonth)
			{
				TextView show = (TextView) findViewById(R.id.end_date);
				show.setText( year + "-" + (month + 1)
					+ "-" + dayOfMonth);
			}

		}
	
	, c.get(Calendar.YEAR)
	, c.get(Calendar.MONTH)
	, c.get(Calendar.DAY_OF_MONTH)).show();
		
	}
}
