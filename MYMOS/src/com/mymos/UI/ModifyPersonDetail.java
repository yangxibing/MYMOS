package com.mymos.UI;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyPersonDetail extends Activity {
	private TextView m_name;
	   private TextView m_phoneNumber;
	 

	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.modify_person_detail);
		String p_phoneNumber = getIntent().getStringExtra("p_phoneNumber");
		String p_name = getIntent().getStringExtra("p_name");
		m_name = (TextView)findViewById(R.id.modify_p_Name);
		m_phoneNumber = (TextView)findViewById(R.id.modify_p_Phone);
		m_name.setText(p_name);
		m_phoneNumber.setText(p_phoneNumber);
		Button modify = (Button)findViewById(R.id.modify_p_but);
		//Log.i("yangnamedd", "   dd");
		//Toast.makeText(this, p_name, Toast.LENGTH_LONG).show();
		
		
		modify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String name = ((EditText)ModifyPersonDetail.this.findViewById(R.id.modify_p_Name)).getText().toString();
				//String phone  = ((EditText)ModifyPersonDetail.this.findViewById(R.id.modify_p_Phone)).getText().toString();
				String email  = ((EditText)ModifyPersonDetail.this.findViewById(R.id.modifyEmail)).getText().toString();
				//Log.i("name", name);
				//Toast.makeText(ModifyPersonDetail.this, name, Toast.LENGTH_LONG).show();
				// h建一个空的ContentValues
				String phone ="12345678";
				// 向RawContacts.CONTENT_URI执行一个空值插入，
				// 目的是获取系统返回的rawContactId
				int contactId = Integer.parseInt(ModifyPersonDetail.this.getIntent().getStringExtra("contactId"));
				Uri personUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactId);
				//String values
				/*
				ContentValues values = new ContentValues();  
				values.put(Phone.NUMBER, phone);  
				values.put(Phone.TYPE, Phone.TYPE_MOBILE);  
				String Where = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";  
				String[] WhereParams = new String[]{""+contactId, Phone.CONTENT_ITEM_TYPE};  
				getContentResolver().update(ContactsContract.Data.CONTENT_URI,   
				values, Where, WhereParams);
				*/
				
				
				
				
				if(!name.equals("")){
				ContentValues values = new ContentValues();
				//values.put(StructuredName.TYPE, StructuredName.DISPLAY_NAME); 
				values.put(StructuredName.DISPLAY_NAME, name);
				getContentResolver().update(android.provider.ContactsContract.Data.CONTENT_URI, values, 
						 ContactsContract.Data.RAW_CONTACT_ID+ " =? and " +ContactsContract.Data.MIMETYPE +" =? ", 
			    		new String[]{""+contactId,StructuredName.CONTENT_ITEM_TYPE});
				}
				/*
			    if(!phone.equals("")){
			    ContentValues values = new ContentValues();
			    values.put(Phone.NUMBER, phone);
			  getContentResolver().update(android.provider.ContactsContract.Data.CONTENT_URI, values, Data.RAW_CONTACT_ID+ " =? and " +Data.MIMETYPE +" =? ", 
			    		new String[]{""+contactId,Phone.CONTENT_ITEM_TYPE});
			    }
			    if(!email.equals("")){
			    ContentValues values = new ContentValues();
			    values.put(Email.DATA, email);
			   getContentResolver().update(android.provider.ContactsContract.Data.CONTENT_URI, values, Data.RAW_CONTACT_ID+ " =? and " +Data.MIMETYPE +" =? ", 
			    		new String[]{""+contactId,Email.CONTENT_ITEM_TYPE});
			    }
			  */
			    Toast.makeText(ModifyPersonDetail.this, "联系人数据已修改",
						15000).show();
				Intent intent = new Intent();
				intent.setClass(ModifyPersonDetail.this, ContactActivity.class);
				startActivity(intent);
				finish();
	
				// TODO Auto-generated method stub
				
				
				
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}

}
