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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener{

	private Button mBtnRegister;
	
	private Handler mHandler;
	private RequestAndResponse mRar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.register);
		
		mRar = new RequestAndResponse();
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 0x111:
					if(msg.obj.toString().equals("1")){
						Toast.makeText(RegisterActivity.this,"注册成功", 2000).show();
						Intent intent = new Intent();
				    	intent.setClass(RegisterActivity.this, LoginActivity.class);
				    	startActivity(intent);
				    	finish();
					}else{
						mDialog.dismiss();
						Toast.makeText(RegisterActivity.this,"注册失败，手机号已被注册",2000).show();
					}
					break;
				default:
					break;
				}
			}
        	
        };
        
		initView();
		
	}
	
	
	public void initView()
	{
		mBtnRegister = (Button) findViewById(R.id.register_btn);
		mBtnRegister.setOnClickListener(this);
	}
	
	

	private Dialog mDialog = null;
	private void showRequestDialog()
	{
		if (mDialog != null)
		{
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "注册中...");
		mDialog.show();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.register_btn:
			EditText etLastName = (EditText) findViewById(R.id.lastName);
			EditText etFirstName = (EditText) findViewById(R.id.firstName);
			EditText etPassword = (EditText) findViewById(R.id.password);
			EditText etPhone = (EditText) findViewById(R.id.phone);
			EditText etEmail = (EditText) findViewById(R.id.email);
			EditText etCity = (EditText) findViewById(R.id.city);
			TextView etBirthday = (TextView) findViewById(R.id.birthday);
			String lastName = etLastName.getText().toString();
			String firstName = etFirstName.getText().toString();
			String password = etPassword.getText().toString();
			String phoneNum = etPhone.getText().toString();
			String email = etEmail.getText().toString();
			String city = etCity.getText().toString();
			String birthday = etBirthday.getText().toString();
			
			if(lastName.equals("")){
				Toast.makeText(this, "请输入您的姓", 1000).show();
				break;
			}
			if(firstName.equals("")){
				Toast.makeText(this, "请输入您的名", 1000).show();
				break;
			}
			if(password.equals("")){
				Toast.makeText(this, "请输入您的密码", 1000).show();
				break;
			}
			if(phoneNum.equals("")){
				Toast.makeText(this, "请输入您的手机号",1000).show();
				break;
			}
			if(email.equals("")){
				Toast.makeText(this, "请输入您的邮箱", 1000).show();
				break;
			}
			if(city.equals("")){
				Toast.makeText(this, "请输入您所在的城市", 1000).show();
				break;
			}
			if(birthday.equals("")){
				Toast.makeText(this, "请输入您的生日", 1000).show();
				break;
			}
			
			Log.i("birthday",birthday);
			
			JSONObject json = new JSONObject();
			try {
				json.put("lastName", lastName);
				json.put("password", password);
				json.put("phoneNumber", phoneNum);
				json.put("firstName", firstName);
				json.put("city", city);
				json.put("email", email);
				json.put("birthday", birthday);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			final String sendInfor = json.toString();
	
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						String response = mRar.access("androidRegister", sendInfor);
						Message msg = new Message();
						msg.what = 0x111;
						msg.obj = response;
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
			showRequestDialog();

			break;
			default:
				break;
		}
	}
	public void select_date(View v)
	{
		Calendar c = Calendar.getInstance();
		
		new DatePickerDialog(RegisterActivity.this,
			
			new DatePickerDialog.OnDateSetListener()
			{
				@Override
				public void onDateSet(DatePicker dp, int year,
					int month, int dayOfMonth)
				{
					TextView show = (TextView) findViewById(R.id.birthday);
					show.setText( year + "-" + (month + 1)
						+ "-" + dayOfMonth);
				}

			}
		
		, c.get(Calendar.YEAR)
		, c.get(Calendar.MONTH)
		, c.get(Calendar.DAY_OF_MONTH)).show();
	}

	}

