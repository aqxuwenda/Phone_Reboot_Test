
package com.longcheer.reboot;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.graphics.Color;


public class TestPassActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.success);
		LinearLayout linearlayout = (LinearLayout) findViewById(R.id.success);
		linearlayout.setBackgroundColor(Color.parseColor("#ff00ff00"));
	}
}