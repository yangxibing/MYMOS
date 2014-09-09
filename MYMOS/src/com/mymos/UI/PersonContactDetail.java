package com.mymos.UI;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PersonContactDetail extends Activity {
   private TextView name;
   private TextView phoneNumber;
   private TextView email;
   private Button modify;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.personcontact_detail);
		String p_phoneNumber = getIntent().getStringExtra("phoneNumber");
		String p_name = getIntent().getStringExtra("name");
		//String p_email = getIntent().getStringExtra("email");
		name = (TextView)findViewById(R.id.p_Name);
		phoneNumber = (TextView)findViewById(R.id.p_phone);
		email = (TextView)findViewById(R.id.P_email);
		name.setText(p_name);
		phoneNumber.setText(p_phoneNumber);
		
		int contact_id = Integer.parseInt(getIntent().getStringExtra("contactId"));
		Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
				null, ContactsContract.CommonDataKinds.Email.CONTACT_ID+"="+contact_id, null, null);
		String emailaddress =null;
		while(emails.moveToNext()){
			emailaddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
		}
		emails.close();
		email.setText(emailaddress);
		modify =(Button)findViewById(R.id.p_revise_btn);
		modify.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent();
				intent2.putExtra("p_phoneNumber", phoneNumber.getText());
				intent2.putExtra("p_name", name.getText());
				intent2.putExtra("contactId", getIntent().getStringExtra("contactId"));
				intent2.setClass(PersonContactDetail.this, ModifyPersonDetail.class);
				
				startActivity(intent2);
			}
			
		});
		 
	}
	

}
