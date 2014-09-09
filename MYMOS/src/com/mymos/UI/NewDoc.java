package com.mymos.UI;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.mymos.Util.FileOperation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class NewDoc extends Activity {

	private TextView applyView;
	private Spinner spinnerFormwork;
	private Spinner spinnerStyle;
	private ArrayAdapter adapterFormwork;
	private ArrayAdapter adapterStyle;
	
	private FileOperation mFileOperation;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_doc);

		Intent intent = this.getIntent();
		String fileName = intent.getStringExtra("docName");
		
		
		//模板spinner
		spinnerFormwork = (Spinner) findViewById(R.id.formwork_spinner);
		// applyView = (TextView) findViewById(R.id.spinner_notice);

		// 将可选内容与ArrayAdapter连接起来
		adapterFormwork = ArrayAdapter.createFromResource(this,
				R.array.doc_formwork_value, android.R.layout.simple_spinner_item);

		// 设置下拉列表的风格
		adapterFormwork.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

		// 将adapter2 添加到spinner中
		spinnerFormwork.setAdapter(adapterFormwork);
		// 添加事件Spinner事件监听
		// spinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		// 设置默认值
		spinnerFormwork.setVisibility(View.VISIBLE);
		
		
		mFileOperation = new FileOperation();
		if(!fileName.equals("")){
			EditText etDocName = (EditText) findViewById(R.id.editText1);
			if(fileName.contains("/"))
			{
				String tempFileName = fileName.substring(fileName.lastIndexOf("/")+1);
				etDocName.setText(tempFileName);
			}else{
				etDocName.setText(fileName);
			}
			String content = mFileOperation.readSDFile(fileName);
			EditText etDocContent = (EditText) findViewById(R.id.editText3);
			etDocContent.setText(content);
			
		}
		
		
		
		//样式spinner
		spinnerStyle = (Spinner) findViewById(R.id.style_spinner);
		// applyView = (TextView) findViewById(R.id.spinner_notice);

		// 将可选内容与ArrayAdapter连接起来
		adapterStyle = ArrayAdapter.createFromResource(this,
				R.array.doc_style_value, android.R.layout.simple_spinner_item);

		// 设置下拉列表的风格
		adapterStyle.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		// 将adapter2 添加到spinner中
		spinnerStyle.setAdapter(adapterStyle);

		// 添加事件Spinner事件监听
		// spinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());

		// 设置默认值
		spinnerStyle.setVisibility(View.VISIBLE);

	}

	public void onSaveDoc(View v){
		final Spinner spinner = new Spinner(this);
		
		final List<String> fileNamelist = mFileOperation.getFolderNameListFrom("");
		fileNamelist.add(0, "");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,fileNamelist);
		
		adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		spinner.setAdapter(adapter);
		
		
		final EditText etTitle = (EditText) findViewById(R.id.editText1);
		
		new AlertDialog.Builder(NewDoc.this)  
		.setTitle("保存为")
		.setView(spinner)
		.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.i("which",which+"");
				String path = fileNamelist.get(spinner.getSelectedItemPosition());
				String fileName = etTitle.getText().toString();
				if(!path.equals("")){
					fileName = path + "/" + fileName;
				}
				EditText etContent = (EditText) findViewById(R.id.editText3);
				String str = etContent.getText().toString();
				mFileOperation.writeFileToSD(fileName, str);
				
				Toast.makeText(NewDoc.this, "保存成功", 2000).show();
			}
		})
		.setNegativeButton("取消", null)
		.show();

	}
}
