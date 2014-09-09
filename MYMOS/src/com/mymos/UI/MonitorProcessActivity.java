package com.mymos.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MonitorProcessActivity extends Activity {

	private String[] processnames = new String[] { "流程1", "流程2", "流程3", "流程4" };
	private String[] date = new String[] { "2014-07-04", "2014-07-04",
			"2014-07-04", "2014-07-04" };

	// private String[] authornames = new String[] { "虎头", "弄玉", "李清照", "李白" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.monitor_process);

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < processnames.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("processName", processnames[i]);
			listItem.put("date", date[i]);
			listItems.add(listItem);
		}

		// 创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.monitor_process_list_item, new String[] { "processName",
						"date" }, new int[] { R.id.monitor_process_name,
						R.id.apply_time });

		ListView list = (ListView) findViewById(R.id.monitor_process_list);
		list.setAdapter(simpleAdapter);
		
		//单击事件
	
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {

				Intent intent = new Intent(MonitorProcessActivity.this, ProcessDetail.class);
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
				menu.add(0, 1, 0, "取消");

			}
		});
	}
}