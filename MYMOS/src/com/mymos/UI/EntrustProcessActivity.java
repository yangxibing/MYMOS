package com.mymos.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EntrustProcessActivity extends Activity {

	// 委托类别
	private String[] list_entrust = { "委托1", "委托2", "委托3" };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entrust_process);

		// 委托列表

		ListView entrustList = (ListView) findViewById(R.id.entrust_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, list_entrust);
		entrustList.setAdapter(adapter);

		entrustList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View arg1, int pos,
					long id) {
				String result = parent.getItemAtPosition(pos).toString();
				Toast.makeText(EntrustProcessActivity.this, result,
						Toast.LENGTH_SHORT).show();

			}

		});

	}

	// 新委托
	public void new_a_entrust(View v) {
		Intent intent = new Intent(EntrustProcessActivity.this,
				NewEntrustActivity.class);
		startActivity(intent);
	}

}