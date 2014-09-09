package com.mymos.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mymos.Util.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.widget.ListView;

import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("HandlerLeak")
public class SearchContact extends Activity {
	private ContactHomeAdapter adapter;
	private ListView personList;
	private List<ContactBean> list;
	private AsyncQueryHandler asyncQuery;
	private QuickAlphabeticBar alpha;
	// private ArrayList<String> phoneNumber = new ArrayList<String>();
	private ArrayList<String> namelist;
	private Map<Integer, ContactBean> contactIdMap = null;
	private EditText keywords;
	private Button search;
	private String s_Keywords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.search);
		personList = (ListView) findViewById(R.id.acbuwa_list);
		alpha = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		keywords =(EditText)findViewById(R.id.search_content);
		search = (Button)findViewById(R.id.searchforContacts);
		init();
		//Log.d("search_string", keywords.getText().toString())
		search.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				s_Keywords = keywords.getText().toString();
				init(s_Keywords);
				
			}
			
		});
		
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
		//String name ="loveyou";
		asyncQuery.startQuery(0, null, uri, projection, ContactsContract.Data.DISPLAY_NAME+" like "
				+"'%"+word+"%'",null,
				
				"sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询
	}

	/**
	 * 数据库异步查询类AsyncQueryHandler
	 * 
	 * @author administrator
	 * 
	 */
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		@SuppressLint("HandlerLeak")
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		/**
		 * 查询结束的回调函数
		 */
		@SuppressLint("UseSparseArrays")
		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {

				contactIdMap = new HashMap<Integer, ContactBean>();

				list = new ArrayList<ContactBean>();
				namelist = new ArrayList<String>();
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

						contactIdMap.put(contactId, cb);

					}
				}
				System.out.println(list.size());
				if (list.size() > 0) {

					System.out.println("list" + list.size());
					//System.out.println("namelist" + namelist.size());
					setAdapter(list);

				}
			}
		}

	}

	private void setAdapter(List<ContactBean> list) {
		adapter = new ContactHomeAdapter(this, list, alpha);
		personList.setAdapter(adapter);
		alpha.init(SearchContact.this);
		alpha.setListView(personList);
		alpha.setHight(alpha.getHeight());
		alpha.setVisibility(View.VISIBLE);
		personList.setItemsCanFocus(false);
		personList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		personList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				ContactBean cb = (ContactBean) adapter.getItem(position);
				
				showContactDialog(lianxiren1, cb, position);
			}
		});

	}

	private String[] lianxiren1 = new String[] { "拨打电话", "发送手机短信", "发送内部信",
			"查看详细", "移动分组", "移出群组", "删除" };

	// 群组联系人弹出页
	private void showContactDialog(final String[] arg, final ContactBean cb,
			final int position) {
		new AlertDialog.Builder(this).setTitle(cb.getDisplayName())
				.setItems(arg, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						Uri uri = null;

						switch (which) {

						case 0:// 打电话
							String toPhone = cb.getPhoneNum();
							uri = Uri.parse("tel:" + toPhone);
							Intent it = new Intent(Intent.ACTION_CALL, uri);
							startActivity(it);
							break;

						case 1:// 发手机短息
							Intent intent = new Intent();
							intent.setClass(SearchContact.this,
									SendPhoneMessage.class);
							intent.putExtra("phoneNumber", cb.getPhoneNum());
							startActivity(intent);
							break;
						case 2:
							Intent intent1 = new Intent();
							intent1.setClass(SearchContact.this,
									SendInnerMessage.class);
							intent1.putExtra("phoneNumber", cb.getPhoneNum());
							startActivity(intent1);

							break;

						case 3:// 查看详细 修改联系人资料

							Intent intent2 = new Intent();
							intent2.putExtra("phoneNumber", cb.getPhoneNum());
							intent2.putExtra("name", cb.getDisplayName());
							intent2.putExtra("contactId",
									"" + cb.getContactId());
							// intent2.putExtra("email", ""+cb.getEmail());

							intent2.setClass(SearchContact.this,
									PersonContactDetail.class);
							startActivity(intent2);

							break;

						case 4:// 移动分组

							break;

						case 5:// 移出群组

							break;

						case 6:// 删除

							showDelete(cb.getContactId(), position);
							break;
						}
					}
				}).show();
	}

	private void showDelete(final int contactsID, final int position) {
		new AlertDialog.Builder(this)
				.setIcon(R.drawable.mymos_launcher)
				.setTitle("是否删除此联系人")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// 源码删除
						Uri deleteUri = ContentUris.withAppendedId(
								ContactsContract.Contacts.CONTENT_URI,
								contactsID);
						Uri lookupUri = ContactsContract.Contacts.getLookupUri(
								SearchContact.this.getContentResolver(),
								deleteUri);
						if (lookupUri != Uri.EMPTY) {
							SearchContact.this.getContentResolver().delete(
									deleteUri, null, null);
						}
						adapter.remove(position);
						adapter.notifyDataSetChanged();
						Toast.makeText(SearchContact.this, "该联系人已经被删除.",
								Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				}).show();
	}

}
