package com.mymos.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mymos.Util.FileOperation;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class DocumentActivity extends TabActivity {

	private FileOperation mFileOperation;
	private int isReturn = 1;

	private int[] fileimages = new int[] { R.drawable.folder,
			R.drawable.file_icon_default };
	private String[] filenames = new String[] { "虎头", "弄玉", "李清照", "李白" };
	private String[] date = new String[] { "2014-08-04", "2014-08-04",
			"2014-08-04", "2014-08-04" };
	private String[] authornames = new String[] { "虎头", "弄玉", "李清照", "李白" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.docment);

		mFileOperation = new FileOperation();

		mFileOperation.buildDir();

		initalTabHost();

		refreshDoc();

		// //公共文档列表
		// ListView list1 = (ListView) findViewById(R.id.public_doc_list);
		// list1.setAdapter(simpleAdapter);
		//
		// // 单击事件
		//
		// list1.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		//
		// Intent intent = new Intent(DocumentActivity.this, NewDoc.class);
		// startActivity(intent);
		//
		// }
		// });
		//
		// list1.setOnCreateContextMenuListener(new
		// OnCreateContextMenuListener() {
		//
		// @Override
		// public void onCreateContextMenu(ContextMenu menu, View v,
		// ContextMenuInfo menuInfo) {
		// // TODO Auto-generated method stub
		// final AdapterView.AdapterContextMenuInfo info =
		// (AdapterView.AdapterContextMenuInfo) menuInfo;
		//
		// menu.setHeaderTitle("选项菜单");
		//
		// menu.add(0, 0, 0, "编辑");
		// menu.add(0, 1, 0, "删除");
		// menu.add(0, 2, 0, "日志");
		// menu.add(0, 3, 0, "取消");
		//
		// }
		// });
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "sdh", 2000).show();
		refreshDoc();
		super.onRestart();
	}
	
	public void refreshDoc() {
		final List<String> folderNameList = mFileOperation
				.getFolderNameListFrom("");
		final List<String> fileNameList = mFileOperation
				.getFileNameListFrom("");
		// filenames[0] = fileNameList.size()+"";

		// 显示所有文件夹
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < folderNameList.size(); i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("fileImage", fileimages[0]);
			listItem.put("fileName", folderNameList.get(i));
			listItems.add(listItem);
		}

		// 显示所有文档
		for (int i = 0; i < fileNameList.size(); i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("fileImage", fileimages[1]);
			listItem.put("fileName", fileNameList.get(i));
			listItems.add(listItem);
		}

		// 创建一个SimpleAdapter
		// RelativeLayout r =(RelativeLayout)
		// getLayoutInflater().inflate(R.layout.announcemnet_list_item, null);
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.docment_browse_list_item, new String[] { "fileImage",
						"fileName" }, new int[] { R.id.file_image,
						R.id.file_name });

		final ListView list = (ListView) findViewById(R.id.person_doc_list);
		list.setAdapter(simpleAdapter);

		// 单击事件
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position < folderNameList.size()) {
					isReturn = 0;

					final String folderName = folderNameList.get(position);
					final List<String> fileList = mFileOperation
							.getFileNameListFrom(folderName + "/");

					Log.i("fileList size",fileList.size()+"");
					
					// 显示该文件夹下所有文档
					final List<Map<String, Object>> fileListItems = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < fileList.size(); i++) {
						Map<String, Object> listItem = new HashMap<String, Object>();
						listItem.put("fileImage", fileimages[1]);
						listItem.put("fileName", fileList.get(i));
						fileListItems.add(listItem);
					}

					// 创建一个SimpleAdapter
					SimpleAdapter simpleAdapter = new SimpleAdapter(
							DocumentActivity.this, fileListItems,
							R.layout.docment_browse_list_item, new String[] {
									"fileImage", "fileName" }, new int[] {
									R.id.file_image, R.id.file_name });
					list.setAdapter(simpleAdapter);

					list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(DocumentActivity.this,
									NewDoc.class);
							intent.putExtra("docName",
									folderName + "/" + fileList.get(position));
							startActivity(intent);
						}
					});
					
					list.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							final String fileName1 = fileList.get(position);
							final int pos = position;
							new AlertDialog.Builder(DocumentActivity.this)
							.setTitle("选项菜单")
							.setItems(
									new String[] { "转移到文件夹", "删除", "取消" }, new OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											switch(which){
											case 0:
												final Spinner spinner = new Spinner(DocumentActivity.this);
												
												final List<String> fileNamelist = mFileOperation.getFolderNameListFrom("");
												fileNamelist.add(0, "");
												
												ArrayAdapter<String> adapter = new ArrayAdapter<String>(DocumentActivity.this, android.R.layout.simple_spinner_item,fileNamelist);
												
												adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
												spinner.setAdapter(adapter);
												
												
												new AlertDialog.Builder(DocumentActivity.this)  
												.setTitle("")
												.setView(spinner)
												.setPositiveButton("确定", new OnClickListener() {
													
													@Override
													public void onClick(DialogInterface dialog, int which) {
														// TODO Auto-generated method stub
														Log.i("which",which+"");
														String path = fileNamelist.get(spinner.getSelectedItemPosition());
														String fileName = fileName1;
														if(!path.equals("")){
															fileName = path + "/" + fileName;
														}
														mFileOperation.writeFileToSD(fileName, mFileOperation.readSDFile(folderName + "/" +fileName1));
														mFileOperation.deleteSDFile(folderName + "/" +fileName1);
														
														fileListItems.remove(pos);
														// 创建一个SimpleAdapter
														SimpleAdapter simpleAdapter = new SimpleAdapter(
																DocumentActivity.this, fileListItems,
																R.layout.docment_browse_list_item, new String[] {
																		"fileImage", "fileName" }, new int[] {
																		R.id.file_image, R.id.file_name });

														ListView list = (ListView) findViewById(R.id.person_doc_list);
														list.setAdapter(simpleAdapter);
														//refreshDoc();
														Toast.makeText(DocumentActivity.this, "转移成功", 2000).show();
													}
												})
												.setNegativeButton("取消", null)
												.show();
												break;
											case 1:
												mFileOperation.deleteSDFile(folderName + "/" +fileName1);
												
												fileListItems.remove(pos);
												// 创建一个SimpleAdapter
												SimpleAdapter simpleAdapter = new SimpleAdapter(
														DocumentActivity.this, fileListItems,
														R.layout.docment_browse_list_item, new String[] {
																"fileImage", "fileName" }, new int[] {
																R.id.file_image, R.id.file_name });

												ListView list = (ListView) findViewById(R.id.person_doc_list);
												list.setAdapter(simpleAdapter);
												//refreshDoc();
												Toast.makeText(DocumentActivity.this, "已删除", 2000).show();
												break;
											case 2:
												break;
											}
										}
									})
							.show();
							return true;
						}
					});
					
				} else {
					Intent intent = new Intent(DocumentActivity.this,
							NewDoc.class);
					intent.putExtra("docName", fileNameList.get(position-folderNameList.size()));
					startActivity(intent);
				}

			}
		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position < folderNameList.size())
				{
					final String folderName = folderNameList.get(position);
					new AlertDialog.Builder(DocumentActivity.this)
							.setTitle("选项菜单")
							.setItems(
									new String[] { "新建文件夹", "重命名", "删除文件夹",
											"取消" }, new OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											switch(which){
											case 0:
												final EditText et = new EditText(DocumentActivity.this);
												new AlertDialog.Builder(DocumentActivity.this)
												.setTitle("请输入新的文件夹名")
												.setView(et)
												.setPositiveButton("确定", new OnClickListener() {
													
													@Override
													public void onClick(DialogInterface dialog, int which) {
														// TODO Auto-generated method stub
														int status = mFileOperation.buildDir(et.getText().toString());
														if(status == 0){
															refreshDoc();
														}else if(status == 1){
															Toast.makeText(DocumentActivity.this, "创建失败，该文件夹已存在", 2000).show();
														}else{
															Toast.makeText(DocumentActivity.this, "创建失败，未插入SD卡", 2000).show();
														}
													}
												})
												.setNegativeButton("取消", null)
												.show();
												break;
											case 1:
												final EditText et1 = new EditText(DocumentActivity.this);
												new AlertDialog.Builder(DocumentActivity.this)
												.setTitle("请输入新的名称")
												.setView(et1)
												.setPositiveButton("确定", new OnClickListener() {
													
													@Override
													public void onClick(DialogInterface dialog, int which) {
														// TODO Auto-generated method stub
														int status = mFileOperation.renameFile(folderName, et1.getText().toString());
														if(status == 0){
															refreshDoc();
														}else{
															Toast.makeText(DocumentActivity.this, "创建失败，原文件夹不存在", 2000).show();
														}
													}
												})
												.setNegativeButton("取消", null)
												.show();
												break;
											case 2:
												mFileOperation.deleteSDFile(folderName);
												refreshDoc();
												break;
											case 3:
												break;
											default:
												break;
													
											}
										}
									})
							.show();
				}
				else{
					final String fileName1 = fileNameList.get(position - folderNameList.size());
					new AlertDialog.Builder(DocumentActivity.this)
					.setTitle("选项菜单")
					.setItems(
							new String[] { "转移到文件夹", "删除", "取消" }, new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									switch(which){
									case 0:
										final Spinner spinner = new Spinner(DocumentActivity.this);
										
										final List<String> fileNamelist = mFileOperation.getFolderNameListFrom("");
										fileNamelist.add(0, "");
										
										ArrayAdapter<String> adapter = new ArrayAdapter<String>(DocumentActivity.this, android.R.layout.simple_spinner_item,fileNamelist);
										
										adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
										spinner.setAdapter(adapter);
										
										
										new AlertDialog.Builder(DocumentActivity.this)  
										.setTitle("")
										.setView(spinner)
										.setPositiveButton("确定", new OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												// TODO Auto-generated method stub
												Log.i("which",which+"");
												String path = fileNamelist.get(spinner.getSelectedItemPosition());
												String fileName = fileName1;
												if(!path.equals("")){
													fileName = path + "/" + fileName;
												}
												mFileOperation.writeFileToSD(fileName, mFileOperation.readSDFile(fileName1));
												mFileOperation.deleteSDFile(fileName1);
												
												refreshDoc();
												Toast.makeText(DocumentActivity.this, "转移成功", 2000).show();
											}
										})
										.setNegativeButton("取消", null)
										.show();
										break;
									case 1:
										mFileOperation.deleteSDFile(fileName1);
										refreshDoc();
										Toast.makeText(DocumentActivity.this, "已删除", 2000).show();
										break;
									case 2:
										break;
									}
								}
							})
					.show();
				}
				return true;
			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (isReturn == 1) {
			super.onBackPressed();
		} else {
			refreshDoc();
			isReturn = 1;
		}
	}

	public void initalTabHost() {
		TabHost tabHost2 = getTabHost();
		TabHost tabHost = tabHost2;
		//
		TabSpec tab1 = tabHost.newTabSpec("tab01").setIndicator("个人文档")
				.setContent(R.id.tab01);
		tabHost.addTab(tab1);
		TabSpec tab2 = tabHost.newTabSpec("tab02").setIndicator("公共文档")
				.setContent(R.id.tab02);
		tabHost.addTab(tab2);
		TabSpec tab3 = tabHost.newTabSpec("tab03").setIndicator("模板样式")
				.setContent(R.id.tab03);
		tabHost.addTab(tab3);

		TabWidget tabWidget = getTabWidget();
		int count = tabWidget.getChildCount();
		for (int i = 0; i < count; i++) {
			View view = tabWidget.getChildTabViewAt(i);

			final TextView tv = (TextView) view
					.findViewById(android.R.id.title);
			tv.setTextSize(20);
			tv.setGravity(Gravity.CENTER);

		}

	}

	PopupMenu popup = null;

	// 个人文档搜索方式下拉菜单
	public void click_person_query(View button1) {

		popup = new PopupMenu(this, button1);
		getMenuInflater().inflate(R.menu.popup_doc_menu, popup.getMenu());
		// 为popup菜单的菜单项单击事件绑定事件监听器
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {

				case R.id.by_key_word:
					break;
				case R.id.by_content:
					break;
				case R.id.by_creater:
					break;

				default:
					// 使用Toast显示用户点击的菜单项
					Toast.makeText(DocumentActivity.this,
							"您单击了【" + item.getTitle() + "】菜单项",
							Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
		popup.show();
	}

	PopupMenu popup1 = null;

	// 公共文档搜索方式下拉菜单
	public void click_public_query(View button2) {

		popup1 = new PopupMenu(this, button2);
		getMenuInflater().inflate(R.menu.popup_doc_menu, popup1.getMenu());
		// 为popup菜单的菜单项单击事件绑定事件监听器
		popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {

				case R.id.by_key_word:
					break;
				case R.id.by_content:
					break;
				case R.id.by_creater:
					break;

				default:
					// 使用Toast显示用户点击的菜单项
					Toast.makeText(DocumentActivity.this,
							"您单击了【" + item.getTitle() + "】菜单项",
							Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});

		popup1.show();
	}

	/*
	 * public void new_person_document(View v) { final AlertDialog dlg = new
	 * AlertDialog.Builder(this).create(); LinearLayout r = (LinearLayout)
	 * getLayoutInflater().inflate( R.layout.select_document_type_dialog, null);
	 * dlg.setView(r); dlg.setCanceledOnTouchOutside(false);
	 * 
	 * dlg.show();
	 * 
	 * Window window = dlg.getWindow();
	 * window.setContentView(R.layout.select_document_type_dialog);
	 * window.setGravity(Gravity.BOTTOM);
	 * window.setLayout(getWindowManager().getDefaultDisplay().getWidth(), 600);
	 * String types[] = new String[] { "就职报告", "请假条", "公文", "通告", "不使用模板" };
	 * ListView mListView = (ListView) dlg
	 * .findViewById(R.id.documentType_list); ArrayAdapter<String> arrayAdapter
	 * = new ArrayAdapter<String>( dlg.getContext(), R.layout.listitem_style,
	 * types); mListView.setAdapter(arrayAdapter);
	 * 
	 * }
	 */

	// 新建个人文档
	public void new_person_document(View v) {
		Intent intent = new Intent();
		intent.setClass(this, NewDoc.class);
		intent.putExtra("docName", "");
		startActivity(intent);
	}

	// 新建公共文档
	public void new_public_doc(View v) {
		Intent intent = new Intent();
		intent.setClass(this, NewDoc.class);
		startActivity(intent);
	}

	// 文档模板页面
	public void click_formwork(View v) {
		Intent intent = new Intent();
		intent.setClass(this, DocFormwork.class);
		startActivity(intent);
	}

	// 文档样式页面
	public void click_style(View v) {
		Intent intent = new Intent();
		intent.setClass(this, DocStyle.class);
		startActivity(intent);
	}

}
