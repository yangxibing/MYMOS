package com.mymos.UI;

import com.mymos.Util.SData;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ListAllSmsPage extends Activity {
	private ListView message_list = null;
 @Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	this.setContentView(R.layout.message_list);
	//message_list = (ListView)findViewById(R.id.phonemessage_list);
	//TextView title = (TextView) findViewById(R.id.my_phone_message_title1);
	//title.setText(getIntent().getStringExtra(SData.KEY_TITLE));

}
}
