package com.example.main;

import org.apache.http.protocol.HTTP;

import utils.Connection_Detector;
import utils.CoreGsonUtils;
import utils.Utils_Http;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.model.Forcast;
import com.example.model.ResBean;
import com.example.test.R;

public class HomeScreen extends Activity {
	Connection_Detector cd;
	TextView display_location;
	EditText location;
	ProgressBar pbar;
	public static String url = "http://api.openweathermap.org/data/2.5/weather?q=";
	public static String api_key = "&appid=cbec883d7afa3c2c00dd6d5abc2daa05";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		location = (EditText) findViewById(R.id.editText1);
		display_location = (TextView) findViewById(R.id.textView1);
		Button get_info = (Button) findViewById(R.id.button1);
		cd = new Connection_Detector(this);
		pbar = (ProgressBar) findViewById(R.id.progressBar1);
		get_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// pbar.setVisibility(View.VISIBLE);
				GetData();

			}
		});
	}

	boolean isPalindrome(String str) {
		int n = str.length();

		for (int i = 0; i < n / 2; i++)
			if (str.charAt(i) != str.charAt(n - i - 1))
				return false;
		return true;
	}

	public void GetData() {

		if (cd.isConnectingToInternet()) {

			ResponseThread iResponseThread = new ResponseThread(location
					.getText().toString().trim());
			iResponseThread.start();
		} else {
			pbar.setVisibility(View.GONE);

			display_location.setText("Check your internet");
		}
	}

	private class ResponseThread extends Thread {
		String name;

		public ResponseThread(String threadName) {
			super(threadName);
			this.name = threadName;
		}

		@Override
		public void run() {
			super.run();

			String response = Utils_Http.Request(url + name + api_key);
			final ResBean iResBin = CoreGsonUtils.fromJson(response,
					ResBean.class);
			String for_response = Utils_Http
					.Request("http://api.openweathermap.org/data/2.5/forecast?q="
							+ name + "&mode=json&" + api_key);
			final Forcast f_res = CoreGsonUtils.fromJson(for_response,
					Forcast.class);

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					pbar.setVisibility(View.GONE);
					if (iResBin != null && iResBin.getCod() == 200) {
						SharedPreferences prefs = getSharedPreferences(
								"com.example.shareapp", Context.MODE_PRIVATE);
						display_location.setText("Name:- " + iResBin.getName()
								+ "\n" + "Temp:- "
								+ iResBin.getMain().getTemp() + "\n"
								+ "Status:- "
								+ iResBin.getWeather().get(0).getDescription());
						prefs.edit().putString("location", name).commit();
					} else {
						display_location.setText("Enter Vaild Location");
					}

					String text = "";
					if (f_res != null && f_res.getCod() == 200
							&& f_res.getList().size() > 0) {
						for (int i = 0; i < 4 && i < f_res.getList().size(); i++) {
							text += "Forcast:- "
									+ (i + 1)
									+ "\n"
									+ "Temp:- "
									+ f_res.getList().get(i).getMain()
											.getTemp()
									+ "\n"
									+ "Status:- "
									+ f_res.getList().get(i).getWeather()
											.get(0).getDescription() + "\n";
						}

						display_location.setText(display_location.getText()
								.toString()
								+ "\n \n  Weather Forcaste \n \n"
								+ text);

					}

				}
			});

		}
	}

}
