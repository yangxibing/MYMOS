package com.mymos.UI;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONObject;

import com.mymos.Util.RequestAndResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewsType extends Activity {

	private EditText mNewsTypeName;
	private EditText mNewsTypeNumber;
	private Button mSubmitNewsType;
	private Button mCancelCreateNewsType;
	private RequestAndResponse mRar;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_newstype);
		mNewsTypeName = (EditText) findViewById(R.id.input_NewsType);
		mNewsTypeNumber = (EditText) findViewById(R.id.input_NewsTypeNumber);
		mSubmitNewsType = (Button) findViewById(R.id.addNewsType_submit);
		mCancelCreateNewsType = (Button) findViewById(R.id.addNewsType_cancel);
		mRar = new RequestAndResponse();

		/* 处理服务器返回结果 */
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x111) {
					if (!(msg.obj.toString().isEmpty())) {
						Log.i("添加新闻类型Id", msg.obj.toString());
						Toast.makeText(AddNewsType.this, "添加新闻类型成功", 2000)
								.show();
						AddNewsType.this.finish();
					}
				}
			}
		};
        

		
		/** 取消创建新闻类型 */
		mCancelCreateNewsType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent cancelCreateIntent = new Intent(AddNewsType.this,
						NewsType.class);
				startActivity(cancelCreateIntent);
			}
		});

		/* 创建新闻类型 */
		mSubmitNewsType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// 新闻类型名和新闻类型Id不为空时提交按钮可用
				if (mNewsTypeName.getText().toString().isEmpty() || mNewsTypeNumber
						.getText().toString().isEmpty()) {
					Toast.makeText(AddNewsType.this, "新闻类型或和排序号为空，请重新输入", 4000)
					.show();
                    Log.i("添加新闻类型if", mNewsTypeName.getText().toString());
                    Log.i("添加新闻类型if", mNewsTypeNumber.getText().toString());
				} else {
					Map<String, String> map = new HashMap<String, String>();
                    Log.i("添加新闻类型", mNewsTypeName.getText().toString());
                    Log.i("添加新闻类型", mNewsTypeNumber.getText().toString());
					map.put("name", mNewsTypeName.getText().toString());
					map.put("sortNumber", mNewsTypeNumber.getText()
							.toString());

					final JSONObject jsonObj = new JSONObject(map);

					new Thread() {
						@Override
						public void run() {

							try {
								String addNewsReturnMsg = mRar.access(
										"addNewsType", jsonObj.toString());
								Message msg = new Message();
								msg.what = 0x111;
								msg.obj = addNewsReturnMsg;
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
		});
	}
}
