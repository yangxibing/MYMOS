package com.mymos.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mymos.Util.SData;
import com.mymos.Util.SmsMmsService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;

import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;
import android.widget.PopupMenu;

import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyPhoneMessage extends Activity {

	PopupMenu popup = null;
	private ListView conversationsList = null;
	private static int scrollY = -1;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> Phonedata = new ArrayList<Map<String, Object>>();

	private String uri = null;
	private int protocol;
	private String bar_title = null;
	private int type;
	private static final int MENU_CLEAR = 1;


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.my_phone_message);
		conversationsList = (ListView) findViewById(R.id.conversations_list);
		this.registerForContextMenu(conversationsList);
		// 设置标题
		TextView title = (TextView) findViewById(R.id.my_phone_message_title);
		
		bar_title = getIntent().getStringExtra(SData.KEY_TITLE);
		title.setText(bar_title);
		Intent intent = getIntent();
		uri = intent.getStringExtra(SData.KEY_URI);
		protocol = getIntent().getIntExtra(SData.KEY_PROTOCOL,SData.PROTOCOL_SMS);
		type = intent.getIntExtra(SData.KEY_MSG_BOX,SData.TYPE_ALL);
		//type = Integer.parseInt(tempType);
		registerListener();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 之所以把设置数据放置此处是因为后续页面可能会有删除动作，影响其数据，当然这样会减慢速度
		loadData();
		if (scrollY != -1) {

			conversationsList.setSelection(scrollY);

		} else {

		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		scrollY = conversationsList.getFirstVisiblePosition();
		// Log.i(TAG, "step "+(step++)+" : onPause()");
	}

	private void loadData() {
		
		Phonedata = new ArrayList<Map<String, Object>>();
		data = SmsMmsService.getConversation(this, uri, protocol);
		
		
		for(int i =0 ;i<data.size();i++){
			Map<String,Object> map = data.get(i);
			int id = Integer.parseInt(map.get(SData.KEY_THREAD_ID).toString());
			//List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
			temp = SmsMmsService.getSMSByThread_id(MyPhoneMessage.this, uri, id);
			Map<String,Object> item = new HashMap<String,Object>();
			int count = 0;
			String date =null;
			String body =null;
			for(int j = 0; j<temp.size();j++){
				if(!temp.get(j).get(SData.KEY_BODY).toString().startsWith("#MYMOS")){
					
					date = temp.get(j).get(SData.KEY_DATE).toString();
					body = temp.get(j).get(SData.KEY_BODY).toString();
					count++;
				}
			}
				if(date !=null && body != null)
				{
					item.put(SData.KEY_NAME, map.get(SData.KEY_NAME).toString());
					item.put(SData.KEY_MSG_COUNT, "("+count+")");
					item.put(SData.KEY_DATE, date);
					item.put(SData.KEY_BODY, body);
					item.put(SData.KEY_THREAD_ID, map.get(SData.KEY_THREAD_ID).toString());
					Phonedata.add(item);
					
				}
		}
		String from[] = null;
		from = new String[] { SData.KEY_NAME, SData.KEY_MSG_COUNT,
				SData.KEY_DATE, SData.KEY_BODY };
		SimpleAdapter adapter = new SimpleAdapter(this, Phonedata,
				R.layout.conversationitem, from, new int[] {
						R.id.conversationname, R.id.conversationmsgcount,
						R.id.conversationdate, R.id.conversationsnippet });
		if (Phonedata != null && !Phonedata.isEmpty())
			conversationsList.setAdapter(adapter);
		

	}

	private void registerListener() {
		conversationsList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent();
				intent.setClass(MyPhoneMessage.this, PhoneMessageList.class);
						
				intent.putExtra(SData.KEY_URI, uri);
				intent.putExtra(SData.KEY_TITLE	,bar_title+ " > "
							+ Phonedata.get(position).get(SData.KEY_NAME).toString());
				// +data.get(position).get(SData.KEY_MSG_COUNT));
				intent.putExtra(SData.KEY_PROTOCOL, protocol);
				intent.putExtra(SData.KEY_THREAD_ID,
						Integer.parseInt(Phonedata.get(position).get(SData.KEY_THREAD_ID).toString()));
				intent.putExtra(SData.KEY_MSG_BOX, type);
				startActivity(intent);
			
			
				

				
			}
			
		});
	}

	// 短信搜索方式下拉菜单
	public void onPopupButtonClick1(View button) {

		popup = new PopupMenu(this, button);
		getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
		// 为popup菜单的菜单项单击事件绑定事件监听器
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {

				case R.id.by_start_time:
					break;
				case R.id.by_end_line:
					break;
				case R.id.by_receiver:
					break;

				default:
					// 使用Toast显示用户点击的菜单项
					Toast.makeText(MyPhoneMessage.this,
							"您单击了【" + item.getTitle() + "】菜单项",
							Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});

		popup.show();
	}

	public void backMessae3(View v) {
		Intent intent = new Intent(MyPhoneMessage.this, MessageActivity.class);
		startActivity(intent);
	}
	private Dialog mDialog = null;
	@Override


	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = menuInfo.position;
		final int thread_id = Integer.parseInt(Phonedata.get(position).get(
				SData.KEY_THREAD_ID).toString());
		switch (item.getItemId()) {
		case MENU_CLEAR:
			new AlertDialog.Builder(this)
					.setTitle("确定要清空吗？")
					.setPositiveButton(R.string.OK,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									
					List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
					temp = SmsMmsService.getSMSByThread_id(MyPhoneMessage.this, uri, thread_id);
					for(int j = 0; j<temp.size();j++){
						if(!temp.get(j).get(SData.KEY_BODY).toString().startsWith("#MYMOS")){
							SmsMmsService.deleteById(MyPhoneMessage.this, (Integer)temp.get(j).get(SData.KEY_ID),type, protocol);	
						
						}

						}
					loadData();
							}})
					.setNegativeButton(R.string.cancle,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									// 什么动作也不执行
								}
							}).create().show();

			break;
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

		menu.add(0, MENU_CLEAR, 0, getResources().getString(R.string.delete));
	
	}

}
