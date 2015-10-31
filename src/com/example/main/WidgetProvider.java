package com.example.main;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class WidgetProvider extends AppWidgetProvider {
	Context mcon;
	private static final String ACTION_CLICK = "ACTION_CLICK";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		ComponentName thisWidget = new ComponentName(context,
				WidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		Intent intent = new Intent(context.getApplicationContext(),
				WidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

		context.startService(intent);
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		mcon = context;

	}

}
