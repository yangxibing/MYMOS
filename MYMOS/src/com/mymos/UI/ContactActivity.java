package com.mymos.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mymos.Entity.User;
import com.mymos.Util.*;

import android.widget.PopupMenu;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class ContactActivity extends TabActivity {

	PopupMenu popup = null;
	private Handler mHandler;
	private RequestAndResponse mRar;
	private ListView mListView;
	private TextView txt;
	private List<String> mList;
	private List<String> mListGroupId;
	private ContactHomeAdapter adapter;
	private ListView personList;
	private List<ContactBean> list;
	private AsyncQueryHandler asyncQuery;
	private QuickAlphabeticBar alpha;
	private Button add;
	private Map<Integer, ContactBean> contactIdMap = null;

	private User mUser;
	// 默认用户权限为1，即普通用户
	private int mPriority = 1;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.contacts);
		personList = (ListView) findViewById(R.id.acbuwa_list);

		alpha = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());

		init();

		// SearchView sv =(SearchView)findViewById(R.id.search_contact);
		// personList.setTextFilterEnabled(true);
		// sv.setOnQueryTextListener(ContactActivity.this);
		Button add = (Button) findViewById(R.id.add_person_contact);
		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setClass(ContactActivity.this, NewContact.class);
				startActivity(intent);

			}

		});

		// 获取用户权限
		UserDao userDao = new UserDao(this);
		List<User> list = userDao.queryAll();
		if (!list.toString().equals("[]")) {
			mUser = list.get(0);
			mPriority = mUser.getPriority();
		}

		mListView = (ListView) findViewById(R.id.lv_contactslist);
		mList = new ArrayList<String>();
		mListGroupId = new ArrayList<String>();
		mRar = new RequestAndResponse();
		//

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0x123:
					try {
						JSONArray json = new JSONArray(msg.obj.toString());
						for (int i = 0; i < json.length(); i++) {
							JSONObject groupInfor = json.getJSONObject(i);
							String groupName = groupInfor
									.getString("groupName");
							String groupId = groupInfor.getString("groupId");
							mListGroupId.add(i, groupId);
							mList.add(i, groupName);
							// RelativeLayout rl = (RelativeLayout)
							// findViewById(R.id.my_public_group);
							// rl.addView(tv, i);
						}
						ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(
								ContactActivity.this, R.layout.listitem_style,
								mList);

						mListView.setAdapter(mArrayAdapter);

						mListView
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										// TODO Auto-generated method stub
										String groupId = mListGroupId
												.get(position);
										String groupName = mList.get(position);
										Intent intent = new Intent(
												ContactActivity.this,
												PublicGroup.class);
										intent.putExtra("groupId", groupId);
										intent.putExtra("groupName", groupName);
										// 把所有分组信息传过去，避免从服务器读
										JSONArray jsonGroupId = new JSONArray(
												mListGroupId);
										JSONArray jsonGroupName = new JSONArray(
												mList);

										intent.putExtra("groupIdList",
												jsonGroupId.toString());
										intent.putExtra("groupNameList",
												jsonGroupName.toString());
										startActivity(intent);

									}
								});

						// 是管理员以上级别才可操作
						if (mPriority > 1) {
							mListView
									.setOnItemLongClickListener(new OnItemLongClickListener() {

										@Override
										public boolean onItemLongClick(
												AdapterView<?> parent,
												View view, int position, long id) {
											// TODO Auto-generated method stub
											final String groupId = mListGroupId
													.get(position);
											new AlertDialog.Builder(
													ContactActivity.this)
													.setTitle("")
													.setItems(
															new String[] {
																	"新建分组",
																	"修改分组",
																	"转移分组",
																	"删除分组" },
															new OnClickListener() {

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	switch (which) {
																	case 0:
																		// 新建分组
																		final EditText et = new EditText(
																				ContactActivity.this);
																		new AlertDialog.Builder(
																				ContactActivity.this)
																				.setTitle(
																						"请输入新的分组名")
																				.setView(
																						et)
																				.setPositiveButton(
																						"确定",
																						new OnClickListener() {

																							@Override
																							public void onClick(
																									DialogInterface dialog,
																									int which) {
																								// TODO
																								// Auto-generated
																								// method
																								// stub
																								final String groupName = et
																										.getText()
																										.toString();
																								if (!groupName
																										.equals("")) {
																									// 将分组信息传到服务器
																									new Thread() {

																										@Override
																										public void run() {
																											// TODO
																											// Auto-generated
																											// method
																											// stub
																											try {
																												String groupInfor = mRar
																														.access("addGroup",
																																groupName);
																												Message msg = new Message();
																												msg.what = 0x124;
																												msg.obj = groupInfor;
																												mHandler.sendMessage(msg);

																												Log.i("groupInfor",
																														groupInfor);
																											} catch (HttpException e) {
																												// TODO
																												// Auto-generated
																												// catch
																												// block
																												e.printStackTrace();
																											} catch (IOException e) {
																												// TODO
																												// Auto-generated
																												// catch
																												// block
																												e.printStackTrace();
																											}
																										}

																									}.start();
																								} else {
																									Toast.makeText(
																											ContactActivity.this,
																											"请输入分组名",
																											1000)
																											.show();
																								}
																							}
																						})
																				.setNegativeButton(
																						"取消",
																						null)
																				.show();

																		break;
																	case 1:
																		// 修改分组
																		final EditText et2 = new EditText(
																				ContactActivity.this);
																		new AlertDialog.Builder(
																				ContactActivity.this)
																				.setTitle(
																						"请输入修改后的分组名")
																				.setView(
																						et2)
																				.setPositiveButton(
																						"确定",
																						new OnClickListener() {

																							@Override
																							public void onClick(
																									DialogInterface dialog,
																									int which) {
																								// TODO
																								// Auto-generated
																								// method
																								// stub
																								String newGroupName = et2
																										.getText()
																										.toString();
																								if (!newGroupName
																										.equals("")) {
																									JSONObject json = new JSONObject();
																									try {
																										json.put(
																												"groupId",
																												groupId);
																										json.put(
																												"groupName",
																												newGroupName);
																									} catch (JSONException e1) {
																										// TODO
																										// Auto-generated
																										// catch
																										// block
																										e1.printStackTrace();
																									}
																									final String groupInfor = json
																											.toString();
																									// 将修改后的分组信息传到服务器
																									new Thread() {

																										@Override
																										public void run() {
																											// TODO
																											// Auto-generated
																											// method
																											// stub
																											try {
																												mRar.access(
																														"reviseGroup",
																														groupInfor);
																												Message msg = new Message();
																												msg.what = 0x125;
																												msg.obj = groupInfor;
																												mHandler.sendMessage(msg);

																												Log.i("groupInfor",
																														groupInfor);
																											} catch (HttpException e) {
																												// TODO
																												// Auto-generated
																												// catch
																												// block
																												e.printStackTrace();
																											} catch (IOException e) {
																												// TODO
																												// Auto-generated
																												// catch
																												// block
																												e.printStackTrace();
																											}
																										}

																									}.start();
																								} else {
																									Toast.makeText(
																											ContactActivity.this,
																											"分组名不能为空",
																											2000)
																											.show();
																								}
																							}
																						})
																				.setNegativeButton(
																						"取消",
																						null)
																				.show();
																		break;
																	case 2:
																		// 转移分组
																		final Spinner spinner = new Spinner(
																				ContactActivity.this);

																		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
																				ContactActivity.this,
																				android.R.layout.simple_spinner_item,
																				mList);

																		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

																		spinner.setAdapter(adapter);

																		spinner.setVisibility(View.VISIBLE);

																		new AlertDialog.Builder(
																				ContactActivity.this)
																				.setTitle(
																						"请选择您要转移的分组")
																				.setView(
																						spinner)
																				.setPositiveButton(
																						"确定",
																						new OnClickListener() {

																							@Override
																							public void onClick(
																									DialogInterface dialog,
																									int which) {
																								// TODO
																								// Auto-generated
																								// method
																								// stub
																								int position = spinner
																										.getSelectedItemPosition();
																								String transferGroupId = mListGroupId
																										.get(position);
																								JSONObject json = new JSONObject();
																								try {
																									json.put(
																											"oldGroupId",
																											groupId);
																									json.put(
																											"newGroupId",
																											transferGroupId);
																								} catch (JSONException e) {
																									// TODO
																									// Auto-generated
																									// catch
																									// block
																									e.printStackTrace();
																								}

																								final String sendInfor = json
																										.toString();
																								new Thread() {

																									@Override
																									public void run() {
																										// TODO
																										// Auto-generated
																										// method
																										// stub
																										try {
																											mRar.access(
																													"transferGroup",
																													sendInfor);
																										} catch (HttpException e) {
																											// TODO
																											// Auto-generated
																											// catch
																											// block
																											e.printStackTrace();
																										} catch (IOException e) {
																											// TODO
																											// Auto-generated
																											// catch
																											// block
																											e.printStackTrace();
																										}
																									}

																								}.start();

																								Toast.makeText(
																										ContactActivity.this,
																										"转移分组成功",
																										2000)
																										.show();
																							}
																						})
																				.setNegativeButton(
																						"取消",
																						null)
																				.show();
																		break;
																	case 3:
																		new AlertDialog.Builder(
																				ContactActivity.this)
																				.setTitle(
																						"确定删除该分组？")
																				.setPositiveButton(
																						"确定",
																						new OnClickListener() {

																							@Override
																							public void onClick(
																									DialogInterface dialog,
																									int which) {
																								// TODO
																								// Auto-generated
																								// method
																								// stub
																								new Thread() {

																									@Override
																									public void run() {
																										// TODO
																										// Auto-generated
																										// method
																										// stub
																										try {
																											mRar.access(
																													"deleteGroup",
																													groupId);
																										} catch (HttpException e) {
																											// TODO
																											// Auto-generated
																											// catch
																											// block
																											e.printStackTrace();
																										} catch (IOException e) {
																											// TODO
																											// Auto-generated
																											// catch
																											// block
																											e.printStackTrace();
																										}
																									}

																								}.start();

																								for (int i = 0; i < mListGroupId
																										.size(); i++) {
																									if (mListGroupId
																											.get(i)
																											.equals(groupId)) {
																										if (mList
																												.get(i)
																												.equals("未分组")) {
																											Toast.makeText(
																													ContactActivity.this,
																													"该分组不能删除",
																													2000)
																													.show();
																											break;
																										}
																										mListGroupId
																												.remove(i);
																										mList.remove(i);
																										break;
																									}
																								}
																								ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(
																										ContactActivity.this,
																										R.layout.listitem_style,
																										mList);

																								mListView
																										.setAdapter(mArrayAdapter);

																							}
																						})
																				.setNegativeButton(
																						"取消",
																						null)
																				.show();
																		break;
																	default:
																		break;

																	}
																}
															}).show();
											return true;
										}
									});
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 0x124:
					try {
						Log.i("addGroup renturn", msg.obj.toString());
						if (msg.obj.toString() != "") {
							JSONObject json = new JSONObject(msg.obj.toString());
							String id = json.getString("id");
							String groupName = json.getString("groupName");
							mListGroupId.add(id);
							mList.add(groupName);

							ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(
									ContactActivity.this,
									R.layout.listitem_style, mList);

							mListView.setAdapter(mArrayAdapter);
						} else {
							Toast.makeText(ContactActivity.this, "该分组已存在", 2000)
									.show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				case 0x125:
					try {
						JSONObject json = new JSONObject(msg.obj.toString());
						String groupId = json.getString("groupId");
						String newGroupName = json.getString("groupName");
						for (int i = 0; i < mListGroupId.size(); i++) {
							if (mListGroupId.get(i).equals(groupId)) {
								mList.set(i, newGroupName);
								break;
							}
						}
						ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(
								ContactActivity.this, R.layout.listitem_style,
								mList);

						mListView.setAdapter(mArrayAdapter);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}

		};

		initalTabHost();

	}

	private void init() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,

				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY }; // 查询的列
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

				contactIdMap = new HashMap<Integer, ContactBean>();

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

					if (contactIdMap.containsKey(contactId)) {

					} else {

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

						contactIdMap.put(contactId, cb);

					}
				}
				System.out.println(list.size());
				if (list.size() > 0) {

					System.out.println(list.size());
					setAdapter(list);
				}
			}
		}

	}

	private void setAdapter(List<ContactBean> list) {
		adapter = new ContactHomeAdapter(this, list, alpha);
		personList.setAdapter(adapter);
		alpha.init(ContactActivity.this);
		alpha.setListView(personList);
		alpha.setHight(alpha.getHeight());
		alpha.setVisibility(View.VISIBLE);
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

							// String threadId =
							// getSMSThreadId(cb.getPhoneNum());
							Intent intent = new Intent();
							intent.setClass(ContactActivity.this,
									SendPhoneMessage.class);
							// Map<String, String> map = new HashMap<String,
							// String>();
							intent.putExtra("phoneNumber", cb.getPhoneNum());
							intent.putExtra("type", "contact");
							startActivity(intent);
							// finish();
							// map.put("threadId", threadId);

							break;
						case 2:
							Intent intent1 = new Intent();
							intent1.setClass(ContactActivity.this,
									SendInnerMessage.class);
							// Map<String, String> map = new HashMap<String,
							// String>();
							intent1.putExtra("phoneNumber", cb.getPhoneNum());
							intent1.putExtra("type", "contact");
							startActivity(intent1);
							// finish();

							break;

						case 3:// 查看详细 修改联系人资料

							// uri = ContactsContract.Contacts.CONTENT_URI;
							// Uri personUri = ContentUris.withAppendedId(uri,
							// cb.getContactId());

							Intent intent2 = new Intent();
							intent2.putExtra("phoneNumber", cb.getPhoneNum());
							intent2.putExtra("name", cb.getDisplayName());
							intent2.putExtra("contactId",
									"" + cb.getContactId());
							// intent2.putExtra("email", ""+cb.getEmail());

							intent2.setClass(ContactActivity.this,
									PersonContactDetail.class);
							startActivity(intent2);

							break;

						case 4:// 移动分组

							// Intent intent3 = null;
							// intent3 = new Intent();
							// intent3.setClass(ContactHome.this,
							// GroupChoose.class);
							// intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							// intent3.putExtra("联系人", contactsID);
							// Log.e("contactsID", "contactsID---"+contactsID);
							// ContactHome.this.startActivity(intent3);
							break;

						case 5:// 移出群组

							// moveOutGroup(getRaw_contact_id(contactsID),Integer.parseInt(qzID));
							break;

						case 6:// 删除

							showDelete(cb.getContactId(), position);
							break;
						}
					}
				}).show();
	}

	// 删除联系人方法
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
								ContactActivity.this.getContentResolver(),
								deleteUri);
						if (lookupUri != Uri.EMPTY) {
							ContactActivity.this.getContentResolver().delete(
									deleteUri, null, null);
						}
						adapter.remove(position);
						adapter.notifyDataSetChanged();
						Toast.makeText(ContactActivity.this, "该联系人已经被删除.",
								Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				}).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflator = new MenuInflater(this);

		inflator.inflate(R.menu.contact_group, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem mi) {

		switch (mi.getItemId()) {
		case R.id.daoru:
			Intent intent = new Intent();
			intent.setClass(this, ImportContactsActivity.class);
			startActivity(intent);
			break;
		case R.id.export:
			Intent intent2 = new Intent();
			intent2.setClass(this, ExportContactsActivity.class);
			startActivity(intent2);
			break;
		default:
			break;

		}
		return true;
	}

	public void initalTabHost() {
		TabHost tabHost2 = getTabHost();
		TabHost tabHost = tabHost2;
		//
		TabSpec tab1 = tabHost.newTabSpec("tab01").setIndicator("个人通讯录")
				.setContent(R.id.tab01);

		tabHost.addTab(tab1);
		TabSpec tab2 = tabHost.newTabSpec("tab02")

		.setIndicator("公共通讯录").setContent(R.id.tab02);
		// .getDrawable(R.drawable.ic_launcher))

		tabHost.addTab(tab2);
		TabWidget tabWidget = getTabWidget();
		int count = tabWidget.getChildCount();//
		for (int i = 0; i < count; i++) {
			View view = tabWidget.getChildTabViewAt(i);

			final TextView tv = (TextView) view
					.findViewById(android.R.id.title);
			tv.setTextSize(20);
			tv.setGravity(Gravity.CENTER);

		}

		Thread thread = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String result = mRar.access("viewAddressListGroup", "");
					Message msg = new Message();
					msg.what = 0x123;
					msg.obj = result;
					mHandler.sendMessage(msg);

					Log.i("addressList", result);
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		thread.start();
	}

	public void enter_myGroup(View v) {

		Intent intent = new Intent();
		intent.setClass(this, GroupActivity.class);
		startActivity(intent);

	}

	public void enter_public_group(View v) {
		Intent intent = new Intent();
		intent.setClass(this, PublicGroup.class);
		startActivity(intent);
	}

	public void new_contact(View v) {
		Intent intent = new Intent();
		intent.setClass(this, NewContactActivity.class);
		startActivity(intent);

	}

	public void enter_contact_detail(View v) {
		Intent intent = new Intent();
		intent.setClass(this, ContactDetail.class);
		startActivity(intent);

	}

	// 通讯录搜索方式下拉菜单
	public void onPopupContactButtonClick(View button) {

		popup = new PopupMenu(this, button);
		getMenuInflater().inflate(R.menu.popup_contact_menu, popup.getMenu());
		// 为popup菜单的菜单项单击事件绑定事件监听器
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {

				case R.id.by_name:
					break;
				case R.id.by_birth:
					break;
				case R.id.by_city:
					break;

				default:
					// 使用Toast显示用户点击的菜单项
					Toast.makeText(ContactActivity.this,
							"您单击了【" + item.getTitle() + "】菜单项",
							Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});

		popup.show();
	}

	public void search_search(View v) {
		Intent intent = new Intent();
		intent.setClass(ContactActivity.this, SearchContact.class);
		startActivity(intent);
		finish();
	}
}
