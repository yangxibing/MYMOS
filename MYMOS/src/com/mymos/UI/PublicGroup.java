package com.mymos.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mymos.Entity.User;
import com.mymos.Util.RequestAndResponse;
import com.mymos.Util.UserDao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PublicGroup extends Activity {
	private Handler mHandler;
	private RequestAndResponse mRar;
	
	private ListView mListView;
	private List<String> mList;
	private List<String> mContactsList;
	
	private List<String> mGroupIdList;
	private List<String> mGroupNameList;
	
	//默认是用户权限
	private int mPriority = 1;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.public_group);
		
		UserDao userDao = new UserDao(this);
		List<User> list = userDao.queryAll();
		if(!list.toString().equals("[]")){
			mPriority = list.get(0).getPriority();
		}
		Intent intent = this.getIntent();
		final String groupId = intent.getStringExtra("groupId");
		String groupName = intent.getStringExtra("groupName");
		JSONArray jsonGroupId = null;
		JSONArray jsonGroupName = null;
		try {
			jsonGroupId = new JSONArray(intent.getStringExtra("groupIdList"));
			jsonGroupName = new JSONArray(intent.getStringExtra("groupNameList"));
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mGroupIdList = new ArrayList<String>();
		mGroupNameList = new ArrayList<String>();
		
		for(int i = 0;i < jsonGroupId.length(); i++){
			try {
				mGroupIdList.add(jsonGroupId.getString(i));
				mGroupNameList.add(jsonGroupName.getString(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		TextView tv = (TextView) findViewById(R.id.groupName);
		tv.setText(groupName);
		mListView = (ListView) findViewById(R.id.pub_contact_list);
		mList = new ArrayList<String>();
		mContactsList = new ArrayList<String>();
		
		mRar = new RequestAndResponse();
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what == 0x111){
					String infor = msg.obj.toString();
					try {
						JSONArray jsonArray = new JSONArray(infor);
						TextView tvPeopleNum = (TextView) findViewById(R.id.peopelNum);
						tvPeopleNum.setText(Integer.toString(jsonArray.length()));
						for(int i = 0;i < jsonArray.length();i++){
							JSONObject json = jsonArray.getJSONObject(i);
							String name = json.getString("lastName") + json.getString("firstName");
							String contactsId = json.getString("userId");
							
							mList.add(i,name);
							mContactsList.add(i,contactsId);
						}
						ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PublicGroup.this, R.layout.news_details, mList);
						mListView.setAdapter(arrayAdapter);
						
						mListView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								String contactsId = mContactsList.get(position);
								JSONObject json = new JSONObject();
								try {
									json.put("contactsId", contactsId);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								final String infor = json.toString();
						        new Thread(){
						        	
						        	@Override
						        	public void run(){
										try {											
											String contactsDetail = mRar.access("viewContactsDetail", infor);
											Message msg = new Message();
											msg.what = 0x112;
											msg.obj = contactsDetail;
											mHandler.sendMessage(msg);
		
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
							
						});
						

						mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
							@Override
							public boolean onItemLongClick(
									AdapterView<?> parent, View view,
									int position, long id) {
								// TODO Auto-generated method stub
								final String contactsId = mContactsList.get(position);
								//是管理员以上权限
								Log.i("priority public group",mPriority+"");
								if(mPriority > 1){
									new AlertDialog.Builder(PublicGroup.this)  
									.setTitle("")  
									.setItems(new String[] {"转移到分组","转移到个人通讯录"}, new OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											switch(which){
											case 0:
												//转移联系人到分组
												final Spinner spinner = new Spinner(PublicGroup.this);
												
												ArrayAdapter<String> adapter = new ArrayAdapter<String>(PublicGroup.this, android.R.layout.simple_spinner_item, mGroupNameList);
												
												adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
												
												spinner.setAdapter(adapter);
												
												spinner.setVisibility(View.VISIBLE);
												
												new AlertDialog.Builder(PublicGroup.this)
												.setTitle("请选择您要转移的分组")
												.setView(spinner)
												.setPositiveButton("确定", new OnClickListener() {
													
													@Override
													public void onClick(DialogInterface dialog, int which) {
														// TODO Auto-generated method stub
														int position = spinner.getSelectedItemPosition();
														String groupId = mGroupIdList.get(position);
														JSONObject json = new JSONObject();
														try {
															json.put("contactsId", contactsId);
															json.put("groupId", groupId);
														} catch (JSONException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														
														final String sendInfor = json.toString();
														new Thread(){
	
															@Override
															public void run() {
																// TODO Auto-generated method stub
																try {
																	mRar.access("transferContacts", sendInfor);
																} catch (HttpException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																} catch (IOException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
															}
															
														}.start();
														for(int i = 0; i < mContactsList.size();i++){
															if(mContactsList.get(i).equals(contactsId)){
																mContactsList.remove(i);
																mList.remove(i);
																break;
															}
														}
														ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PublicGroup.this, R.layout.listitem_style, mList);
														mListView.setAdapter(arrayAdapter);
														
														Toast.makeText(PublicGroup.this, "转移联系人成功", 2000).show();
													}
												})
												.setNegativeButton("取消",null)
												.show();
												break;
											case 1:	
												new Thread(){

													@Override
													public void run() {
														// TODO Auto-generated method stub
														String personalInfor;
														try {
															personalInfor = mRar.access("transToPersonal", contactsId);
															Message msg = new Message();
															msg.what = 0x321;
															msg.obj = personalInfor;
															mHandler.sendMessage(msg);
														} catch (HttpException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														} catch (IOException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														
													}
													
												}.start();			
												break;
											default:
												break;
													
											}
										}
									})   
									.show(); 
								}
								else{
									new AlertDialog.Builder(PublicGroup.this)  
									.setTitle("")  
									.setItems(new String[] {"转移到个人通讯录"}, new OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											switch(which){
											case 0:
												new Thread(){

													@Override
													public void run() {
														// TODO Auto-generated method stub
														String personalInfor;
														try {
															personalInfor = mRar.access("transToPersonal", contactsId);
															Message msg = new Message();
															msg.what = 0x321;
															msg.obj = personalInfor;
															mHandler.sendMessage(msg);
														} catch (HttpException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														} catch (IOException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														
													}
													
												}.start();	
												break;
											default:
												break;
													
											}
										}
									})   
									.show(); 
								}
								return true;
								
							}
						});
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else if(msg.what == 0x112){
					String contactsDetail = msg.obj.toString();
					Intent intent = new Intent(PublicGroup.this, ContactDetail.class);
					intent.putExtra("contactsDetail", contactsDetail);
					startActivity(intent);
				}
				else if(msg.what == 0x321){
					JSONObject json;
					try {
						json = new JSONObject(msg.obj.toString());
						String name = json.getString("userName");
						String email = json.getString("email");
						String phone = json.getString("phneNumber");
						ContentValues values = new ContentValues();
						// 向RawContacts.CONTENT_URI执行一个空值插入，
						// 目的是获取系统返回的rawContactId
						Uri rawContactUri = getContentResolver().insert(
							RawContacts.CONTENT_URI, values);
						long rawContactId = ContentUris.parseId(rawContactUri);
						values.clear();
						values.put(Data.RAW_CONTACT_ID, rawContactId);
						// 设置内容类型
						values
							.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
						// 设置联系人名字
						values.put(StructuredName.GIVEN_NAME, name);
						// 向联系人URI添加联系人名字
						getContentResolver().insert(
							android.provider.ContactsContract.Data.CONTENT_URI,
							values);
						values.clear();
						values.put(Data.RAW_CONTACT_ID, rawContactId);
						values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
						// 设置联系人的电话号码
						values.put(Phone.NUMBER, phone);
						// 设置电话类型
						values.put(Phone.TYPE, Phone.TYPE_MOBILE);
						// 向联系人电话号码URI添加电话号码
						getContentResolver().insert(
							android.provider.ContactsContract.Data.CONTENT_URI,
							values);
						values.clear();
						values.put(Data.RAW_CONTACT_ID, rawContactId);
						values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
						// 设置联系人的Email地址
						values.put(Email.DATA, email);
						// 设置该电子邮件的类型
						values.put(Email.TYPE, Email.TYPE_WORK);
						// 向联系人Email URI添加Email数据
						getContentResolver().insert(
							android.provider.ContactsContract.Data.CONTENT_URI,
							values);
						Toast.makeText(PublicGroup.this, "联系人转移成功",
							15000).show();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					
					
				}
			}
			
		};
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				JSONObject json = new JSONObject();
				try {
					json.put("groupId", groupId);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String sendInfor = json.toString();
				try {
					String contacts = mRar.access("viewAddressList", sendInfor);
					Message msg = new Message();
					msg.what = 0x111;
					msg.obj = contacts;
					mHandler.sendMessage(msg);
					Log.i("contacts", contacts);
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

public void new_public_group(View v)
{
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