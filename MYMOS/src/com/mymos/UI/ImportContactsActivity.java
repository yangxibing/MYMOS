package com.mymos.UI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.mymos.Util.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class ImportContactsActivity extends Activity {
	private static final int REQUEST_EX = 1;
	private String fileName = new String();
	
	private Handler mHandler;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.daoru_contacts);
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 0x222:
					Toast.makeText(ImportContactsActivity.this, "导入联系人成功", 20000).show();
					mDialog.dismiss();
					break;
				default:
					break;
				}
			}
			
		};
	}

	public void scard_in(View v) {
		Intent intent = new Intent();
		intent.putExtra("explorer_title",
				getString(R.string.dialog_read_from_dir));
		intent.setDataAndType(Uri.fromFile(new File("/sdcard")), "*/*");
		intent.setClass(ImportContactsActivity.this, ExDialog.class);
		startActivityForResult(intent, REQUEST_EX);
	}

	public void start_import(View v) {
		showRequestDialog();
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					FileInputStream fis = new FileInputStream(fileName);
					BufferedReader br = new BufferedReader(new InputStreamReader(fis));
					// StringBuilder sb =new StringBuilder("");
					String line = null;
					//String fileString = new String();
					String name = "";
					String phone = "";
					//Toast.makeText(ImportContactsActivity.this,
							//"导入联系人成功" + br.readLine(), 30000).show();
					while ((line = br.readLine()) != null) {
						Log.d("line: ", line);
						int location = line.indexOf(",");
						name = line.substring(0, location);
						phone = line.substring(location + 1);
						//fileString = name + phone;
						Log.d("name", name);
						Log.d("phone", phone);
						ContentValues values = new ContentValues();
						// 向RawContacts.CONTENT_URI执行一个空值插入，
						// 目的是获取系统返回的rawContactId
						Uri rawContactUri = getContentResolver().insert(
								RawContacts.CONTENT_URI, values);
						long rawContactId = ContentUris.parseId(rawContactUri);
						values.clear();
						values.put(Data.RAW_CONTACT_ID, rawContactId);
						// 设置内容类型
						values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
						// 设置联系人名字
						values.put(StructuredName.GIVEN_NAME, name);
						// 向联系人URI添加联系人名字
						getContentResolver().insert(
								android.provider.ContactsContract.Data.CONTENT_URI,
								values);
						values.clear();
						values.put(Data.RAW_CONTACT_ID, rawContactId);
						values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
						// 设置联系人的电话号码
						values.put(Phone.NUMBER, phone);
						// 设置电话类型
						values.put(Phone.TYPE, Phone.TYPE_MOBILE);
						// 向联系人电话号码URI添加电话号码
						getContentResolver().insert(
								android.provider.ContactsContract.Data.CONTENT_URI,
								values);
						
					}
					br.close();
				} catch (FileNotFoundException e) {
					Toast.makeText(ImportContactsActivity.this, "导入联系人失败", 30000)
							.show();
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				Message msg = new Message();
				msg.what = 0x222;
				mHandler.sendMessage(msg);
				
			}
			
		}.start();

		

	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// String path;
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_EX) {
				Uri uri = intent.getData();
				fileName = uri.getPath();
				Toast.makeText(ImportContactsActivity.this,
						"select: " + fileName, 30000).show();

			}
		}
	}

	private Dialog mDialog = null;

	private void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "导入中...");
		mDialog.show();
	}
}
