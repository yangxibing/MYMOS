package com.mymos.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;

@SuppressWarnings("deprecation")
public class ProcessActivity extends TabActivity {

	private String[] processnames = new String[] { "流程1", "流程2", "流程3", "流程4" };
	private String[] date = new String[] { "2014-07-04", "2014-07-04",
			"2014-07-04", "2014-07-04" };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.process_manage);


		// tabHost控件
		TabHost tabHost2 = getTabHost();
		TabHost tabHost = tabHost2;
		TabSpec tab1 = tabHost.newTabSpec("new_process_page")
				.setIndicator("新建流程").setContent(R.id.new_process_page);
		tabHost.addTab(tab1);
		TabSpec tab2 = tabHost.newTabSpec("view_matter").setIndicator("查看事宜")
				.setContent(R.id.view_matter);
		tabHost.addTab(tab2);
		TabSpec tab3 = tabHost.newTabSpec("process_control")
				.setIndicator("流程控制").setContent(R.id.process_control);
		tabHost.addTab(tab3);

		TabWidget tabWidget = getTabWidget();
		int count = tabWidget.getChildCount();
		for (int i = 0; i < count; i++) {
			View view = tabWidget.getChildTabViewAt(i);

			final TextView tv = (TextView) view
					.findViewById(android.R.id.title);
			tv.setTextSize(20);
			tv.setGravity(BIND_ABOVE_CLIENT);

		}
		
		
		
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < processnames.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("processName", processnames[i]);
			listItem.put("date", date[i]);
			listItems.add(listItem);
		}

		// 创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.query_process_list_item, new String[] { "processName",
						"date" }, new int[] { R.id.query_process_name,
						R.id.apply_time });

		ListView list = (ListView) findViewById(R.id.process_list1);
		list.setAdapter(simpleAdapter);
		
		//单击事件
	
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {

				Intent intent = new Intent(ProcessActivity.this, ProcessDetail.class);
				startActivity(intent);
				
			}
		});
		
		//长按事件
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) { 

				// final AdapterView.AdapterContextMenuInfo info =
				// (AdapterView.AdapterContextMenuInfo) menuInfo;

				menu.setHeaderTitle("选项菜单");

				menu.add(0, 0, 0, "删除");
				menu.add(0, 1, 0, "编辑");
				menu.add(0, 2, 0, "查询");
				menu.add(0, 3, 0, "取消");

			}
		});
	}
		

	
	
	//查看事宜三个跳转页面
	//待办事宜
	public void click_matter1(View v) {
		Intent intent = new Intent(ProcessActivity.this,
				ViewMatter1.class);
		startActivity(intent);
	}
	
	//已办事宜
	public void click_matter2(View v) {
		Intent intent = new Intent(ProcessActivity.this,
				ViewMatter2.class);
		startActivity(intent);
	}
	//办结事宜
	public void click_matter3(View v) {
		Intent intent = new Intent(ProcessActivity.this,
				ViewMatter3.class);
		startActivity(intent);
	}
	
	
	// 流程控制四个跳转页面
	// 流程监控
	public void click_monitor_process(View v) {
		Intent intent = new Intent(ProcessActivity.this,
				MonitorProcessActivity.class);
		startActivity(intent);
	}

	// 流程查询
	public void click_query_process(View v) {
		Intent intent = new Intent(ProcessActivity.this,
				QueryProcessActivity.class);
		startActivity(intent);
	}

	// 流程配置
	public void click_config_process(View v) {
		Intent intent = new Intent(ProcessActivity.this,
				ConfigureProcessActivity.class);
		startActivity(intent);
	}

	// 流程委托
	public void click_entrust_process(View v) {
		Intent intent = new Intent(ProcessActivity.this,
				EntrustProcessActivity.class);
		startActivity(intent);
	}
	
	//新流程
	public void new_a_process(View v) {
		Intent intent = new Intent(ProcessActivity.this,
				NewProcessActivity.class);
		startActivity(intent);
	}
}
