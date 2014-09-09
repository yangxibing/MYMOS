package com.mymos.UI;
import android.content.AsyncQueryHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mymos.Util.SData;
import com.mymos.Util.SmsMmsService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PhoneMessageList extends Activity {
	


   //private AsyncQueryHandler myquery;
	private static int scrollY = -1;
	private static final int MENU_DELETE = 1;
	private static final int MENU_OPTION_DELETE = 2;
	private static final int MENU_MOVE = 2;
	private String title = null;
	private String uri = null;
	private int protocol;
	private int thread_id;
	private int type;
	private ListView smsList = null;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> Phonedata = new ArrayList<Map<String, Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.message_list);
		loadIntentData();
		smsList = (ListView) findViewById(R.id.phonemessage_list);
		this.registerForContextMenu(smsList);
		TextView title = (TextView) findViewById(R.id.my_phone_message_title1);
		title.setText(getIntent().getStringExtra(SData.KEY_TITLE));
		System.out.println(getIntent().getStringExtra(SData.KEY_TITLE));
		String pro [] = new String[]{SData.COL_DATE,SData.COL_BODY,SData.COL_ID};
		//myquery.startQuery(0, null, Uri.parse(uri), pro, "thread_id=", new String[]{thread_id+""}, "date asc");
		 loadData() ;
	

	}

	
	/*
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		@SuppressLint("HandlerLeak")
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}
		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor c) {
			// TODO Auto-generated method stub
			if(c.getCount()>0){
				Map<String,Object>map = null;
			
				while(c.moveToNext()){
					map = new HashMap<String,Object>();
					map.put(SData.KEY_ID, c.getInt(c.getColumnIndex(SData.COL_ID)));
					map.put(SData.KEY_BODY, c.getString(c.getColumnIndex(SData.COL_BODY)));
					map.put(SData.KEY_DATE, 
							getDate(c.getLong(c.getColumnIndex(SData.COL_DATE))));
					data.add(map);
				}
			}
			c.close();

			super.onQueryComplete(token, cookie, c);
		}
		
	}
	
	private static String getDate(long date){
		SimpleDateFormat dateFormat = new SimpleDateFormat(    
                "yyyy-MM-dd HH:mm:ss");//ע�⣬һ��Ҫ�Ѵ�Сд��ԣ�HH��hh�ǲ�ͬ�ģ�    
        Date d = new Date(date);    
        return dateFormat.format(d);  //�õ�����
	}
	*/
	private void loadIntentData() {
		Intent intent = this.getIntent();

		uri = intent.getStringExtra(SData.KEY_URI);
		protocol = intent.getIntExtra(SData.KEY_PROTOCOL, SData.PROTOCOL_SMS);
		thread_id = intent.getIntExtra(SData.KEY_THREAD_ID, 0);
		type = intent.getIntExtra(SData.KEY_MSG_BOX, SData.TYPE_ALL);
		System.out.println("uri:"+uri);
		System.out.println("protocol:"+protocol);
		System.out.println("thread_id:"+thread_id);
		System.out.println("type:"+type);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (scrollY != -1) {
			smsList.setSelection(scrollY);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		scrollY = smsList.getFirstVisiblePosition();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
	}

	private void loadData() {
		Phonedata = new  ArrayList<Map<String, Object>>();
		
		data = SmsMmsService.getSMSByThread_id(PhoneMessageList.this, uri, thread_id);
		if (data.isEmpty())
		{
		String TAG= "yang test";
		Log.i(TAG, "the data is null");
		}
		
		for(int i = 0;i<data.size();i++){
			if(!data.get(i).get(SData.KEY_BODY).toString().startsWith("#MYMOS")){
				
				Map<String,Object> item = new HashMap<String,Object>();
				item.put(SData.KEY_DATE, data.get(i).get(SData.KEY_DATE).toString());
				item.put(SData.KEY_BODY, data.get(i).get(SData.KEY_BODY).toString());
				item.put(SData.KEY_ID, data.get(i).get(SData.KEY_ID).toString());
				Phonedata.add(item);
				
			}

			
		}
		
		String from[] = null;
		from = new String[] { SData.KEY_DATE, SData.KEY_BODY};
				
		SimpleAdapter adapter = new SimpleAdapter(this, Phonedata,
				R.layout.smsitem,from, new int[] {
						R.id.smsdate, R.id.smsbody});
		if (Phonedata != null && !Phonedata.isEmpty())
			smsList.setAdapter(adapter);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		final int position = menuInfo.position;
		switch (item.getItemId()) {
		case MENU_DELETE:
			new AlertDialog.Builder(this)
					.setTitle("确定要删除吗？")
					.setPositiveButton(R.string.OK,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									// 删除操作
		SmsMmsService.deleteById(PhoneMessageList.this, Integer.parseInt(Phonedata.get(position).get(SData.KEY_ID).toString()),type, protocol);
		loadData();
								}
							})
					.setNegativeButton(R.string.cancle,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									// 什么也不做
								}
							}).create().show();
			break;
		case MENU_MOVE:
			Intent intent = new Intent();
			intent.setClass(PhoneMessageList.this, SendPhoneMessage.class);
			String content = data.get(position).get(SData.KEY_BODY).toString();
			intent.putExtra("message_content", content);
			startActivity(intent);
			

		default:
			break;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, MENU_DELETE, 0, R.string.delete);
		menu.add(0,MENU_MOVE,1,getResources().getString(R.string.transmit_m));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		menu.add(0, MENU_OPTION_DELETE, 0, R.string.delete);
		

		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
}
