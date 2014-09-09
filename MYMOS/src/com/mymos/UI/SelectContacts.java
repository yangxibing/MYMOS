package com.mymos.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.mymos.Util.*;
import com.mymos.Util.selectContactAdapter.ViewHolder;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;


public class SelectContacts extends Activity implements OnQueryTextListener {
	private selectContactAdapter adapter;
	private ListView personList, lv;
	private List<ContactBean> list;
	private AsyncQueryHandler asyncQuery;
	private QuickAlphabeticBar alpha;
	private ArrayList<String> phoneNumber = new ArrayList<String>();
	private EditText keywords;
	private Button search;
	private String s_Keywords;
	// private ArrayList<String> namelist;
	private Map<Integer, ContactBean> contactIdMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.select_person_contact_list);

		personList = (ListView) findViewById(R.id.acbuwa_list);

		alpha = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		keywords = (EditText) findViewById(R.id.search_content);
		search = (Button) findViewById(R.id.searchforContacts);
        init();
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				s_Keywords = keywords.getText().toString();
				init(s_Keywords);

			}

		});

		// lv = (ListView) findViewById(R.id.lv);
		// lv.setTextFilterEnabled(true);
		// Log.d("size", "" + namelist.size());

		// SearchView sv = (SearchView) findViewById(R.id.searchView1);
		// sv.setOnQueryTextListener(this);
		// sv.setSubmitButtonEnabled(true);

		// sv.setQueryHint("查找联系人");
	}
	private void init() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY }; // 查询的列
		asyncQuery.startQuery(0, null, uri, projection,
				null, null, "sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询
	}

	private void init(String word) {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY }; // 查询的列
		asyncQuery.startQuery(0, null, uri, projection,
				ContactsContract.Data.DISPLAY_NAME + " like " + "'%" + word
						+ "%'", null, "sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询
	}

	/**
	 * 数据库异步查询类AsyncQueryHandler
	 * 
	 * @author administrator
	 * 
	 */
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

	
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		/**
		 * 查询结束的回调函数
		 */
		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {

				contactIdMap = new HashMap<Integer, ContactBean>();

				list = new ArrayList<ContactBean>();
				// namelist = new ArrayList<String>();
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					String sortKey = cursor.getString(3);
					int contactId = cursor.getInt(4);
					Long photoId = cursor.getLong(5);
					String lookUpKey = cursor.getString(6);

					if (contactIdMap.containsKey(contactId)) {

					} else {

						ContactBean cb = new ContactBean();
						cb.setDisplayName(name);
						cb.setPhoneNum(number);
						cb.setSortKey(sortKey);
						cb.setContactId(contactId);
						cb.setPhotoId(photoId);
						cb.setLookUpKey(lookUpKey);
						list.add(cb);
						// namelist.add(name);

						contactIdMap.put(contactId, cb);

					}
				}
				System.out.println(list.size());
				if (list.size() > 0) {

					System.out.println("list" + list.size());

					setAdapter(list);
				}


			}
		}
	}

	private void setAdapter(List<ContactBean> list) {
		if(list.size() ==0){
			Toast.makeText(SelectContacts.this, "没有搜索结果", Toast.LENGTH_LONG).show();
           
		}
		adapter = new selectContactAdapter(this, list, alpha);
		personList.setAdapter(adapter);
		alpha.init(SelectContacts.this);
		alpha.setListView(personList);
		alpha.setHight(alpha.getHeight());
		alpha.setVisibility(View.VISIBLE);
		personList.setItemsCanFocus(false);
		personList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		personList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// adapter.isSelected.put(position, true);
				ViewHolder vHollder = (ViewHolder) view.getTag();
				vHollder.checkB.toggle();
				adapter.isSelected.put(position, vHollder.checkB.isChecked());
				ContactBean cb = (ContactBean) adapter.getItem(position);
				phoneNumber.add(cb.getPhoneNum());

				// showContactDialog(lianxiren1, cb, position);
			}
		});
		Button add_ok = (Button) findViewById(R.id.ok_addcontact);
		add_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String request = SelectContacts.this.getIntent()
						.getStringExtra("request");
				if (request.equals("phone_request")) {
					// TODO Auto-generated method stub
					JSONArray json = new JSONArray(phoneNumber);
					Intent intent = new Intent(SelectContacts.this,
							SendPhoneMessage.class);
					intent.putExtra("phoneNumber", json.toString());
					intent.putExtra("type", "listcontact");
					startActivity(intent);
					finish();
				} else {
					JSONArray json = new JSONArray(phoneNumber);
					Intent intent = new Intent(SelectContacts.this,
							SendInnerMessage.class);
					intent.putExtra("phoneNumber", json.toString());
					intent.putExtra("type", "listcontact");
					startActivity(intent);
					finish();
				}
			}

		});

	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Toast.makeText(this, "您已选择:" + query, Toast.LENGTH_SHORT).show();
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (TextUtils.isEmpty(newText)) {

			lv.clearTextFilter();
		} else {

			lv.setFilterText(newText);
		}
		return true;
	}

}
