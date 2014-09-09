package com.mymos.UI;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.TabActivity;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.content.ContentResolver;
import android.database.Cursor;

@SuppressWarnings("deprecation")
public class ContactTest extends TabActivity  {
	ListView lv;
	Cursor c;
	    // 此处定义了将要被查询的字段列表

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.contacts);
		initalTabHost();
		init();

	}
	
	private void init(){
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
		String[] projection = { 
				ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
		}; // 查询的列
		ContentResolver cr = this.getContentResolver();
		c = cr.query(uri, projection, null, null,"	ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME desc");
		c.moveToFirst();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.listitem_style, c, new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },
				new int[] { R.id.pub_groupName });
				lv.setAdapter(adapter);
				}

	public void initalTabHost() {
		TabHost tabHost2 = getTabHost();
		TabHost tabHost = tabHost2;
		//
		TabSpec tab1 = tabHost.newTabSpec("tab01").setIndicator("个人通讯录")
				.setContent(R.id.tab01);
 
		tabHost.addTab(tab1);
		TabSpec tab2 = tabHost.newTabSpec("tab02")

		.setIndicator("公共通讯录").setContent(R.id.tab02);
		// .getDrawable(R.drawable.ic_launcher))

		tabHost.addTab(tab2);
		TabWidget tabWidget = getTabWidget();
		int count = tabWidget.getChildCount();//
		for (int i = 0; i < count; i++) {
			View view = tabWidget.getChildTabViewAt(i);

			final TextView tv = (TextView) view
					.findViewById(android.R.id.title);
			tv.setTextSize(20);
			tv.setGravity(Gravity.CENTER);

		}
	}
	

}
