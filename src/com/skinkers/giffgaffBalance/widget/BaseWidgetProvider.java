package com.skinkers.giffgaffBalance.widget;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.skinkers.giffgaffBalance.R;

public class BaseWidgetProvider extends AppWidgetProvider 
{
	protected String code;
	protected int imagSrcId;
	
	
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) 
    {
	    final int N = appWidgetIds.length;
	
	    // Perform this loop procedure for each App Widget that belongs to this provider
	    for (int i=0; i<N; i++) 
	    {
	        int appWidgetId = appWidgetIds[i];
	
	        // Create an Intent to launch ExampleActivity
	        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:"+code));
	        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
	
	        // Get the layout for the App Widget and attach an on-click listener to the button
	        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.balance_widget);
	        views.setOnClickPendingIntent(R.id.widget_balance_button, pendingIntent);
	        views.setImageViewResource(R.id.widget_balance_button, imagSrcId);

	        
	        // Tell the AppWidgetManager to perform an update on the current App Widget
	        appWidgetManager.updateAppWidget(appWidgetId, views);
	        
	    }
    }
    
}