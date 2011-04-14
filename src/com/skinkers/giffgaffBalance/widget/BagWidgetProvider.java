package com.skinkers.giffgaffBalance.widget;
import android.net.Uri;

import com.skinkers.giffgaffBalance.R;

public class BagWidgetProvider extends BaseWidgetProvider 
{
	public BagWidgetProvider() 
	{
		this.code = "*100*7"+Uri.encode("#");
		this.imagSrcId = R.drawable.ic_launcher_bag;
	}
}