package com.mymos.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ShareDocActivity extends Activity {

	private TextView applyView;
	private Spinner spinner;
	private ArrayAdapter adapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_doc);

		spinner = (Spinner) findViewById(R.id.doc_share_spinner);
		applyView = (TextView) findViewById(R.id.spinner_notice);

		// 将可选内容与ArrayAdapter连接起来
		adapter = ArrayAdapter.createFromResource(this,
				R.array.share_doc_value, android.R.layout.simple_spinner_item);

		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

		// 将adapter2 添加到spinner中
		spinner.setAdapter(adapter);

		// 添加事件Spinner事件监听
		spinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());

		// 设置默认值
		spinner.setVisibility(View.VISIBLE);

	}

	// 使用XML形式操作
	class SpinnerXMLSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			applyView.setText("你选择了：" + adapter.getItem(arg2));
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}
}
