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
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DocStyle extends Activity {

	private String[] stylenames = new String[] { "Style1", "Style2", "Style3", "Style4",
			"Style5", "Style6", "Style7", "Style8", "Style9", "Style10" };
	private String[] date = new String[] { "2014-07-04", "2014-07-04",
			"2014-07-04", "2014-07-04", "2014-07-04", "2014-07-04",
			"2014-07-04", "2014-07-04", "2014-07-04", "2014-07-04" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.doc_style);

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < stylenames.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("styleName", stylenames[i]);
			listItem.put("date", date[i]);
			listItems.add(listItem);
		}

		// 创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.doc_style_item, new String[] {
						"styleName", "date" }, new int[] {
						R.id.doc_style_name, R.id.style_appear_time });

		ListView list = (ListView) findViewById(R.id.doc_style_list);
		list.setAdapter(simpleAdapter);

		// 单击事件
		/*
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(DocStyle.this,
						NewDoc.class);
				startActivity(intent);

			}
		});
		*/
		// 长按事件
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {


				menu.setHeaderTitle("选项菜单");
				
				menu.add(0, 0, 0, "删除");
				menu.add(0, 1, 0, "取消");

			}
		});
	}
}