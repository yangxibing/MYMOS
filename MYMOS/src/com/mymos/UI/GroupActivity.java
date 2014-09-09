package com.mymos.UI;

import java.util.ArrayList;
import java.util.List;
import com.mymos.Util.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.Groups;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class GroupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.person_groups);
		
		/*
		ListView person_group = (ListView)findViewById(R.id.person_group_list);
		List<GroupBean> list=new ArrayList<GroupBean>();
		list = queryGroup();
		System.out.println("----------"+list.size());
		String []groupNames = new String[]{};
		for(int i = 0;i<list.size();i++){
			
			groupNames[i] = list.get(i).getName();
			
		}
		ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(
				GroupActivity.this, R.layout.listitem_style, groupNames);
		
		person_group.setAdapter(mArrayAdapter);
		*/
		
		
	}
	/*
	public void new_group(View v) {
		
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setCanceledOnTouchOutside(false);
		dlg.show();
		dlg.setContentView(R.layout.new_group_dialog);
		Window window = dlg.getWindow();
		window.setContentView(R.layout.new_group_dialog);

		Button cancle = (Button) dlg.findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//dlg.cancel(); // �˳��Ի���...
			}
		});
		
		
	}
	/*
	public List<GroupBean> queryGroup(){

		List<GroupBean> list=new ArrayList<GroupBean>();

		GroupBean cg_all=new GroupBean();
		cg_all.setId(0);
		cg_all.setName("全部");
		list.add(cg_all);

		Cursor cur = getContentResolver().query(Groups.CONTENT_URI, null, null, null, null); 
		for (cur.moveToFirst();!(cur.isAfterLast());cur.moveToNext()) {
			if(null!=cur.getString(cur.getColumnIndex(Groups.TITLE))&&(!"".equals(cur.getString(cur.getColumnIndex(Groups.TITLE))))){
				GroupBean cg=new GroupBean();
				cg.setId(cur.getInt(cur.getColumnIndex(Groups._ID)));
				cg.setName(cur.getString(cur.getColumnIndex(Groups.TITLE)));
				list.add(cg);
			}
		}   
		cur.close();
		return list;
	}


	
	*/
	public void new_group(View v) {
	
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		RelativeLayout r =(RelativeLayout) getLayoutInflater().inflate(R.layout.new_group_dialog, null);
		dlg.setView(r);
		dlg.setCanceledOnTouchOutside(false);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.new_group_dialog);
		window.setGravity(Gravity.BOTTOM);
		window.setLayout(getWindowManager().getDefaultDisplay().getWidth(), 600);
		Button cancle = (Button) dlg.findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.cancel(); // �˳��Ի���...
			}
		});
		
	}
	
	

}
