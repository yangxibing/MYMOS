package com.mymos.UI;


import java.util.ArrayList;
import java.util.List;

import com.mymos.Entity.Announcement;
import com.mymos.Entity.User;
import com.mymos.Util.UserDao;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;


@SuppressWarnings("deprecation")
public class IndexActivity extends TabActivity implements OnClickListener {
	private Button contactsButton;
	private Button messageButton;
	private Button processButton;
	private Button documnet_button;
	private Button newsButton,exit;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		initTabHost();
		
		

		initView();

	}

	private void initTabHost() {
		TabHost tabHost2 = getTabHost();
		TabHost tabHost = tabHost2;
		TabSpec tab1 = tabHost.newTabSpec("tab1").setIndicator("中心") 
				.setContent(R.id.tab01); 
		tabHost.addTab(tab1);
		TabSpec tab2 = tabHost.newTabSpec("tab2")
				.setIndicator("消息") 
				.setContent(R.id.tab02);
		tabHost.addTab(tab2);
		TabSpec tab3 = tabHost.newTabSpec("tab3").setIndicator("提醒")
				.setContent(R.id.tab03);
		tabHost.addTab(tab3);
		TabSpec tab4 = tabHost.newTabSpec("tab4").setIndicator("我")
				.setContent(R.id.tab04);
	
		tabHost.addTab(tab4);

		TabWidget tabWidget = getTabWidget();
		int count = tabWidget.getChildCount();// TabHost����һ��getTabWidget()�ķ���
		for (int i = 0; i < count; i++) {
			View view = tabWidget.getChildTabViewAt(i);

			final TextView tv = (TextView) view
					.findViewById(android.R.id.title);

		}
		
		UserDao userDao = new UserDao(IndexActivity.this);
		
		List<User> list=new ArrayList<User>();
		Log.i("userDao list1",list.toString());
		list= userDao.queryAll();

		
		Log.i("userDao list",list.toString());
		if(list != null&&!list.toString().equals("[]")){
			String name = list.get(0).getLastName() + list.get(0).getFirstName();
			TextView tvPersonName = (TextView) findViewById(R.id.person_name);
			tvPersonName.setText(name);
		}

	}

	public void initView() {
		contactsButton = (Button) findViewById(R.id.contacts_button);
		contactsButton.setOnClickListener(this);
		messageButton = (Button) findViewById(R.id.message_button);
		messageButton.setOnClickListener(this);
		processButton = (Button) findViewById(R.id.process_button);
		processButton.setOnClickListener(this);
	
		documnet_button  = (Button) findViewById(R.id.docs_button);
		documnet_button.setOnClickListener(this);
		
        //exit = (Button)findViewById(R.id.exit_btn);
       // exit.setOnClickListener(this);
		newsButton=(Button)findViewById(R.id.news_button);
		newsButton.setOnClickListener(this);


	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.contacts_button:
			Intent intent = new Intent();
			intent.setClass(this, ContactActivity.class);
			startActivity(intent);
			break;
		case R.id.message_button:
			Intent intent1 = new Intent();
	    	intent1.setClass(this, MessageActivity.class);
	    	startActivity(intent1);
	    	break;

	    	
		case R.id.news_button:
			Intent intent_news=new Intent(IndexActivity.this, NewsMainActivity.class);
			startActivity(intent_news);
			break;	

		case R.id.process_button:
			Intent intent2 = new Intent();
	    	intent2.setClass(this, ProcessActivity.class);
	    	startActivity(intent2);
	    	break;
		case R.id.docs_button:
			Intent intent3 = new Intent();
	    	intent3.setClass(this, DocumentActivity.class);
	    	startActivity(intent3);
	    	break;

		default:
			break;
		}
	}

	public void enter_announcement(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this, AnnouncementActivity.class);
		startActivity(intent);		
	}
	public void click_password_manage(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this, PasswordManage.class);
		startActivity(intent);		
	}
	public void exitAPP(View v)
	{
		System.exit(0);	
	}
	
}
