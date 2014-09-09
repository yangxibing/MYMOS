package com.mymos.UI;

import com.mymos.Entity.News;
import com.mymos.Util.Bitmap2byteTransfer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsDetails extends Activity {

	private TextView mNewsTitle;
	private TextView mNewsContent;
	private ImageView mNewsImg;
	private Button mComment;
	private Button mReturn;
	private News mTempNews;
	private boolean mNewsIsComment;
	private Bitmap mImageViewBitmap;
	private Bitmap2byteTransfer mB2bT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_item);
		initialViewById();
		loadNews();
	}

	/**
	 * 初始化控件
	 */
	private void initialViewById() {
		mTempNews = new News();
		mB2bT = new Bitmap2byteTransfer();
		mNewsTitle = (TextView) findViewById(R.id.showNewsTitle);
		mNewsContent = (TextView) findViewById(R.id.showNewsContent);
		mNewsContent.setMovementMethod(ScrollingMovementMethod.getInstance()); // 可以滑动
		mNewsImg = (ImageView) findViewById(R.id.showNewsImg);
		mComment = (Button) findViewById(R.id.btn_comment);
		mReturn = (Button) findViewById(R.id.btn_return);
		eventOperate();
	}

	private void eventOperate() {
		/* 返回新闻列表页面 */
		mReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent returnIntent = new Intent(NewsDetails.this,
						NewsViewPager.class);
				NewsDetails.this.finish();
			}
		});

		/* 评论 */
		mComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				/* 允许评论的新闻才有评论 */
				if (mTempNews.getIsComment()) {
					Intent newsIdIntent = new Intent(NewsDetails.this,
							NewsCommentActivity.class);
					newsIdIntent.putExtra("comment-newsId",
							mTempNews.getNewsId()); // 传递要查询评论的新闻id
					Log.i("带评论news --id", mTempNews.getNewsId());
					startActivity(newsIdIntent);
				} else {
					Toast.makeText(NewsDetails.this, "该新闻不允许评论！", 5000).show();
				}
			}
		});

	}

	/**
	 * 将新闻的详细加载到页面
	 */
	private void loadNews() {
		mNewsTitle.setText(getIntent().getStringArrayExtra("newsMsg")[1]); // 加载标题
		mNewsContent.setText(getIntent().getStringArrayExtra("newsMsg")[2]);
        //获取图片
		mNewsImg.setImageBitmap(mB2bT.string2Bye2Bitmap(getIntent()
				.getStringArrayExtra("newsMsg")[3]));
		// 获取新闻是否可以
		mNewsIsComment = Boolean.parseBoolean(getIntent().getStringArrayExtra(
				"newsMsg")[5]);

		mTempNews.setNewsId(getIntent().getStringArrayExtra("newsMsg")[0]); // 用来加载评论
		Log.i("---------带评论news --id",
				getIntent().getStringArrayExtra("newsMsg")[0]);
		Log.i("---------带评论news --id", mTempNews.getNewsId());
		// 保持将新闻信息，在取消编辑时用于恢复
		mTempNews.setTitle(mNewsTitle.getText().toString());
		mTempNews.setContent(mNewsContent.getText().toString());
		mTempNews.setIsComment(Boolean.parseBoolean(getIntent()
				.getStringArrayExtra("newsMsg")[5]));
		mTempNews.setIsTop(Boolean.parseBoolean(getIntent()
				.getStringArrayExtra("newsMsg")[4]));
	}
}
