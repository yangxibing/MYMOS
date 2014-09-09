package com.mymos.UI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mymos.Util.ContactBean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ExportContactsActivity extends Activity {
	private List<ContactBean> list;
	private AsyncQueryHandler asyncQuery;
	private Map<Integer, ContactBean> contactIdMap = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.export_contacts);
		
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		init();
	
	}
 public void scard_out(View v){
	final String fileName = "contact.csv";
    final String pathName = "/sdcard/cantactBackup/";
     File path = new File(pathName);
	 File file = new File(pathName + fileName); 
	    if(!path.exists()){
	    	path.mkdir();
	    }
	    if( !file.exists()) {  
           Log.i("TestFile", "Create the file:" + fileName);  
           try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
       }  
	try {
		FileOutputStream stream = new FileOutputStream(file);
		for (int i = 0; i < list.size(); i++) {
			String name = list.get(i).getDisplayName();
			String phone = list.get(i).getPhoneNum();
			String lineString = name + "," + phone  + "\n";
			byte[] buf = lineString.getBytes();
			stream.write(buf);
		}
		 stream.close(); 
		 Toast.makeText(ExportContactsActivity.this, "个人通讯录写入到"+pathName + fileName, 150000).show();

	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Toast.makeText(ExportContactsActivity.this, "文件写入错误", Toast.LENGTH_LONG).show();
	}

}
	


	
	private void init() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
			
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
				}; // 查询的列
		asyncQuery.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询
	}

	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		@SuppressLint("HandlerLeak")
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		/**
		 * 查询结束的回调函数
		 */
		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {

				//contactIdMap = new HashMap<Integer, ContactBean>();

				list = new ArrayList<ContactBean>();
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					String sortKey = cursor.getString(3);
					int contactId = cursor.getInt(4);
					Long photoId = cursor.getLong(5);
					String lookUpKey = cursor.getString(6);

						ContactBean cb = new ContactBean();
						cb.setDisplayName(name);
						// if (number.startsWith("+86")) {//
						// 去除多余的中国地区号码标志，对这个程序没有影响。
						// cb.setPhoneNum(number.substring(3));
						// } else {
						cb.setPhoneNum(number);
						// }
						cb.setSortKey(sortKey);
					
						cb.setContactId(contactId);
						cb.setPhotoId(photoId);
						cb.setLookUpKey(lookUpKey);
						
						list.add(cb);


					}
				}
				System.out.println(list.size());
				if (list.size() > 0) {

					System.out.println(list.size());
					//setAdapter(list);
				}
			}
		}

	}
