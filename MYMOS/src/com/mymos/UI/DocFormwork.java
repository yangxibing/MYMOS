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

public class DocFormwork extends Activity {

	private String[] formworknames = new String[] { "模板1", "模板2", "模板3", "流程4",
			"模板5", "模板6", "模板7", "模板8", "模板9", "模板10" };
	private String[] date = new String[] { "2014-07-04", "2014-07-04",
			"2014-07-04", "2014-07-04", "2014-07-04", "2014-07-04",
			"2014-07-04", "2014-07-04", "2014-07-04", "2014-07-04" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.doc_formwork);

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < formworknames.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("formworkName", formworknames[i]);
			listItem.put("date", date[i]);
			listItems.add(listItem);
		}

		// 创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.doc_formwork_item, new String[] {
						"formworkName", "date" }, new int[] {
						R.id.doc_formwork_name, R.id.appear_time });

		ListView list = (ListView) findViewById(R.id.doc_formwork_list);
		list.setAdapter(simpleAdapter);

		// 单击事件
		/*
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(DocFormwork.this,
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

				// final AdapterView.AdapterContextMenuInfo info =
				// (AdapterView.AdapterContextMenuInfo) menuInfo;

				menu.setHeaderTitle("选项菜单");

				menu.add(0, 0, 0, "编辑");
				menu.add(0, 1, 0, "删除");
				menu.add(0, 2, 0, "取消");

			}
		});
	}

}