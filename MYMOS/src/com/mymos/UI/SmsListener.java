package com.mymos.UI;

import com.mymos.Util.SmsMmsService;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


public class SmsListener extends BroadcastReceiver {
	NotificationManager nm;
	static final int NOTIFICATION_ID = 0x123;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// 如果是接收到短信
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
			// 取消广播（这行代码将会让系统收不到短信）
			abortBroadcast(); // ①
			StringBuilder sb = new StringBuilder();
			// 接收由SMS传过来的数据
			Bundle bundle = intent.getExtras();
			// 判断是否有数据
			if (bundle != null) {
				// 通过pdus可以获得接收到的所有短信消息
				Object[] pdus = (Object[]) bundle.get("pdus");
				// 构建短信对象array,并依据收到的对象长度来创建array的大小
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				// 将送来的短信合并自定义信息于StringBuilder当中
				for (SmsMessage message : messages) {
					sb.append("短信来源:");
					// 获得接收短信的电话号码
					sb.append(message.getDisplayOriginatingAddress());

					if (message.getDisplayMessageBody().startsWith("#MYMOS")) {
						sb.append("\n------内部短信------\n");
						// 获得短信的内容
						sb.append(message.getDisplayMessageBody());

					} else {
						sb.append("\n------手机短信------\n");
						// 获得短信的内容
						sb.append(message.getDisplayMessageBody());

					}

				}
			}
		
			Toast.makeText(context, sb.toString(), 100000).show();
			

		}
	}

	// TODO Auto-generated method stub

}
