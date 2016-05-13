
package com.longcheer.reboot;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.graphics.Color;

public class TestFailActivity extends Activity { 

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.result);
		linearLayout.setBackgroundColor(Color.parseColor("#ffff0000"));
	}
}