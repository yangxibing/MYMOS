package com.mymos.UI;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Example extends Activity {
	private HttpClient httpClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.mymos.UI.R.layout.activity_main);

		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HttpGet get = new HttpGet("http://10.10.141.141:8080/MYMOSServer/index.jsp");
				try
				{
					HttpResponse httpResponse = httpClient.execute(get);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						HttpEntity httpEntity = httpResponse.getEntity();
						if(httpEntity != null){
							try{
								BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent(),"UTF-8"),8);
								StringBuilder entityStringBuilder = new StringBuilder();
								String line = null;
								while((line = reader.readLine())!= null){
									entityStringBuilder.append(line+"\n");
								}
								String str = entityStringBuilder.toString();
								TextView tv = (TextView) findViewById(R.id.textView2);
								tv.setText(str);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}

			}
		});
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
