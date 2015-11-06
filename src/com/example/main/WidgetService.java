package com.example.main;

import utils.CoreGsonUtils;
import utils.Utils_Http;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.example.model.Forcast;
import com.example.model.ResBean;
import com.example.test.R;

public class WidgetService extends Service {
	int allWidgetIds[];
	Context mcon;
	AppWidgetManager appWidgetManager;

	@Override
	public void onStart(Intent intent, int startId) {
		mcon = this;
		appWidgetManager = AppWidgetManager.getInstance(this
				.getApplicationContext());

		allWidgetIds = intent
				.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

		ResponseThread iResponseThread = new ResponseThread();
		iResponseThread.start();

		stopSelf();

		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private class ResponseThread extends Thread {

		@Override
		public void run() {
			super.run();
			SharedPreferences prefs = getSharedPreferences(
					"com.example.shareapp", Context.MODE_PRIVATE);
			String name = prefs.getString("location", "Mumbai");
			String response = Utils_Http.Request(HomeScreen.url + name
					+ HomeScreen.api_key);
			final ResBean iResBin = CoreGsonUtils.fromJson(response,
					ResBean.class);
			RemoteViews remoteViews = new RemoteViews(getApplicationContext()
					.getPackageName(), R.layout.widget_layout);
			if (iResBin != null) {

				remoteViews.setTextViewText(R.id.update, "Location:- "
						+ iResBin.getName() + "\n" + "Temp:- "
						+ iResBin.getMain().getTemp() + "\n" + "Status:- "
						+ iResBin.getWeather().get(0).getDescription());
			}
			// /
			String for_response = Utils_Http
					.Request("http://api.openweathermap.org/data/2.5/forecast?q="
							+ name
							+ "&mode=json&appid=cbec883d7afa3c2c00dd6d5abc2daa05");
			final Forcast f_res = CoreGsonUtils.fromJson(for_response,
					Forcast.class);
			if (f_res != null) {

				String text = "";
				if (f_res != null && f_res.getCod() == 200
						&& f_res.getList().size() > 0) {
					for (int i = 0; i < 4 && i < f_res.getList().size(); i++) {
						text += "Weather Forcaste:- "
								+ (i + 1)
								+ "\n"
								+ "Temp:- "
								+ f_res.getList().get(i).getMain().getTemp()
								+ "\n"
								+ "Status:- "
								+ f_res.getList().get(i).getWeather().get(0)
										.getDescription() + "\n";
					}

				}
				remoteViews.setTextViewText(R.id.forcaste, text);
			}
			Intent clickIntent = new Intent(getApplicationContext(),
					WidgetProvider.class);

			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
					allWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					getApplicationContext(), 0, clickIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
			remoteViews.setOnClickPendingIntent(R.id.layout, pendingIntent);
			appWidgetManager.updateAppWidget(allWidgetIds, remoteViews);

		}
	}

}