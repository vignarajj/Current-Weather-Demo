package com.ind.weatherapi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class BaseMethods {
	Context context;
	public BaseMethods(Context context){
		this.context= context;
	}
	public String kevinToCelsius(String kelvin) {
		double kel, celsius;
		kel = Double.parseDouble(kelvin);
		celsius = kel - 273.15;
		return String.valueOf(celsius).substring(0, 4);
	}

	// Unix timestamp into time format
	public String getDateFormat(String dateformat) {
		long dt = Long.parseLong(dateformat);
		Date date = new Date(dt * 1000L); // *1000 is to convert seconds to
		// milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm"); // the
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
		String formattedDate = sdf.format(date);
		return formattedDate;
	}

	// Network Availablity
	public boolean networkAvailability() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}
}
