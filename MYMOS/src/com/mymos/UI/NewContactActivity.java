package com.mymos.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewContactActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_contact);
		
		
	}
public void enter_myGroup(View v) {
		
		Intent intent = new Intent();
    	intent.setClass(this, GroupActivity.class);
    	startActivity(intent);
	
	}
}
