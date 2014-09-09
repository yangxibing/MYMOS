package com.mymos.UI;

import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.mymos.Util.RequestAndResponse;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyDetail extends Activity {

	private RequestAndResponse mRar;
	private Handler mHandler;
	private String mOldContactsInfor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.modify_detail);
		
		mRar = new RequestAndResponse();
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what == 0x111){
					mDialog.dismiss();
					Toast.makeText(ModifyDetail.this, "修改成功", 2000).show();
					ModifyDetail.this.finish();
				}
			}
			
		};
		Intent intent = this.getIntent();
		mOldContactsInfor = intent.getStringExtra("contactsDetail");
		try {
			JSONObject json = new JSONObject(mOldContactsInfor);
			String lastName = json.getString("lastName");
			String firstName = json.getString("firstName");
			String phone = json.getString("phone");
			String email = json.getString("email");
			String city = json.getString("city");
			String birthday = json.getString("birthday");
			
			EditText etLastName = (EditText) findViewById(R.id.modifyLastName);
			EditText etFirstName = (EditText) findViewById(R.id.modifyFirstName);
			EditText etPhone = (EditText) findViewById(R.id.modifyPhone);
			EditText etEmail = (EditText) findViewById(R.id.modifyEmail);
			EditText etCity = (EditText) findViewById(R.id.modifyCity);
			TextView tvBirthday = (TextView) findViewById(R.id.modifyBirthday);
			
			etLastName.setText(lastName);
			etFirstName.setText(firstName);
			etPhone.setText(phone);
			etEmail.setText(email);
			etCity.setText(city);
			tvBirthday.setText(birthday);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private Dialog mDialog = null;
	public void modify_detail(View v)
	{
		EditText etLastName = (EditText) findViewById(R.id.modifyLastName);
		EditText etFirstName = (EditText) findViewById(R.id.modifyFirstName);
		EditText etPhone = (EditText) findViewById(R.id.modifyPhone);
		EditText etEmail = (EditText) findViewById(R.id.modifyEmail);
		EditText etCity = (EditText) findViewById(R.id.modifyCity);
		TextView tvBirthday = (TextView) findViewById(R.id.modifyBirthday);
		
		String lastName = etLastName.getText().toString();
		String firstName = etFirstName.getText().toString();
		String phone = etPhone.getText().toString();
		String email = etEmail.getText().toString();
		String city = etCity.getText().toString();
		String birthday = tvBirthday.getText().toString();
		
		JSONObject json = new JSONObject();
		
		try {
			JSONObject jsonObject = new JSONObject(mOldContactsInfor);
			json.put("id", jsonObject.getString("id"));
			json.put("lastName", lastName);
			json.put("firstName", firstName);
			json.put("phone", phone);
			json.put("email", email);
			json.put("city", city);
			json.put("birthday", birthday);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String reviseInfor = json.toString();
		Log.i("reviseInfor-----",reviseInfor);
		Log.i("mOldContactsInfor-----",mOldContactsInfor);
		if(cmp(reviseInfor,mOldContactsInfor)){
			Toast.makeText(ModifyDetail.this, "未做任何修改", 2000).show();
			return;
		}
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mRar.access("reviseUserInfor", reviseInfor);
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
		
		if (mDialog != null)
		{
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在提交中...");
		mDialog.show();
		

	}
	
	public boolean cmp(String str1,String str2) {
		try {
			JSONObject json1 = new JSONObject(str1);
			JSONObject json2 = new JSONObject(str2);
			String ln1 = json1.getString("lastName");
			String fn1 = json1.getString("firstName");
			String phone1 = json1.getString("phone");
			String email1 = json1.getString("email");
			String city1 = json1.getString("city");
			String birthday1 = json1.getString("birthday");
			
			String ln2 = json2.getString("lastName");
			String fn2 = json2.getString("firstName");
			String phone2 = json2.getString("phone");
			String email2 = json2.getString("email");
			String city2 = json2.getString("city");
			String birthday2 = json2.getString("birthday");
			
			if(ln1.equals(ln2)&&fn1.equals(fn2)&&phone1.equals(phone2)&&email1.equals(email2)&&city1.equals(city2)&&birthday1.equals(birthday2)){
				return true;
			}
			else{
				return false;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}
	public void select_date(View v)
	{
		Calendar c = Calendar.getInstance();
		
		new DatePickerDialog(ModifyDetail.this,
			
			new DatePickerDialog.OnDateSetListener()
			{
				@Override
				public void onDateSet(DatePicker dp, int year,
					int month, int dayOfMonth)
				{
					TextView show = (TextView) findViewById(R.id.modifyBirthday);
					show.setText( year + "-" + (month + 1)
						+ "-" + dayOfMonth);
				}

			}
		
		, c.get(Calendar.YEAR)
		, c.get(Calendar.MONTH)
		, c.get(Calendar.DAY_OF_MONTH)).show();
	}
	

}
