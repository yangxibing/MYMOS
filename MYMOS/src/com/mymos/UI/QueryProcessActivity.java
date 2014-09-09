package com.mymos.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class QueryProcessActivity extends Activity {

	private String[] processnames = new String[] { "流程1", "流程2", "流程3", "流程4" };
	private String[] date = new String[] { "2014-07-04", "2014-07-04",
			"2014-07-04", "2014-07-04" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.query_process);

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

		ListView list = (ListView) findViewById(R.id.query_process_list);
		list.setAdapter(simpleAdapter);
		
		//单击事件
	
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {

				Intent intent = new Intent(QueryProcessActivity.this, ProcessDetail.class);
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
	
	
	PopupMenu popup = null;
	
	// 短信搜索方式下拉菜单
		public void onQueryPopupButton(View button) {

			popup = new PopupMenu(this, button);
			getMenuInflater().inflate(R.menu.popup_query_process_menu, popup.getMenu());
			// 为popup菜单的菜单项单击事件绑定事件监听器
			popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					switch (item.getItemId()) {

					case R.id.finished:
						break;
					case R.id.unfinished:
						break;

					default:
						// 使用Toast显示用户点击的菜单项
						Toast.makeText(QueryProcessActivity.this,
								"您单击了【" + item.getTitle() + "】菜单项",
								Toast.LENGTH_SHORT).show();
					}
					return true;
				}
			});

			popup.show();
		}
}