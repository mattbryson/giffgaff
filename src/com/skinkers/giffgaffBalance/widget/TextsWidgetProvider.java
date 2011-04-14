package com.skinkers.giffgaffBalance.widget;
import android.net.Uri;

import com.skinkers.giffgaffBalance.R;

public class TextsWidgetProvider extends BaseWidgetProvider 
{
	public TextsWidgetProvider() 
	{
		this.code = "*100*5"+Uri.encode("#");
		this.imagSrcId = R.drawable.ic_launcher_texts;
	}
} 