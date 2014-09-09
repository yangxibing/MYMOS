package com.mymos.UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity {

	private static String serverAdd; 
	private HttpClient httpClient;
	private TextView tv;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == 0x123){
				tv.setText(msg.obj.toString());
			}
		}
		
	};;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

	    tv = (TextView) findViewById(R.id.textView2);
	    //serverAdd = this.getString(R.string.serverAdd);

		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				new Thread(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						httpClient = new HttpClient();
						
						JSONObject json = new JSONObject();
						try {
							json.put("name", "tianye");
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						String str = json.toString();
						
						Log.i("json", str);
						
						//String add = serverAdd+"androidGetName";
						PostMethod postMethod = new PostMethod("http://192.168.23.1:8080/MYMOS_SERVER/androidGetName");
					
						postMethod.setParameter("id", str);
						
						
//						getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
//						System.out.println("getMethod set success");
						
						try {
							int statusCode = httpClient.executeMethod(postMethod);
							if(statusCode != HttpStatus.SC_OK){
								Log.i("getmethod","GetMethod failed:"+postMethod.getStatusLine());
							}
							byte[] responseBody = postMethod.getResponseBody();
							Message msg = new Message();
							msg.what = 0x123;
							msg.obj = new String(responseBody);
							handler.sendMessage(msg);
							
						} catch (HttpException e) {
							// TODO Auto-generated catch block
							//tv.setText("http exception");
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							//tv.setText("io exception");
							e.printStackTrace();
						}
						finally{
							//tv.setText("failed");
							postMethod.releaseConnection();
						}

					}
					
				}.start();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
