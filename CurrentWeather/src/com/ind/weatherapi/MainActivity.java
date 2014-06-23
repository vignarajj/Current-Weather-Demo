package com.ind.weatherapi;

import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Spinner cities;
	Button submit;
	TextView txt_temp, txt_minmax, txt_humidity, txt_feels;
	String[] listCities = { "Select City", "New Delhi", "New York City",
			"London", "Tokyo", "Moscow", "Berlin", "Dubai", "Cape Town",
			"Paris" };
	int[] cityIds = { 00, 1261481, 5128581, 2643741, 1850147, 524901, 2950159,
			292223, 3369157, 2988507 };
	String response;
	ImageView weatherImage;
	BaseMethods baseMethods;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		cities = (Spinner) findViewById(R.id.cities);
		submit = (Button) findViewById(R.id.submit);
		txt_temp = (TextView) findViewById(R.id.temperature);
		txt_minmax = (TextView) findViewById(R.id.minmax);
		txt_humidity = (TextView) findViewById(R.id.humidity);
		txt_feels = (TextView) findViewById(R.id.feels);
		weatherImage = (ImageView) findViewById(R.id.weatherImage);
		ArrayAdapter<String> getCities = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, listCities);
		cities.setAdapter(getCities);
		baseMethods= new BaseMethods(getApplicationContext());
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (cities.getSelectedItemPosition() == 0) {
					Toast.makeText(MainActivity.this, "Invalid Selection",
							Toast.LENGTH_LONG).show();
				} else {
					if (baseMethods.networkAvailability()) {
						int getPosition = cities.getSelectedItemPosition();
						String getCityPosition = String
								.valueOf(cityIds[getPosition]);
						getWeatherReport(getCityPosition);
					} else {
						Toast.makeText(MainActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();
					}

				}
			}
		});
	}

	public void getWeatherReport(final String id) {
		class GetReport extends AsyncTask<String, String, JSONObject> {
			private ProgressDialog progress = new ProgressDialog(
					MainActivity.this);

			protected void onPreExecute() {
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.setMessage("Loading...");
				progress.setCancelable(false);
				progress.show();
			}

			@Override
			protected JSONObject doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				JSONParser jParser = new JSONParser();
				// Getting JSON from URL
// Put Your App ID, its fake. 
				JSONObject json = jParser
						.getJSONFromUrl("http://api.openweathermap.org/data/2.5/weather?id="
								+ id
								+ "&APPID=11111111111111111111111");
				return json;
			}

			protected void onPostExecute(JSONObject json) {
				JSONObject coord = null, sys = null, mainData = null;
				JSONArray weather = null;
				int cod = 0;
				String msg = null, lat = null, lon = null, sunrise = null, sunset = null, main = null, description = null, icon = null, temp = null, humidity = null, min_temp = null, max_temp = null, pressure = null;
				super.onPostExecute(json);
				progress.dismiss();
				response = json.toString();
				try {
					cod = json.getInt("cod");
					if (cod == 200) {
						coord = json.getJSONObject("coord");
						lat = coord.getString("lat");
						lon = coord.getString("lon");
						sys = json.getJSONObject("sys");
						sunrise = sys.getString("sunrise");
						sunset = sys.getString("sunset");
						mainData = json.getJSONObject("main");
						temp = mainData.getString("temp");
						humidity = mainData.getString("humidity");
						pressure = mainData.getString("pressure");
						min_temp = mainData.getString("temp_min");
						max_temp = mainData.getString("temp_max");
						weather = json.getJSONArray("weather");
						for (int i = 0; i < weather.length(); i++) {
							JSONObject weatherData = weather.getJSONObject(i);
							main = weatherData.optString("main").toString();
							description = weatherData.optString("description");
							icon = weatherData.optString("icon");
						}
						txt_temp.setText(baseMethods.kevinToCelsius(temp) + "\u00b0 C");
						txt_minmax.setText(baseMethods.kevinToCelsius(min_temp) + " / "
								+ baseMethods.kevinToCelsius(max_temp) + "\u00b0 C");
						txt_humidity.setText(humidity + " % Humidity");
						txt_feels.setText(main);
						new DownloadImage()
								.execute("http://openweathermap.org/img/w/"
										+ icon + ".png");
					} else {
						Toast.makeText(MainActivity.this, "Invalid City",
								Toast.LENGTH_LONG).show();
						txt_temp.setText("");
						txt_minmax.setText("");
						txt_humidity.setText("");
						txt_feels.setText("");
						weatherImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.transparent));
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		GetReport getReport = new GetReport();
		getReport.execute();
	}

	//
	private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
		private ProgressDialog mProgressDialog = new ProgressDialog(
				MainActivity.this);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(MainActivity.this);
			// Set progressdialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}

		@Override
		protected Bitmap doInBackground(String... URL) {

			String imageURL = URL[0];

			Bitmap bitmap = null;
			try {
				// Download Image from URL
				InputStream input = new java.net.URL(imageURL).openStream();
				// Decode Bitmap
				bitmap = BitmapFactory.decodeStream(input);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			// Set the bitmap into ImageView
			weatherImage.setImageBitmap(bitmap);
			mProgressDialog.dismiss();
		}
	}
}
