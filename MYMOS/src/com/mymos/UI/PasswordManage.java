package com.mymos.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PasswordManage extends Activity {
private Button modify;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_manage);
		
		modify = (Button)findViewById(R.id.m_modify_password);
		modify.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText oldPassword = (EditText)findViewById(R.id.old_password);
				EditText newPassword = (EditText)findViewById(R.id.new_password);
				EditText new_again_Password = (EditText)findViewById(R.id.new_password_again);
				String s_oldPassword = oldPassword.getText().toString();
				String s_newPassword = newPassword.getText().toString();
				String s_new_again_Password = new_again_Password.getText().toString();
				
				
			}
			
		});
		
	}
}