package com.example.main;

import utils.Connection_Detector;
import utils.CoreGsonUtils;
import utils.Utils_Http;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.model.ResBean;
import com.example.test.R;

public class HomeScreen extends Activity {
	Connection_Detector cd;
	TextView display_location;
	EditText location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		location = (EditText) findViewById(R.id.editText1);
		display_location = (TextView) findViewById(R.id.textView1);
		Button get_info = (Button) findViewById(R.id.button1);
		cd = new Connection_Detector(this);

		get_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GetData();
			}
		});
	}

	public void GetData() {

		if (cd.isConnectingToInternet()) {

			ResponseThread iResponseThread = new ResponseThread(location
					.getText().toString().trim());
			iResponseThread.start();
		} else {
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

			String response = Utils_Http
					.Request("http://api.openweathermap.org/data/2.5/weather?q="
							+ name + "&appid=bd82977b86bf27fb59a04b61b657fb6f");
			final ResBean iResBin = CoreGsonUtils.fromJson(response,
					ResBean.class);

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
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

				}
			});

		}
	}

}
