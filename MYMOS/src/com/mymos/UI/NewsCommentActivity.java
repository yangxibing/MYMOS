package com.mymos.UI;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mymos.Entity.Comment;
import com.mymos.Entity.News;
import com.mymos.Util.RequestAndResponse;
import com.mymos.Util.UserDao;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class NewsCommentActivity extends Activity {

	private Button mBtnReturn;
	private Button mBtnAddComment;
	private RequestAndResponse mRar;
	private List<Comment> mCommentList;
	private ArrayList<Map<String, Object>> commentList;
	private ListView list;
	private Handler mHandler;
	private String inputCommentMsg;
	private String currentNewsId;
	private UserDao getCurrentUser;
	private SimpleAdapter sAdapter;
	private EditText input; // 输入评论的输入框
    private String newsId;  //保存传递来的新闻id
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_comment);

		mBtnReturn = (Button) findViewById(R.id.btn_returnNews);
		mBtnAddComment = (Button) findViewById(R.id.addComment);
		list = (ListView) findViewById(R.id.lv_comment);
		commentList = new ArrayList<Map<String, Object>>();

		getCurrentUser = new UserDao(NewsCommentActivity.this); // 用于获取当前登录的用户
		currentNewsId = getIntent().getStringExtra("comment-newsId"); // 获取当前新闻的id
		Log.i("评论----带评论news --id", currentNewsId);
		mRar = new RequestAndResponse();

		mCommentList = new ArrayList<Comment>();

		/* 处理访问服务器的返回数据 */
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub

				/* 加载评论 */
				if (msg.what == 0x001) {
					JSONArray jsonCommentList;
					try {
						jsonCommentList = new JSONArray(msg.obj.toString());
						for (int i = 0; i < jsonCommentList.length(); i++) {
							JSONObject jsonComment = jsonCommentList
									.getJSONObject(i);
							String authorId = jsonComment.getString("authorId");
							String authorName = jsonComment
									.getString("authorName");
							String commentId = jsonComment
									.getString("commentId");
							String content = jsonComment.getString("content");
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date date = sdf.parse(jsonComment
									.getString("commentTime"));
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("content", content);
							map.put("commentTime",
									jsonComment.getString("commentTime"));
							map.put("currentUserName", authorName);
							commentList.add(map);

							Comment mComment = new Comment();

							mComment.setAuthorId(authorId);
							mComment.setId(commentId);
							mComment.setContent(content);
							mComment.setTime(date);
							// 添加进list容器
							mCommentList.add(mComment);
						}

						sAdapter = new SimpleAdapter(NewsCommentActivity.this,
								commentList, R.layout.comment_item_style,
								new String[] { "content", "currentUserName",
										"commentTime" }, new int[] {
										R.id.comment_item_content,
										R.id.comment_item_author,
										R.id.comment_item_time });
						list.setAdapter(sAdapter);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				/* 添加评论 */
				if (msg.what == 0x200) {
					// 返回评论的所有信息***********************
					if (!(msg.obj.toString().isEmpty())) {
						Log.i("添加评论的返回", msg.obj.toString());

						Comment c = new Comment();
						try {
							JSONObject jsonObj = new JSONObject(
									msg.obj.toString());
							c.setAuthorId(jsonObj.getString("authorId"));
							c.setContent(jsonObj.getString("content"));
							c.setId(jsonObj.getString("commentId"));
							c.setNewsId(jsonObj.getString("newsId"));
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm");
							c.setTime(sdf.parse(jsonObj
									.getString("commentTime")));
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("content", jsonObj.getString("content"));
							map.put("commentTime",
									jsonObj.getString("commentTime"));
							String currentUserName = getCurrentUser.queryAll()
									.get(0).getLastName()
									+ getCurrentUser.queryAll().get(0)
											.getFirstName();
							map.put("currentUserName", currentUserName);
							commentList.add(map);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						mCommentList.add(c);
						// 添加评论后为listView重新设置包装的适配器
						sAdapter = new SimpleAdapter(NewsCommentActivity.this,
								commentList, R.layout.comment_item_style,
								new String[] { "content", "currentUserName",
										"commentTime" }, new int[] {
										R.id.comment_item_content,
										R.id.comment_item_author,
										R.id.comment_item_time });
						list.setAdapter(sAdapter);
						Toast.makeText(NewsCommentActivity.this, "添加评论成功", 5000)
								.show();
					}
				}

				/* 删除公告 */
				if (msg.what == 0x222) {
					int index = Integer.parseInt(msg.obj.toString());
					// 删除新闻后，加载
					commentList.remove(index);
					mCommentList.remove(index);
					Log.i("list---size", commentList.size() + "  "
							+ mCommentList.size());
					Log.i("move---position", "  " + index);

					sAdapter = new SimpleAdapter(NewsCommentActivity.this,
							commentList, R.layout.comment_item_style,
							new String[] { "content", "currentUserName",
									"commentTime" }, new int[] {
									R.id.comment_item_content,
									R.id.comment_item_author,
									R.id.comment_item_time });
					list.setAdapter(sAdapter);

					Toast.makeText(NewsCommentActivity.this, "删除评论成功", 2000)
							.show();
				}
			}
		};

		// 获取从新闻详情页面传递来的新闻id,根据id从服务器获取评论
		loadNewsList();
		
		/* 返回 */
		mBtnReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent returnIntent = new Intent(NewsCommentActivity.this,
						NewsDetails.class);
				NewsCommentActivity.this.finish();
			}
		});

		/* 新增评论 */
		mBtnAddComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				inputCommentMsg = null; // 初始化

				/* 创建输入对话框 */
				input = new EditText(NewsCommentActivity.this);
				// input.setText("");
				AlertDialog.Builder builder = new AlertDialog.Builder(
						NewsCommentActivity.this);
				builder.setTitle("输入评论")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(input).setNegativeButton("取消", null);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {

								if (!(input.getText().toString().isEmpty())) {
									inputCommentMsg = input.getText()
											.toString();

									Log.i("输入的评论--", inputCommentMsg + "   新闻");
									if (inputCommentMsg != null) {
										Map<String, String> map = new HashMap<String, String>();
										map.put("newsId", currentNewsId);
										map.put("content", inputCommentMsg);
										// 获得系统当前时间
										map.put("time", getSystemCurrentTime());
										String currentUserId = getCurrentUser
												.queryAll().get(0).getId(); // 只有一个用户
										map.put("authorId", currentUserId);
										final JSONObject jsonObj = new JSONObject(
												map);

										new Thread() {
											@Override
											public void run() {
												try {
													String addCommentReturnMsg = mRar
															.access("commentNews",
																	jsonObj.toString());
													Message msg = new Message();
													msg.what = 0x200;
													msg.obj = addCommentReturnMsg;
													Log.i("返回",
															addCommentReturnMsg
																	+ "   新闻");

													mHandler.sendMessage(msg);
												} catch (HttpException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												} catch (IOException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
											}
										}.start();
									}
								} else {
									Toast.makeText(NewsCommentActivity.this,
											"评论为空，请重新输入", 5000).show();
								}
							}
						});
				builder.show();
			}
		});

		/** 长按删除评论 */
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (getCurrentUser.queryAll().get(0).getPriority() > 1) {
					final Comment c = mCommentList.get(position);
					final int index = position; // 获得所选项index

					AlertDialog.Builder builder = new AlertDialog.Builder(
							NewsCommentActivity.this).setTitle("删除评论")
							.setMessage("删除该评论吗？");

					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									Map<String, Object> map = new HashMap<String, Object>();
									map.put("id", c.getId());
									Log.i("要删除的评论的id-------", c.getId()+"     -----");
									final JSONObject jsonObj = new JSONObject(
											map);
									new Thread() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											try {
												String deleteResult = mRar
														.access("deleteComment",
																jsonObj.toString());
												Message msg = new Message();
												msg.what = 0x222;
												msg.obj = index;
												mHandler.sendMessage(msg);
											} catch (HttpException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
										}
									}.start();
								}
							});

					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								}
							});
					builder.create().show();
				} else {
					Toast.makeText(NewsCommentActivity.this, "您的权限不足，不能删除！",
							5000).show();
				}
				return false;
			}
		});
	}

	/* 获得系统当前时间 */
	private String getSystemCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date curDate = new Date();// 获取当前时间
		String currentTime = formatter.format(curDate);
		Log.i("time", currentTime);
		return currentTime;
	}


	/* 加载新闻评论列表 */
	private void loadNewsList(){
	// 获取从新闻详情页面传递来的新闻id,根据id从服务器获取评论
			 newsId = currentNewsId;

			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Log.i("传参--------", newsId);
						// 访问评论页面************
						String commentList = mRar.access("getNewsComment", newsId);
						Message msg = new Message();
						msg.what = 0x001;
						msg.obj = commentList;
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

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		loadNewsList();
		super.onRestart();
	}
	
	
}
