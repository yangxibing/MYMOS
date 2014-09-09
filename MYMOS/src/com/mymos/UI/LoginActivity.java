package com.mymos.UI;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.mymos.Util.DatabaseAccessTool;
import com.mymos.Util.RequestAndResponse;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private RequestAndResponse mRar;
	private Handler mHandler;

	private Button mBtnRegister;
	private Button mBtnLogin;

	private View mMoreView;
	private ImageView mMoreImage;
	private View mMoreMenuView;

	private boolean mShowMenu = false;

	private DatabaseAccessTool dbAT;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);

		// 定义数据库
		dbAT = new DatabaseAccessTool(LoginActivity.this, "User.db3", null, 1);

		mRar = new RequestAndResponse();
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0x123:
					if (!msg.obj.toString().equals("failed")) {
						Toast.makeText(LoginActivity.this, "登录成功", 2000).show();

						SQLiteDatabase db = dbAT.getWritableDatabase();
						
						db.delete("User", null, null);
						
						ContentValues cv = new ContentValues();
						try {
							JSONObject jsonObj = new JSONObject(
									msg.obj.toString());

							jsonObj = new JSONObject(msg.obj.toString());
							cv.put("userId", jsonObj.getString("userId"));
							cv.put("lastName", jsonObj.getString("lastName"));
							cv.put("firstName", jsonObj.getString("firstName"));
							cv.put("phoneNumber", jsonObj.getString("phoneNum"));
							cv.put("password", jsonObj.getString("password"));
							cv.put("email", jsonObj.getString("email"));
							cv.put("city", jsonObj.getString("city"));
							cv.put("birthday", jsonObj.getString("birthday"));
							cv.put("priority", jsonObj.getString("priority"));
							db.insert("User", "userId, lastName, firstName, phoneNumber, password, email, city, birthday, priority", cv);
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Intent intent = new Intent();
						intent.setClass(LoginActivity.this, IndexActivity.class);
						startActivity(intent);
						finish();
					} else {
						mDialog.dismiss();
						Toast.makeText(LoginActivity.this, "登录失败，请检查您的手机号和密码",
								2000).show();
					}
					break;
				case 0x124:
					try {
						JSONObject json = new JSONObject(msg.obj.toString());
						String userId = json.getString("userId");
						String userName = json.getString("userName");

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

		initView();
	}

	public void initView() {

		mBtnRegister = (Button) findViewById(R.id.regist);
		mBtnRegister.setOnClickListener(this);

		mBtnLogin = (Button) findViewById(R.id.login);
		mBtnLogin.setOnClickListener(this);
	}

	public void showMoreView(boolean bShow) {
		if (bShow) {
			mMoreMenuView.setVisibility(View.GONE);
			mMoreImage.setImageResource(R.drawable.login_more_up);
			mShowMenu = true;
		} else {
			mMoreMenuView.setVisibility(View.VISIBLE);
			mMoreImage.setImageResource(R.drawable.login_more);
			mShowMenu = false;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.regist:
			goRegisterActivity();
			break;
		case R.id.login:
			// ��֤�˺�����,���͸������
			EditText phone = (EditText) findViewById(R.id.accounts);
			final String phoneNum = phone.getText().toString();
			EditText pass = (EditText) findViewById(R.id.password);
			String password = pass.getText().toString();

			Log.i("phone", phoneNum);
			Log.i("password", password);

			if (phoneNum.equals("")) {
				Toast.makeText(this, "请输入手机号", 1000).show();
				break;
			}
			if (password.equals("")) {
				Toast.makeText(this, "请输入密码", 1000).show();
				break;
			}

			JSONObject json = new JSONObject();
			try {
				json.put("phoneNum", phoneNum);
				json.put("password", password);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.i("json", "json error");
				e.printStackTrace();
			}
			final String infor = json.toString();
			Log.i("infor", infor);
			Thread thread = new Thread() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						String result = mRar.access("androidLogin", infor);
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = result;
						mHandler.sendMessage(msg);
					} catch (HttpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};
			thread.start();

			showRequestDialog();

			break;
		default:
			break;
		}
	}

	public void goRegisterActivity() {
		Intent intent = new Intent();
		intent.setClass(this, RegisterActivity.class);
		startActivity(intent);
	}

	private Dialog mDialog = null;

	private void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "登录中...");
		mDialog.show();
		// Intent intent = new Intent();
		// intent.setClass(this, indexActivity.class);
		// startActivity(intent);

	}

}
