package com.mymos.UI;

import com.mymos.Util.SData;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import android.app.Activity;

@SuppressWarnings("deprecation")
public class MessageActivity extends TabActivity {
	private ListView boxList = null;
	private String boxNames[] = new String[]{"收件箱","发件箱","草稿"};
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_manage);
	
		    initView();
			loadPhoneData();
			loadInnerData();
		
			
		}

    private void initView()
    {
    	TabHost tabHost2 = getTabHost();
		TabHost tabHost = tabHost2;
		
		TabSpec tab1 = tabHost.newTabSpec("tab_phone").setIndicator("手机短信") 
				.setContent(R.id.tab_phone); 

		tabHost.addTab(tab1);
		TabSpec tab2 = tabHost.newTabSpec("tab_inner")
	
				.setIndicator("内部短信")
				.setContent(R.id.tab_inner); 

		tabHost.addTab(tab2);
		TabWidget tabWidget = getTabWidget();
		int count = tabWidget.getChildCount();
		for (int i = 0; i < count; i++) {
			View view = tabWidget.getChildTabViewAt(i);

			final TextView tv = (TextView) view
					.findViewById(android.R.id.title);
			tv.setTextSize(20);
			tv.setGravity(BIND_ABOVE_CLIENT);
		}

			
			
    }
	private void loadPhoneData(){
		
		boxList = (ListView) this.findViewById(R.id.entrylist);	
		//ArrayAdapter<?> adapter = new ArrayAdapter<Object>(this,R.layout.menu_list_itemstyle,R.id.boxName,boxNames);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitem_style, boxNames);
		boxList.setAdapter(arrayAdapter );
		boxList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Toast.makeText(MessageActivity.this, "aaaaaaaaa", Toast.LENGTH_SHORT).show();
				
				
				
				if(position==0||position==1)
				{Intent intent = new Intent();
				intent.setClass(MessageActivity.this,MyPhoneMessage.class);
					intent.putExtra(SData.KEY_TITLE,"手机短信"+">"+boxNames[position]);
					intent.putExtra(SData.KEY_MSG_BOX, SData.TYPE_SMS_ARRAY[position]);
					
				intent.putExtra(SData.KEY_URI, SData.URI_SMS_ARRAY[position]);
				startActivity(intent);
				finish();
				}
			
				
			}
		});
		
	}
private void loadInnerData(){
		
		boxList = (ListView) this.findViewById(R.id.entryInnerlist);	
		//ArrayAdapter<?> adapter = new ArrayAdapter<Object>(this,R.layout.menu_list_itemstyle,R.id.boxName,boxNames);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitem_style, boxNames);
		boxList.setAdapter(arrayAdapter );
		boxList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Toast.makeText(MessageActivity.this, "aaaaaaaaa", Toast.LENGTH_SHORT).show();
				if(position==0||position==1){
				Intent intent = new Intent();
				intent.setClass(MessageActivity.this,MyInnerMessage.class);
				intent.putExtra(SData.KEY_TITLE,"内部短信"+">"+boxNames[position]);
				intent.putExtra(SData.KEY_MSG_BOX, SData.TYPE_SMS_ARRAY[position]);
				intent.putExtra(SData.KEY_URI, SData.URI_SMS_ARRAY[position]);

				startActivity(intent);
				finish();
				}
				
			}
		});
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void backHomePage1(View v) {
		Intent intent = new Intent(MessageActivity.this, IndexActivity.class);
		startActivity(intent);
	}
	public void backHomePage01(View v) {
		Intent intent = new Intent(MessageActivity.this, IndexActivity.class);
		startActivity(intent);
	}

	public void send_phone_messaage(View v) {
		Intent intent = new Intent(MessageActivity.this, SendPhoneMessage.class);
		intent.putExtra("message_content", "");
		intent.putExtra("phoneNumber", "");
		intent.putExtra("type", "");
		startActivity(intent);
	}

	public void my_phone_click(View v) {
		//Intent intent1 = new Intent(MessageActivity.this, MyPhoneMessage.class);
		//startActivity(intent1);
		Intent intent2 = new Intent(MessageActivity.this, MyPhoneMessage.class);
		startActivity(intent2);
	}

	public void send_inner_message(View v) {
		Intent intent = new Intent(MessageActivity.this, SendInnerMessage.class);
		intent.putExtra("message_content", "");
		intent.putExtra("phoneNumber", "");
		intent.putExtra("type", "");
		startActivity(intent);
	}

	public void my_inner_click(View v) {
		Intent intent = new Intent(MessageActivity.this, MyInnerMessage.class);
		startActivity(intent);
	}

}
