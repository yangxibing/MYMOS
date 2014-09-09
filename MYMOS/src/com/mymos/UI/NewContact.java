package com.mymos.UI;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewContact extends Activity {
	private EditText newName;
	private EditText newPhone;
	private EditText newEmail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.new_person_contact);
		Button add = (Button)findViewById(R.id.add_p_person_btn);
		add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String name = ((EditText)findViewById(R.id.new__p_Name)).getText().toString();
				String phone  = ((EditText)findViewById(R.id.new__p_Phone)).getText().toString();
				String email  = ((EditText)findViewById(R.id.new_p_Email)).getText().toString();
				// 创建一个空的ContentValues
				ContentValues values = new ContentValues();
				// 向RawContacts.CONTENT_URI执行一个空值插入，
				// 目的是获取系统返回的rawContactId
				Uri rawContactUri = getContentResolver().insert(
					RawContacts.CONTENT_URI, values);
				long rawContactId = ContentUris.parseId(rawContactUri);
				values.clear();
				values.put(Data.RAW_CONTACT_ID, rawContactId);
				// 设置内容类型
				values
					.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
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
				values.clear();
				values.put(Data.RAW_CONTACT_ID, rawContactId);
				values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
				// 设置联系人的Email地址
				values.put(Email.DATA, email);
				// 设置该电子邮件的类型
				values.put(Email.TYPE, Email.TYPE_WORK);
				// 向联系人Email URI添加Email数据
				getContentResolver().insert(
					android.provider.ContactsContract.Data.CONTENT_URI,
					values);
				Toast.makeText(NewContact.this, "联系人数据添加成功",
					15000).show();
				Intent intent = new Intent();
				intent.setClass(NewContact.this, ContactActivity.class);
				startActivity(intent);
				finish();
	
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
	}
}


