package com.mymos.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ViewMatter2 extends Activity {

	// 流程名字
	private String[] matternames = new String[] { "事件1", "事件2", "事件3", "事件4" };
	// 申请时间
	private String[] matter_date = new String[] { "2014-09-15", "2014-09-04",
			"2014-09-05", "2014-09-05" };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.matter2);

		// 定义流程列表
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < matternames.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("matterName", matternames[i]);
			listItem.put("date", matter_date[i]);
			listItems.add(listItem);
		}

		// 创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.matter2_item, new String[] { "matterName",
						"date" }, new int[] { R.id.matter2_name,
						R.id.matter2_time });

		ListView list = (ListView) findViewById(R.id.matter2_list);
		list.setAdapter(simpleAdapter);

	

		// 长按事件
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
