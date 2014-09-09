package com.mymos.UI;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class SendPhoneMessage extends Activity {

	EditText number, content;
	Button send;
	Button save;
	Button add_contact;
	SmsManager sManager;
	private String phoneNumberText = new String();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_phone_message);
		// 获取phoneNumber

		// 获取SmsManager
		sManager = SmsManager.getDefault();
		// 获取程序界面上的两个文本框和按钮
		number = (EditText) findViewById(R.id.phonenumber);
		content = (EditText) findViewById(R.id.receiverText2);
		add_contact = (Button) findViewById(R.id.add_contact);
		// System.out.println(getIntent().getStringExtra("message_content"));
		// Log.i("yang", getIntent().getStringExtra("message_content"));
		add_contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SendPhoneMessage.this, SelectContacts.class);
				intent.putExtra("request", "phone_request");
				startActivity(intent);
				finish();

			}

		});
		content.setText(getIntent().getStringExtra("message_content"));
		Intent intent = SendPhoneMessage.this.getIntent();
		if (intent.getStringExtra("type").equals("listcontact")) {
			String phoneNumber = intent.getStringExtra("phoneNumber");

			try {
				JSONArray jsonNumber = new JSONArray(phoneNumber);
				for (int i = 0; i < jsonNumber.length(); i++) {
					if (i == jsonNumber.length() - 1) {
						phoneNumberText += jsonNumber.get(i).toString();
					} else {
						phoneNumberText += jsonNumber.get(i).toString() + ",";
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			number.setText(phoneNumberText);
		} else if (intent.getStringExtra("type").equals("contact")) {
			String phoneNumber = intent.getStringExtra("phoneNumber");
            phoneNumberText = phoneNumber;
            number.setText(phoneNumberText);
            
		}else{
			number.setText("");
		}

		// save = (Button) findViewById(R.id.save_phone_message_button);

		send = (Button) findViewById(R.id.send_phone_message_button);
		// 为send按钮的单击事件绑定监听器

		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (number.getText().toString().equals("")) {

					Toast.makeText(SendPhoneMessage.this, "发送号码不能为空!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (content.getText().toString().equals("")) {
					Toast.makeText(SendPhoneMessage.this, "发送内容不能为空!",
							Toast.LENGTH_SHORT).show();
					return;

				}

				Intent intent = SendPhoneMessage.this.getIntent();
				String phoneNumber = intent.getStringExtra("phoneNumber");
				try {
					JSONArray jsonNumber = new JSONArray(phoneNumber);
					for (int i = 0; i < jsonNumber.length(); i++) {
						PendingIntent pi = PendingIntent.getActivity(
								SendPhoneMessage.this, 0, new Intent(), 0);
						// 发送短信
						sManager.sendTextMessage(jsonNumber.getString(i), null,
								content.getText().toString(), pi, null);
						writeToSendDataBase(jsonNumber.getString(i), content
								.getText().toString());
						number.setText("");
						content.setText("");

						// 提示短信发送完成
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Toast.makeText(SendPhoneMessage.this, "短信发送完成", 10000).show();

				// 创建一个PendingIntent对象

			}
		});
		/*
		 * save.setOnClickListener(new OnClickListener(){
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub /* if(number.getText().toString().equals("")){
		 * 
		 * Toast.makeText(SendPhoneMessage.this, "发送号码不能为空!",
		 * Toast.LENGTH_SHORT).show(); return; }
		 * 
		 * if(content.getText().toString().equals("")){
		 * Toast.makeText(SendPhoneMessage.this, "发送内容不能为空!",
		 * Toast.LENGTH_SHORT).show(); return;
		 * 
		 * } writeToDraftDataBase("",content.getText().toString());
		 * Toast.makeText(SendPhoneMessage.this, "已保存到草稿箱!",
		 * Toast.LENGTH_SHORT).show(); }
		 * 
		 * });
		 */

	}

	private void writeToDraftDataBase(String phoneNumber, String smsContent) {
		ContentValues values = new ContentValues();
		values.put("address", phoneNumber);
		values.put("body", smsContent);
		values.put("type", "3");
		// values.put("read", "1");//"1"means has read ,1表示已读
		getContentResolver().insert(Uri.parse("content://mms/draft"), values);
	}

	private void writeToSendDataBase(String phoneNumber, String smsContent) {
		ContentValues values = new ContentValues();
		values.put("address", phoneNumber);
		values.put("body", smsContent);
		values.put("type", "2");
		values.put("read", "1");// "1"means has read ,1表示已读
		getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
	}

}