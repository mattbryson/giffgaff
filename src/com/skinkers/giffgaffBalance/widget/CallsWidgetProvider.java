package com.skinkers.giffgaffBalance.widget;
import android.net.Uri;

import com.skinkers.giffgaffBalance.R;

public class CallsWidgetProvider extends BaseWidgetProvider 
{
	public CallsWidgetProvider() 
	{
		this.code = "*100"+Uri.encode("#");
		this.imagSrcId = R.drawable.ic_launcher_calls;
	}
}