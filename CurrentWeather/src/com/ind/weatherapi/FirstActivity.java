package com.ind.weatherapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FirstActivity extends Activity {
	ListView listview;
	String[] subjects = { " Get Weather by City Id",
			" Get Weather by City Name", " Info" };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		listview = (ListView) findViewById(R.id.listview);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, subjects);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 0) {
					startActivity(new Intent(FirstActivity.this,
							MainActivity.class));
				} else if (arg2 == 1) {
					startActivity(new Intent(FirstActivity.this,
							CityWeather.class));
				} else if (arg2 == 2) {
					startActivity(new Intent(FirstActivity.this,
							DialogActivity.class));
				}
			}
		});
	}
}
