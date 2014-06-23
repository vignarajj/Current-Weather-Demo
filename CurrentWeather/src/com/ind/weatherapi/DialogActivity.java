package com.ind.weatherapi;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class DialogActivity extends Activity {
	TextView links;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
//		links= (TextView)findViewById(R.id.lnk1);
//		links.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
