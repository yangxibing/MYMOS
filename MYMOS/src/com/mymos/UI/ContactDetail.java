package com.mymos.UI;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.mymos.Entity.User;
import com.mymos.Util.UserDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactDetail extends Activity {

	private String mContactsDetail;
	
	private int mPriority = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.contact_detail);
		
		UserDao userDao = new UserDao(this);
		List<User> list = userDao.queryAll();
		if(!list.toString().equals("[]")){
			Log.i("priority1:",""+mPriority);
			mPriority = list.get(0).getPriority();
			Log.i("priority2:",""+mPriority);
		}
		Log.i("priority3:",""+mPriority);
		if(mPriority == 1){
			Button btn = (Button) findViewById(R.id.revise_btn);
			//btn.setActivated(false);
			btn.setVisibility(View.GONE);
		}
		Intent intent = this.getIntent();
		mContactsDetail = intent.getStringExtra("contactsDetail");
		
		try {
			JSONObject json = new JSONObject(mContactsDetail);
			String lastName = json.getString("lastName");
			String firstName = json.getString("firstName");
			String phoneNum = json.getString("phone");
			String email = json.getString("email");
			String city = json.getString("city");
			String birthday = json.getString("birthday");
			
			TextView tvLastName = (TextView) findViewById(R.id.lastName);
			TextView tvFirstName = (TextView) findViewById(R.id.contactFirstName);
			TextView tvPhone = (TextView) findViewById(R.id.phone);
			TextView tvEmail = (TextView) findViewById(R.id.email);
			TextView tvCity = (TextView) findViewById(R.id.city);
			TextView tvBirthday = (TextView) findViewById(R.id.contactBirthday);
			
			tvLastName.setText(lastName);
			tvFirstName.setText(firstName);
			tvPhone.setText(phoneNum);
			tvEmail.setText(email);
			tvCity.setText(city);
			tvBirthday.setText(birthday);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void modify_contact_detail(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this, ModifyDetail.class);
		intent.putExtra("contactsDetail", mContactsDetail);
		startActivity(intent);
	}

}
