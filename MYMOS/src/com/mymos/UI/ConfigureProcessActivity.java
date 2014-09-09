package com.mymos.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.mymos.Util.RequestAndResponse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import android.app.Activity;

@SuppressWarnings("deprecation")
public class ConfigureProcessActivity extends TabActivity {
	private RequestAndResponse mRar;
	private Handler mHandler;
	
	//流程类别
	private List<String> list_category= new ArrayList<String>();
	//表单列表
	private String [] list_form={"表单1","表单2","表单3"};
	//流程名字
	private String[] processnames = new String[] { "流程1", "流程2", "流程3", "流程4" };
	//申请时间
	private String[] date = new String[] { "2014-07-04", "2014-07-04",
			"2014-07-04", "2014-07-04" };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure_process);

		mRar = new RequestAndResponse();
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		// tabHost控件
		TabHost tabHost2 = getTabHost();
		TabHost tabHost = tabHost2;
		TabSpec tab1 = tabHost.newTabSpec("define_form").setIndicator("定义表单")
				.setContent(R.id.define_form);
		tabHost.addTab(tab1);
		TabSpec tab2 = tabHost.newTabSpec("define_process")
				.setIndicator("定义流程").setContent(R.id.define_process);
		tabHost.addTab(tab2);
		TabSpec tab3 = tabHost.newTabSpec("process_category")
				.setIndicator("流程类别").setContent(R.id.process_category);
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

		//获取流程类别、表单
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
			
		}.start();
		
		//定义流程列表
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

				Intent intent = new Intent(ConfigureProcessActivity.this, ProcessDetail.class);
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
		
		
		
		//流程类别列表
		
		ListView categoryList = (ListView)findViewById(R.id.process_category_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>  (this, android.R.layout.select_dialog_item, list_category);
        categoryList.setAdapter(adapter);
         
        categoryList.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent,View arg1,int pos,long id){
                String result = parent.getItemAtPosition(pos).toString();
                Toast.makeText(ConfigureProcessActivity.this, result, Toast.LENGTH_SHORT).show();
                 
            }
             
        });
        
      //流程类别列表
		
      		ListView formList = (ListView)findViewById(R.id.form_list);
              ArrayAdapter<String> adapter1 = new ArrayAdapter<String>  (this, android.R.layout.select_dialog_item, list_form);
              formList.setAdapter(adapter1);
               
              formList.setOnItemClickListener(new OnItemClickListener(){
                  public void onItemClick(AdapterView<?> parent,View arg1,int pos,long id){
                      String result = parent.getItemAtPosition(pos).toString();
                      Toast.makeText(ConfigureProcessActivity.this, result, Toast.LENGTH_SHORT).show();
                       
                  }
                   
              });
		
	}

	// 新表单
	public void new_a_form(View v) {
		Intent intent = new Intent(ConfigureProcessActivity.this,
				NewFormActivity.class);
		startActivity(intent);
	}

	// 新流程
	public void new_a_process(View v) {
		Intent intent = new Intent(ConfigureProcessActivity.this,
				NewProcessActivity.class);
		startActivity(intent);
	}
	// 新类别
	public void new_a_category(View v) {
//			Intent intent = new Intent(ConfigureProcessActivity.this,
//					NewCategoryActivity.class);
//			startActivity(intent);
			final EditText et = new EditText(ConfigureProcessActivity.this);
			new AlertDialog.Builder(ConfigureProcessActivity.this)
			.setTitle("请输入新的流程类别名称")
			.setView(et)
			.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					final String processTypeName = et.getText().toString();
					list_category.add(processTypeName);
					ListView categoryList = (ListView)findViewById(R.id.process_category_list);
			        ArrayAdapter<String> adapter = new ArrayAdapter<String>  (ConfigureProcessActivity.this, android.R.layout.select_dialog_item, list_category);
			        categoryList.setAdapter(adapter);
			        
			        new Thread(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								mRar.access("addProcessType", processTypeName);

							} catch (HttpException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
			        	
			        }.start();
				}

			})
			.setNegativeButton("取消",null)
			.show();
	}

}
