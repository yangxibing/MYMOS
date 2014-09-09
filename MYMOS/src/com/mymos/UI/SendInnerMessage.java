package com.mymos.UI;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class SendInnerMessage extends Activity {

	EditText number, content;
	Button send, settime;
	SmsManager sManager;
	Button setTime, add;
	AlarmManager aManager;
	private String numberText = new String();

	Calendar currentTime = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_inner_message);
		sManager = SmsManager.getDefault();
		ContentValues values = new ContentValues();
		aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		// getContentResolver().
		// 获取程序界面上的两个文本框和按钮
		number = (EditText) findViewById(R.id.receiverText1);
		content = (EditText) findViewById(R.id.innerMessageContent);
		// System.out.println(getIntent().getStringExtra("message_content"));
		// Log.i("yang", getIntent().getStringExtra("message_content"));
		content.setText(getIntent().getStringExtra("message_content"));

		Intent intent = SendInnerMessage.this.getIntent();
		if (intent.getStringExtra("type").equals("listcontact")) {

			String phoneNumber = intent.getStringExtra("phoneNumber");

			try {
				JSONArray jsonNumber = new JSONArray(phoneNumber);
				for (int i = 0; i < jsonNumber.length(); i++) {

					if (i == jsonNumber.length() - 1) {
						numberText += jsonNumber.get(i).toString();
					} else {
						numberText += jsonNumber.get(i).toString() + ",";
					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			number.setText(numberText);
		}
		else if (intent.getStringExtra("type").equals("contact")) {
			String phoneNumber = intent.getStringExtra("phoneNumber");
			numberText = phoneNumber;
            number.setText(numberText);
            
		}else{
			number.setText("");
		}

		// number.setText(getIntent().getStringExtra("phoneNumber"));
		settime = (Button) findViewById(R.id.set_time);
		send = (Button) findViewById(R.id.send_inner_message_button);
		add = (Button) findViewById(R.id.add_inner_contact);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(SendInnerMessage.this, SelectContacts.class);
				intent.putExtra("request", "inner_request");
				startActivity(intent);
				finish();

			}

		});
		// 为send按钮的单击事件绑定监听器
		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 创建一个PendingIntent对象
				PendingIntent pi = PendingIntent.getActivity(
						SendInnerMessage.this, 0, new Intent(), 0);
				if (number.getText().toString().equals("")) {
					Toast.makeText(SendInnerMessage.this, "发送号码不能为空!",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					// 发送短信
					sManager.sendTextMessage(number.getText().toString(), null,
							"#MYMOS" + content.getText().toString(), pi, null);
					writeToDataBase(number.getText().toString(), "#MYMOS"
							+ content.getText().toString());
					// 提示短信发送完成
					number.setText("");
					content.setText("");
					Toast.makeText(SendInnerMessage.this, "短信发送完成", 10000)
							.show();
				}
			}
		});
		settime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				send.setClickable(false);
				Calendar currentTime = Calendar.getInstance();
				// 创建一个TimePickerDialog实例，并把它显示出来。
				new TimePickerDialog(SendInnerMessage.this, 0, // 绑定监听器
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker tp, int hourOfDay,
									int minute) {
								// 指定启动AlarmActivity组件
								Intent intent = new Intent(
										SendInnerMessage.this,
										sendInnerMessageService.class);
								// 创建PendingIntent对象
								PendingIntent pi = PendingIntent.getActivity(
										SendInnerMessage.this, 0, intent, 0);
								Calendar c = Calendar.getInstance();
								// 根据用户选择时间来设置Calendar对象
								c.set(Calendar.HOUR, hourOfDay);
								c.set(Calendar.MINUTE, minute);
								// 设置AlarmManager将在Calendar对应的时间启动指定组件
								aManager.set(AlarmManager.RTC_WAKEUP,
										c.getTimeInMillis(), pi);
								// 显示闹铃设置成功的提示信息
								Toast.makeText(SendInnerMessage.this,
										"定时内部短信已设置", Toast.LENGTH_SHORT).show();
							}
						}, currentTime.get(Calendar.HOUR_OF_DAY), currentTime
								.get(Calendar.MINUTE), false).show();
			}
		});

	}

	private class sendInnerMessageService extends Service {

		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			// TODO Auto-generated method stub
			PendingIntent pi = PendingIntent.getActivity(SendInnerMessage.this,
					0, new Intent(), 0);
			sManager.sendTextMessage(number.getText().toString(), null,
					"#MYMOS" + content.getText().toString(), pi, null);
			writeToDataBase(number.getText().toString(), "#MYMOS"
					+ content.getText().toString());
			// 提示短信发送完成
			Toast.makeText(SendInnerMessage.this, "定时内部信发送完成", 10000).show();
			return START_STICKY;
		}

	}

	private void writeToDataBase(String phoneNumber, String smsContent) {
		ContentValues values = new ContentValues();
		values.put("address", phoneNumber);
		values.put("body", smsContent);
		values.put("type", "2");
		values.put("read", "1");// "1"means has read ,1表示已读
		getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
	}

	public void set_send_time(View v) {
		Calendar c = Calendar.getInstance();
		// ����һ��TimePickerDialogʵ�������ʾ������
		new TimePickerDialog(this,
		// �󶨼�����
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker tp, int hourOfDay,
							int minute) {
						Toast.makeText(
								SendInnerMessage.this,
								"�����ڣ�" + hourOfDay + "ʱ" + minute + "��"
										+ "����", Toast.LENGTH_SHORT).show();

					}
				}
				// ���ó�ʼʱ��
				, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)
				// true��ʾ����24Сʱ��
				, true).show();
	}

}