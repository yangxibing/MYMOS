package com.mymos.UI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONObject;

import com.mymos.Entity.News;
import com.mymos.Util.Bitmap2byteTransfer;
import com.mymos.Util.FileOperation;
import com.mymos.Util.RequestAndResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsStatus.NmeaListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNewsActivity extends Activity {

	private Button mBtn_Morn;
	private Button mBtnSubmit;
	private RequestAndResponse mRar;
	private Handler mHandler;
	private EditText mNewsTitle;
	private EditText mNewsContent;
	private CheckBox mIsTop;
	private CheckBox mIsComment;
	private ImageView mNewsImg;
	private Spinner mSpinner;
	private NewsViewPager mNewsvp;
	private Bitmap2byteTransfer mB2bT;
    private String  mBitmapSting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnews);
		mRar = new RequestAndResponse();
		mNewsvp = new NewsViewPager();
		mB2bT=new Bitmap2byteTransfer();
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x111) {
					if (!(msg.obj.toString().isEmpty())) {
						Log.i("添加新闻Id", msg.obj.toString());
						Toast.makeText(AddNewsActivity.this, "添加新闻成功", 2000)
								.show();
						AddNewsActivity.this.finish();
						//
						// // 将新建的新闻信息传递到新闻列表，进行显示
						// News n = new News();
						// n.setNewsId(msg.obj.toString());
						// n.setTitle(mNewsTitle.getText().toString());
						// n.setContent(mNewsContent.getText().toString());
						// /** 设置新闻图片 */
						// n.setImgUrl("");
						// // 获取新闻类型
						// n.setId(getIntent().getStringArrayListExtra(
						// "AllNewsTypeId").get(
						// mSpinner.getSelectedItemPosition()));
						// n.setIsTop(mIsTop.isChecked());
						// n.setIsComment(mIsComment.isChecked());
						// n.setId(getIntent().getStringArrayListExtra(
						// "AllNewsTypeId").get(
						// mSpinner.getSelectedItemPosition()));
						// Log.i("----------------------", n.getTitle());
						// Log.i("----------------------", n.getContent());
						// Log.i("----------------------", n.getId());
						//
						// Intent addNewsSuccessIntent = new Intent(
						// AddNewsActivity.this, NewsViewPager.class);
						// String newsDetail[] = new String[] { n.getNewsId(),
						// n.getTitle(), n.getContent(), n.getImgUrl(),
						// n.getIsTop() + "", n.getIsComment() + "", n.getId()};
						// addNewsSuccessIntent.putExtra("AddNewsSuccess",
						// newsDetail);
						// startActivity(addNewsSuccessIntent);
					}
				}
			}

		};
		initAndHandle();
	}

	private void initAndHandle() {
		mSpinner = (Spinner) findViewById(R.id.spinner);
		mNewsTitle = (EditText) findViewById(R.id.etNewsTitle);
		mNewsContent = (EditText) findViewById(R.id.etNewsContent);
		mIsTop = (CheckBox) findViewById(R.id.cb_top);
		mIsComment = (CheckBox) findViewById(R.id.cb_comment);
		mNewsImg=(ImageView)findViewById(R.id.iv_NewsImg);
		// 选择新闻类型
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				AddNewsActivity.this, android.R.layout.simple_spinner_item,
				getIntent().getStringArrayListExtra("AllNewsTypeName"));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
		mSpinner.setVisibility(View.VISIBLE);

		// mNewImg;

		/* 添加附件 */
		mBtn_Morn = (Button) findViewById(R.id.btn_More);
		mBtn_Morn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				/* 开启Pictures画面Type设定为image */
				intent.setType("image/*");
				/* 使用Intent.ACTION_GET_CONTENT这个Action */
				intent.setAction(Intent.ACTION_GET_CONTENT);
				/* 取得相片后返回本画面 */
				startActivityForResult(intent, 1);
			}
		});
        
		/* 提交新建新闻 */
		mBtnSubmit = (Button) findViewById(R.id.btn_submitNews);
		mBtnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mNewsTitle.getText().toString().isEmpty()
						|| mNewsContent.getText().toString().isEmpty()) {
					Toast.makeText(AddNewsActivity.this, "新闻内容或标题 为空，请重新输入",
							5000).show();
				} else {
					Map<String, String> map = new HashMap<String, String>();
					// 根据所选项获取其Id
					String newsTypeId = getIntent().getStringArrayListExtra(
							"AllNewsTypeId").get(
							mSpinner.getSelectedItemPosition());
					Log.i("新闻类型Id-------------", newsTypeId);
					map.put("newsTypeId", newsTypeId);
					Log.i("spinner +++", mSpinner.getSelectedItem().toString());
					map.put("title", mNewsTitle.getText().toString());
					map.put("content", mNewsContent.getText().toString());
					map.put("imgUrl", mBitmapSting);  //图片
					map.put("isTop", mIsTop.isChecked() + "");
					map.put("isComment", mIsComment.isChecked() + "");

					final JSONObject jsonObj = new JSONObject(map);

					new Thread() {
						@Override
						public void run() {
							// 所选择新闻类型
							try {
								String addNewsReturnMsg = mRar.access(
										"addNews", jsonObj.toString());
								Message msg = new Message();
								msg.what = 0x111;
								msg.obj = addNewsReturnMsg;
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
			}
		}); 
	}
	
	/** 选择图片后返回*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					//PostImage postImg = new PostImage();
					FileOperation fileOperation = new FileOperation();
					byte[] str = fileOperation.readFile("/sdcard/DCIM/Camera/1.jpg");

					Log.d("str",str.length + "");
					
					String infor = "";
					try {
						infor = new String(str,"ISO-8859-1");
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						Log.d("infor",infor.getBytes("ISO-8859-1").length+"");
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					RequestAndResponse rar = new RequestAndResponse();
					try {
						rar.access("addNewsImg", infor);
					} catch (HttpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//postImg.postImg("addNewsImg", infor);
				}
				
			}.start();
			
			
			Uri uri = data.getData();
			Log.e("uri", uri.toString());
			ContentResolver cr = this.getContentResolver();
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(cr
						.openInputStream(uri));
				
				mBitmapSting=mB2bT.bitmap2byte2String(bitmap); //将Bitmap转成String
				
				/* 将Bitmap设定到ImageView */
				mNewsImg.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				Log.e("Exception", e.getMessage(), e);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
